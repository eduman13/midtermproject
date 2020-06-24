package com.ironhack.midtermproject.service.security;

import com.ironhack.midtermproject.model.security.Role;
import com.ironhack.midtermproject.model.security.User;
import com.ironhack.midtermproject.repository.security.RoleRepository;
import com.ironhack.midtermproject.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private RoleRepository roleRepo;
  
  @Secured({"ROLE_ADMIN"})
  public List<User> getAllUserAccounts () {
    return userRepo.findAll();
  }

  @Secured({"ROLE_ADMIN"})
  public List<Role> getAllRoles () {
    return roleRepo.findAll();
  }
}
