package com.ironhack.midtermproject.repository.fraud;

import com.ironhack.midtermproject.model.fraud.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByAccountId(Integer id);

}
