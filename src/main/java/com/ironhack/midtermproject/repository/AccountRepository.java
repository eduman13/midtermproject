package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Checking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository<T extends Checking> extends JpaRepository<T, Integer> {
}
