package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Integer> {
}
