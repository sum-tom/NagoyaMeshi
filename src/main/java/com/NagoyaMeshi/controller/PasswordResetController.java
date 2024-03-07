package com.NagoyaMeshi.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showResetForm(@RequestParam("token") String token, Model model) {
        String result = tokenService.validatePasswordResetToken(token);
        if (!result.equals("valid")) {
            model.addAttribute("error", "トークンが期限切れです。");
            return "password-reset/error"; // エラーを表示するビューを指定
        }
        model.addAttribute("token", token);
        return "password-reset/reset";
    }

    @PostMapping("/reset")//パスワードのリセット
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
        	model.addAttribute("error", "Passwords do not match.");
            return "redirect:/password-reset/reset?error=true&token=" + token;
        }

        boolean resetResult = userService.resetPassword(token, password);
        if (!resetResult) {
        	model.addAttribute("error", "パスワードのリセットに失敗しました");
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

