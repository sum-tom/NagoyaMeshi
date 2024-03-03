package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.event.SignupEventPublisher;
import com.NagoyaMeshi.form.SignupForm;
import com.NagoyaMeshi.repository.UserRepository;
import com.NagoyaMeshi.service.AdminUserService;
import com.NagoyaMeshi.service.VerificationTokenService;

@Controller
@RequestMapping("/admin/member")
public class AdminController {

	private final AdminUserService adminUserService;    
	 private final SignupEventPublisher signupEventPublisher;
	 private final VerificationTokenService verificationTokenService;
	
	private final UserRepository userRepository;
	public AdminController(UserRepository userRepository,
							AdminUserService adminUserService,
							SignupEventPublisher signupEventPublisher,
							VerificationTokenService verificationTokenService) {        
        
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.adminUserService = adminUserService;
        this.userRepository = userRepository;
        
    }
	
	
	@GetMapping
	 public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		 Page<User> userPage;
		 
		 if (keyword != null && !keyword.isEmpty()) {
			 userPage = userRepository.findByNameLike("%" + keyword + "%", pageable);                
        } else {
        	userPage = userRepository.findAll(pageable);
        }  
		 
		 model.addAttribute("userPage", userPage);   
		 model.addAttribute("keyword", keyword);
	        return "/admin/member/admin-member-list";
	    } 
	
	@GetMapping("/signup")
    public String signup(Model model) {      
		
        model.addAttribute("signupForm", new SignupForm());
        
        return "/admin/member/signup";
    }    
	 
	@PostMapping("/signup")
	 public String Create(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
	     
		if (adminUserService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }    
        
        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!adminUserService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }        
		
		if (bindingResult.hasErrors()) {
	         return "/admin/member/signup";
	 }
	 
		adminUserService.create(signupForm);
         redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");
	 
	 return "redirect:/admin/member";
    }    
}
