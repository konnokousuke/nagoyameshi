package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {
    // シークレットキーをapplication.propertiesから読み込む
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    // コンストラクタでStripe APIキーを初期化
    public StripeService(@Value("${stripe.api-key}") String stripeApiKey) {
        Stripe.apiKey = stripeApiKey; // Stripe APIの初期化をコンストラクタで行う
    }

    // 顧客の作成
    public Customer createCustomer(Member member) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(member.getEmail())
                .setName(member.getName())
                .setPhone(member.getPhoneNumber())
                .build();

        return Customer.create(params);
    }
    
    // サブスクリプションを作成
    public Subscription createSubscription(Member member, String priceId) throws StripeException {
        // Stripe顧客を作成（または既存の顧客を利用）
        Customer customer = createCustomer(member);

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId()) // 作成した顧客IDを設定
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId) // 価格IDを設定
                        .build())
                .build();

        return Subscription.create(params);
    }

    // checkout.session.completedイベントの処理
    public void processSessionCompleted(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        
        if (deserializer.getObject().isPresent()) {
            StripeObject stripeObject = deserializer.getObject().get();
            if (stripeObject instanceof Session) {
                Session session = (Session) stripeObject;

                String customerId = session.getCustomer();
                String subscriptionId = session.getSubscription();

                // 顧客IDとサブスクリプションIDの処理
                System.out.println("Customer ID: " + customerId);
                System.out.println("Subscription ID: " + subscriptionId);
            }
        } else {
            // デシリアライズ失敗時のエラーハンドリング
            System.out.println("Failed to deserialize Stripe object.");
        }
    }

    // Stripe Checkoutセッションを作成
    public Session createCheckoutSession(Member member, String priceId) throws StripeException {
        // 顧客の作成
        Customer customer = createCustomer(member);

        // Checkoutセッションの作成
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // カード決済を許可
                .setCustomer(customer.getId()) // 顧客IDを設定
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // サブスクリプションモードを設定
                .setSuccessUrl("http://localhost:8080/success?session_id={CHECKOUT_SESSION_ID}") // 成功時のリダイレクトURL
                .setCancelUrl("http://localhost:8080/auth/cancel") // キャンセル時のリダイレクトURL
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId) // 価格IDを設定
                        .setQuantity(1L) // 数量を設定
                        .build())
                .build();

        return Session.create(params); // セッションを作成
    }

    // セッションIDからSessionオブジェクトを取得
    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId); // セッションIDからSessionを取得
    }
}
