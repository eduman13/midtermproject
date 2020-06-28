package com.ironhack.midtermproject.controller.security;

import com.ironhack.midtermproject.model.security.User;
import com.ironhack.midtermproject.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoggedInController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/loggedin")
    public String loggin(@AuthenticationPrincipal User user) {
        return "Hello, " + user.getUsername();
    }

    @PostMapping("/login")
    @CrossOrigin(origins="*", allowCredentials="true")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails login(@AuthenticationPrincipal User user) {
        return userDetailsService.loadUserByUsername(user.getUsername());
    }
}
