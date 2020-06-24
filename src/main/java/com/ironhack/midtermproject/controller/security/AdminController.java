package com.ironhack.midtermproject.controller.security;

import com.ironhack.midtermproject.model.security.Role;
import com.ironhack.midtermproject.model.security.User;
import com.ironhack.midtermproject.service.security.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return adminService.getAllUserAccounts();
    }

    @GetMapping("/admin/roles")
    public List<Role> getAllRoles() {
        return adminService.getAllRoles();
    }
}
