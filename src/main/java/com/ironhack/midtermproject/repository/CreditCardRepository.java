package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    @Query("select c from CreditCard c where c.id = :id")
    CreditCard findByIdWithoutOptional(@Param("id") Integer id);

}
