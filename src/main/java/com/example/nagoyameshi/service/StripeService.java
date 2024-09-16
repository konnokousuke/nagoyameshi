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
	private String stripeApitKey;
	
	 // コンストラクタまたは初期化時にStripe APIキーを設定
	public StripeService() {
		
	}
	
	// Stripeの処理が必要な場所でAPIキーを設定
	public void setupStripe() {
		Stripe.apiKey = stripeApitKey;
	}
	
	// 顧客の作成
	public Customer createCustomer(Member member) throws StripeException {
		Stripe.apiKey = stripeApitKey;
		
		CustomerCreateParams params = CustomerCreateParams.builder()
				.setEmail(member.getEmail())
				.setName(member.getName())
				.setPhone(member.getPhoneNumber())
				.build();
		
		return Customer.create(params);
	}
	
	// サブスクリプションを作成
	public Subscription createSubscription(Member member, String priceId) throws StripeException {
		Stripe.apiKey = stripeApitKey;
		
		// 既存のStripe顧客がいるかどうかを確認し、なければ作成する
		Customer customer = createCustomer(member);
		
		SubscriptionCreateParams params = SubscriptionCreateParams.builder()
				.setCustomer(customer.getId()) // 作成した顧客IDを設定
				.addItem(SubscriptionCreateParams.Item.builder()
						.setPrice(priceId) // ダッシュボードで設定した価格ID
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
	            
	            // ロジックに必要な操作を実行
	            System.out.println("Customer ID: " + customerId);
	            System.out.println("Subscription ID: " + subscriptionId);
	        }
	    } else {
	        // Deserialization failed, handle the error
	        System.out.println("Failed to deserialize Stripe object.");
	    }
	}
	
	// Stripe Checkoutセッションを作成
	public Session createCheckoutSession(Member member, String priceId) throws StripeException {
	    setupStripe(); // APIキーを設定

	    // Stripeで顧客を作成
	    Customer customer = createCustomer(member);

	    // Checkoutセッションの作成
	    SessionCreateParams params = SessionCreateParams.builder()
	    	.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) 
	        .setCustomer(customer.getId())
	        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
	        .setSuccessUrl("https://your-site.com/success") // 成功時にリダイレクトするURL
	        .setCancelUrl("https://your-site.com/cancel")   // キャンセル時にリダイレクトするURL
	        .addLineItem(SessionCreateParams.LineItem.builder()
	            .setPrice(priceId)
	            .setQuantity(1L)
	            .build())
	        .build();

	    return Session.create(params);
	}

}
