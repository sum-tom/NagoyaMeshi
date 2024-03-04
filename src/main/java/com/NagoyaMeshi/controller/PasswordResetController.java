package com.NagoyaMeshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NagoyaMeshi.service.EmailService;

@Controller
public class PasswordResetController{
    private EmailService emailService;
    
    public PasswordResetController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    
    @GetMapping("/password-reset/request")
    public String request() {
        return "/password-reset/request";
    }   

    @PostMapping("/password-reset/request")
    public String sendEmail(@RequestParam String email) {
        String link = "http://localhost:8080/password-reset/reset";
        String message = "リンクをクリックしてください: " + link;
        emailService.sendPasswordResetEmail(email, "リンク付きメール", message);
        return "redirect:/password-reset/request?success=true";
    }
}