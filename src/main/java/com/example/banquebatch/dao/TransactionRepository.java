package com.example.banquebatch.dao;

import com.example.banquebatch.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Integer> {

}
