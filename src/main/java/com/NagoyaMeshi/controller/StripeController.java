package com.NagoyaMeshi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.NagoyaMeshi.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@RestController
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/api/stripe/charge")
    public Map<String, Object> handleCharge(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String email = payload.get("email"); // ペイロードからemailを取得
        Map<String, Object> response = new HashMap<>();

        try {
            // emailとトークンを使用して顧客を作成または更新
            Customer customer = stripeService.createOrUpdateCustomer(email, token); // emailも引数に追加
            
            // 応答に顧客情報を追加
            response.put("status", "success");
            response.put("customerId", customer.getId());
        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }
}