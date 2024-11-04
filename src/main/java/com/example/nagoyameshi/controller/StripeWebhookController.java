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

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().map(o -> (Session) o).orElse(null);

                if (session != null) {
                    String customerId = session.getCustomer();
                    String email = session.getCustomerDetails().getEmail();

                    logger.info("Customer ID from session: {}", customerId);
                    logger.info("Email from session: {}", email);

                    // メールアドレスで会員を検索
                    Member member = memberService.findByEmail(email);

                    if (member == null) {
                        logger.error("Member not found for email: {}", email);
                        return ResponseEntity.status(404).body("Member not found");
                    }

                    // 会員のステータスをPAIDに変更し、役割を更新
                    member.setStatus(Status.PAID);
                    member.setCustomerId(customerId);
                    memberService.updateRoleToPaid(member); // 役割をPAIDに更新
                    memberService.saveAndFlush(member);
                    memberService.setPaidRegistrationFlag(member.getEmail(), true);

                    logger.info("Member {} status updated to PAID with customerId {}", member.getEmail(), customerId);
                    return ResponseEntity.ok("有料会員登録が完了しました");
                } else {
                    logger.warn("Session data is null");
                }
            }
            return ResponseEntity.ok("Received webhook");
        } catch (SignatureVerificationException e) {
            logger.error("Stripe signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(400).body("Invalid Stripe signature");
        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
