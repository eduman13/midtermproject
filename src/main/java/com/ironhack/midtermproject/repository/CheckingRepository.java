package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Integer> {

    @Query("select u from Checking u where u.id = :id")
    Checking findByIdWithoutOptional(@Param("id") Integer id);

}
