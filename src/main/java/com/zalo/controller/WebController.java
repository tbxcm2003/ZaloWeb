package com.zalo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login/qr")
    public String qrLoginPage() {
        return "qr-login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

}