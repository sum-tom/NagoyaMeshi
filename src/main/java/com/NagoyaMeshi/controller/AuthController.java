package com.NagoyaMeshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.NagoyaMeshi.form.SignupForm;

@Controller
public class AuthController {
	 @GetMapping("/login")
     public String login() {        
         return "auth/login";
     }
	 
	 @GetMapping("/signup")
     public String signup(Model model) {        
         model.addAttribute("signupForm", new SignupForm());
         return "auth/signup";
     }    
	 
}
