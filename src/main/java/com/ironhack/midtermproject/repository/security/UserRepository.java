package com.ironhack.midtermproject.repository.security;

import com.ironhack.midtermproject.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

}
