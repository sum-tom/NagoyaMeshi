package com.NagoyaMeshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/credit")
public class UserCreditController {
	@GetMapping("/registration")
    public String registration() {
        return "/user/credit/credit-registration";
    }   
	
	
}
