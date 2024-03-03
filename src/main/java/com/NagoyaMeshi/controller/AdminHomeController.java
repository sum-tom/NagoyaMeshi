package com.NagoyaMeshi.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    
	//管理者TOP画面
	@GetMapping("/admin")
    public String index() {
        return "/admin/admin-top";
    }   
}

