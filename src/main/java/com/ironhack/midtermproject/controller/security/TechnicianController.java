package com.ironhack.midtermproject.controller.security;

import com.ironhack.midtermproject.model.security.Role;
import com.ironhack.midtermproject.model.security.User;
import com.ironhack.midtermproject.service.security.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TechnicianController {

  @Autowired
  private RoleService roleService;

  @GetMapping("/roles")
  public List<Role> getDashboard (@AuthenticationPrincipal User user) {
    
    return roleService.findByUserId(user.getId());
  }

}
