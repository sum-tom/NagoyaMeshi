package com.NagoyaMeshi.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NagoyaMeshi.entity.StripeCustomer;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;

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

            // 顧客IDに紐づく支払い方法のリストを取得
            List<PaymentMethod> paymentMethods = stripeService.getPaymentMethods(stripeCustomerId);

            // モデルに顧客と支払い方法のリストを追加
            model.addAttribute("paymentMethods", paymentMethods);

            // クレジットカード情報を表示するHTMLビューへ
            return "creditCardDetails";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            // エラーを表示するHTMLビューへ
            return "error";
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
    
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(value = StripeException.class)
        public String handleStripeException(StripeException e, Model model) {
            // Stripeのエラーをハンドル
            model.addAttribute("error", "Payment processing error: " + e.getMessage());
            return "error";
        }

        @ExceptionHandler(value = Exception.class)
        public String handleException(Exception e, Model model) {
            // その他のエラーをハンドル
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }
    }
    
}