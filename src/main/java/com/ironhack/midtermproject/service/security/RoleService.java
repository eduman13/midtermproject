package com.ironhack.midtermproject.service.security;

import com.ironhack.midtermproject.model.security.Role;
import com.ironhack.midtermproject.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepo;

    public List<Role> findByUserId(Integer id) {
        return roleRepo.findByUser_Id(id);
    }
}
