package com.ironhack.midtermproject.service.security;

import com.ironhack.midtermproject.model.security.User;
import com.ironhack.midtermproject.repository.security.UserRepository;
import com.ironhack.midtermproject.security.CustomSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    User user = userRepo.findByUsername(username);
    
    if (user == null)
      throw new UsernameNotFoundException("Invalid username/password combination.");
    
    return new CustomSecurityUser(user);
  }
}
