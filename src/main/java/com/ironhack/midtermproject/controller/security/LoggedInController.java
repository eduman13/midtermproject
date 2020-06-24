package com.ironhack.midtermproject.controller.security;

import com.ironhack.midtermproject.model.security.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggedInController {

    @GetMapping("/loggedin")
    public String sayHello(@AuthenticationPrincipal User user) {
        return "Hello, " + user.getUsername();
    }
}
