package com.NagoyaMeshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.service.EmailService;
import com.NagoyaMeshi.service.PasswordResetTokenService;
import com.NagoyaMeshi.service.UserService;

@Controller
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final EmailService emailService;
    private final UserService userService;
    private final PasswordResetTokenService tokenService;

    public PasswordResetController(EmailService emailService, UserService userService, PasswordResetTokenService tokenService) {
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/reset")//パスワードリセットフォームの表示
    public String showResetForm(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        String result = tokenService.validatePasswordResetToken(token);
        if (!result.equals("valid")) {
        	redirectAttributes.addFlashAttribute("error", "トークンが期限切れ");
            return "redirect:/password-reset/request?error=true";
        }
        redirectAttributes.addFlashAttribute("token", token);
        return "password-reset/reset";
    }

    @PostMapping("/reset")//パスワードのリセット
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
        	redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/password-reset/reset?error=true&token=" + token;
        }

        boolean resetResult = userService.resetPassword(token, password);
        if (!resetResult) {
        	redirectAttributes.addFlashAttribute("error", "パスワードのリセットに失敗しました");
            return "redirect:/password-reset/reset?error=true&token=" + token;
        }

        return "redirect:/login?resetSuccess=true";
    }
    
    
    
    
    @GetMapping("/request")//パスワードリセットリクエストフォームの表示
    public String showPasswordResetRequestForm() {
        return "password-reset/request"; // パスワードリセットリクエストのフォームへのパス
    }

    @PostMapping("/request")//パスワードリセットリクエストの処理
    public String processResetPasswordRequest(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        // パスワードリセット処理（メール送信など）
        boolean result = userService.createPasswordResetTokenForUserAndSendEmail(email);
        if (result) {
        	redirectAttributes.addFlashAttribute("successMessage", "パスワードリセット用のリンクをメールで送信しました。");
            return "redirect:/login"; // 成功メッセージを表示するページへリダイレクト
        } else {
        	redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスが見つかりません。");
            return "password-reset/request"; // エラーメッセージを表示し、再度フォームを表示
        }
    }
    
}

//@Controller
//public class PasswordResetController {
//
//    private final EmailService emailService;
//
//    public PasswordResetController(EmailService emailService) {
//        this.emailService = emailService;
//        
//    }
//
//   
//    @GetMapping("/password-reset/request")
//    public String request() {
//        return "/password-reset/request";
//    }   
//
//    @PostMapping("/password-reset/request")
//    public String sendEmail(@RequestParam String email) {
//        String link = "http://localhost:8080/password-reset/reset";
//        String message = "リンクをクリックしてください: " + link;
//        emailService.sendPasswordResetEmail(email, "リンク付きメール", message);
//        return "redirect:/password-reset/request?success=true";
//    }
//}