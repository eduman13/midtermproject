package com.ironhack.midtermproject.repository.fraud;

import com.ironhack.midtermproject.model.fraud.AmountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountTransactionRepository extends JpaRepository<AmountTransaction, Integer> {
}
