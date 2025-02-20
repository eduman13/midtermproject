package com.ironhack.midtermproject.repository.security;

import com.ironhack.midtermproject.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findByUser_Id(Integer id);

}
