package com.example.nagoyameshi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Member.Status;
import com.example.nagoyameshi.service.MemberService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);
    private final MemberService memberService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeWebhookController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        logger.info("Received Stripe Webhook with payload: {}", payload);
        logger.info("Stripe-Signature header: {}", sigHeader);

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            logger.info("Stripe Event type: {}", event.getType());

            // checkout.session.completedイベントの処理
            if ("checkout.session.completed".equals(event.getType())) {
                handleCheckoutSessionCompleted(event);
            }

            // customer.subscription.deletedイベントの処理
            if ("customer.subscription.deleted".equals(event.getType())) {
                handleSubscriptionDeleted(event);
            }

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (SignatureVerificationException e) {
            logger.error("Stripe signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(400).body("Invalid Stripe signature");
        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Session session = (Session) deserializer.getObject().map(o -> (Session) o).orElse(null);

        if (session != null) {
            String customerId = session.getCustomer();
            String email = session.getCustomerDetails().getEmail();

            logger.info("Customer ID from session: {}", customerId);
            logger.info("Email from session: {}", email);

            Member member = memberService.findByEmail(email);

            if (member == null) {
                logger.error("Member not found for email: {}", email);
                return;
            }

            member.setStatus(Status.PAID);
            member.setCustomerId(customerId);
            memberService.updateRoleToPaid(member); // 役割をPAIDに更新
            memberService.saveAndFlush(member);

            // フラグを設定
            memberService.setPaidRegistrationFlag(member.getEmail(), true); 

            // 成功メッセージをログに記録
            logger.info("有料会員登録が完了しました。ログインし直してください。");

        } else {
            logger.warn("Session data is null");
        }
    }

    private void handleSubscriptionDeleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Subscription subscription = (Subscription) deserializer.getObject().map(o -> (Subscription) o).orElse(null);

        if (subscription != null) {
            String customerId = subscription.getCustomer();
            logger.info("Subscription deleted for customer ID: {}", customerId);

            // customer_idで会員を検索
            Member member = memberService.findByCustomerId(customerId);

            if (member == null) {
                logger.error("Member not found for customer ID: {}", customerId);
                return;
            }

            // ステータスをFREEに設定し、role_idを無料会員に更新
            member.setStatus(Status.FREE);
            member.setCustomerId(null); // customer_idをnullに設定
            memberService.downgradeToFreeMember(member); // 役割を無料会員に更新
            memberService.saveAndFlush(member);

            logger.info("Member {} downgraded to FREE membership", member.getEmail());
        } else {
            logger.warn("Subscription data is null");
        }
    }
}
