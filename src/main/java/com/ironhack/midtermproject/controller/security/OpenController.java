package com.ironhack.midtermproject.controller.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenController {

    @GetMapping("/")
    public String greeting() {
        return "Welcome to the site!";
    }
}
