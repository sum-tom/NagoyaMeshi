package com.NagoyaMeshi.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerListParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public Customer getCustomer(String customerId) throws StripeException {
        return Customer.retrieve(customerId);
    }
    
    public Customer createOrUpdateCustomer(String email, String token) throws StripeException {
        // 顧客のemailを使って既存の顧客を検索
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .build();
        List<Customer> customers = Customer.list(params).getData();

        if (customers.size() > 0) {
            // 既存の顧客が見つかった場合、その顧客情報を更新
            Customer existingCustomer = customers.get(0); // 仮定として、最初のマッチした顧客を取得
            CustomerUpdateParams updateParams = CustomerUpdateParams.builder()
                    .setSource(token) // 新しい支払い方法を設定
                    .build();
            return existingCustomer.update(updateParams);
        } else {
            // 既存の顧客が見つからない場合、新しい顧客を作成
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", email);
            customerParams.put("source", token);
            return Customer.create(customerParams);
        }
    }
    
    
    public Subscription createSubscription(String customerId, String planId) throws StripeException {
        SubscriptionCreateParams params =
            SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(
                    SubscriptionCreateParams.Item.builder()
                    .setPlan(planId)
                    .build()
                )
                .build();

        return Subscription.create(params);
    }
    
    public void deleteCustomer(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        customer.delete();
    }
    
    public Session createCheckoutSession() throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice("price_1OrL8BFrWKO0sh5QVYNAvvws") // ここにプライスIDを設定
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://example.com/success")
                .setCancelUrl("https://example.com/cancel")
                .build();

        return Session.create(params);
    }
    
    public Subscription subscribeCustomerToPlan(String email, String token, String planId) throws StripeException {
        Customer customer = createOrUpdateCustomer(email, token);
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId())
                .addItem(SubscriptionCreateParams.Item.builder().setPlan(planId).build())
                .build();
        return Subscription.create(params);
    }
    
    public Subscription createSubscription(String email, String token, String planId) throws StripeException {
        Customer customer = createOrUpdateCustomer(email, token);
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId())
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice("price_1OrL8BFrWKO0sh5QVYNAvvws")
                        .build())
                .build();
        return Subscription.create(params);
    }
    
    public List<PaymentMethod> getPaymentMethods(String customerId) throws StripeException {
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .setType(PaymentMethodListParams.Type.CARD)
                .build();
        return PaymentMethod.list(params).getData();
    }
    
}

