package com.NagoyaMeshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.entity.VerificationToken;
import com.NagoyaMeshi.form.UserEditForm;
import com.NagoyaMeshi.repository.UserRepository;
import com.NagoyaMeshi.repository.VerificationTokenRepository;
import com.NagoyaMeshi.security.UserDetailsImpl;
import com.NagoyaMeshi.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/member")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService; 
    private final VerificationTokenRepository verificationTokenRepository;

    public UserController(UserRepository userRepository, UserService userService, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
    }
    
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {         
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        
        model.addAttribute("user", user);
        
        return "user/top";
    }
    
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(), user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "user/member/edit";
    }    
    
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        
        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/user/member";
    }    
    
    @GetMapping("/withdrawal")
    public String withdrawl() {
        return "/user/member/withdrawal";
    }
    
    @GetMapping("/withdrawal/delete")
    public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    											RedirectAttributes redirectAttributes,
    											HttpServletResponse response) {        
    	
    	
    	User user = userDetailsImpl.getUser();
    	
    	// 関連するVerificationTokenを削除
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
        if (verificationToken != null) {
            verificationTokenRepository.delete(verificationToken);
        }
        
        userRepository.delete(user);
        
     
        redirectAttributes.addFlashAttribute("successMessage", "退会しました。");
        return "redirect:/login";
    }

    
}