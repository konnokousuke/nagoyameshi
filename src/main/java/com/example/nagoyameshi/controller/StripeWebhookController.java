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
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.service.MemberService;
import com.example.nagoyameshi.service.VerificationTokenService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    private final MemberService memberService;
    private final VerificationTokenService verificationTokenService;
    private final SignupEventPublisher signupEventPublisher;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeWebhookController(MemberService memberService, VerificationTokenService verificationTokenService, SignupEventPublisher signupEventPublisher) {
        this.memberService = memberService;
        this.verificationTokenService = verificationTokenService;
        this.signupEventPublisher = signupEventPublisher;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        logger.info("Received Stripe Webhook with payload: {}", payload);  // 受け取ったペイロードをログに出力
        logger.info("Stripe-Signature header: {}", sigHeader);  // 署名ヘッダーをログに出力

        try {
            // Stripeのイベントを検証
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            logger.info("Stripe Event type: {}", event.getType());  // イベントの種類をログに出力

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String email = session.getCustomerDetails().getEmail();
                    logger.info("Email from session: {}", email);  // 取得したメールアドレスをログに出力
                    Member member = memberService.findByEmail(email);
                    
                    if (member != null) {
                        // 認証トークンを生成し、メール送信
                        VerificationToken token = verificationTokenService.createVerificationToken(member);
                        signupEventPublisher.publishSignupEvent(member, token.getToken());
                        logger.info("Verification email sent to: {}", email);  // メール送信が成功したことをログに出力
                    } else {
                        logger.warn("No member found for email: {}", email);  // メンバーが見つからない場合の警告をログに出力
                    }
                } else {
                    logger.warn("Session data is null");  // セッションがnullの場合の警告をログに出力
                }
            }
            return ResponseEntity.ok("Received webhook");
        } catch (SignatureVerificationException e) {
            logger.error("Stripe signature verification failed: {}", e.getMessage());  // シグネチャ検証エラーをログに出力
            return ResponseEntity.status(400).body("Invalid Stripe signature");
        } catch (Exception e) {
            logger.error("Error processing webhook", e);  // 他のエラーをログに出力
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
