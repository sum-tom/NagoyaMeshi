package com.NagoyaMeshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NagoyaMeshi.entity.StripeCustomer;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@Controller
public class CreditCardController {

    @Autowired
    private StripeService stripeService;

    @GetMapping("/credit-card")
    public String showCreditCard(Model model, @AuthenticationPrincipal User user) {
        try {
            // UserエンティティからStripeCustomerエンティティを取得
            StripeCustomer stripeCustomer = user.getStripeCustomer();
            if (stripeCustomer == null) {
                throw new Exception("Stripe customer information not found for this user.");
            }
            // StripeCustomerエンティティからStripeの顧客IDを取得
            String stripeCustomerId = stripeCustomer.getStripeCustomerId();
            Customer customer = stripeService.getCustomer(stripeCustomerId);
            model.addAttribute("customer", customer);
            return "creditCardDetails"; // クレジットカード情報を表示するHTMLビュー
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // エラーを表示するHTMLビュー
        }
    }
    
    
    @GetMapping("/user/credit/registration")
    public String registration() {
        return "/user/credit/registration";
    }   
    
    @GetMapping("/credit-card/register")
    public String showRegistrationForm(Model model) {
        // Stripeの公開可能キーをモデルに追加
        model.addAttribute("stripePublicKey", "your_stripe_public_key");
        return "registerCreditCard";
    }

    @PostMapping("/credit-card/save")
    public String saveCreditCard(@AuthenticationPrincipal User user, @RequestParam String token) {
        try {
            stripeService.createOrUpdateCustomer(user.getEmail(), token);
            return "redirect:/credit-card";
        } catch (StripeException e) {
            return "redirect:/error";
        }
    }
    
    
}