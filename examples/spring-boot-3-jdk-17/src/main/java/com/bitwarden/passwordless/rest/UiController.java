package com.bitwarden.passwordless.rest;

import com.bitwarden.passwordless.config.PasswordlessApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    private final PasswordlessApiConfig passwordlessApiConfig;

    @Autowired
    public UiController(PasswordlessApiConfig passwordlessApiConfig) {
        this.passwordlessApiConfig = passwordlessApiConfig;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("passwordlessApiConfig", passwordlessApiConfig);
        return "index.html";
    }
}
