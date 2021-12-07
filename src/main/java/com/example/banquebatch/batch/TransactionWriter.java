package com.example.banquebatch.batch;

import com.example.banquebatch.dao.CompteRepository;
import com.example.banquebatch.dao.TransactionRepository;
import com.example.banquebatch.entities.Transaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionWriter implements ItemWriter<Transaction> {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CompteRepository compteRepository;


    @Override
    @Transactional
    public void write(List<? extends Transaction> transactions) throws Exception {
        for (Transaction transaction : transactions) {
            compteRepository.save(transaction.getCompte());
            System.out.println("compte n " + transaction.getCompte().getIdCompte() + " de solde " + transaction.getCompte().getSolde() + " avant debit");
            transaction.getCompte().debiter(transaction.getMontant());
            transactionRepository.save(transaction);
            System.out.println("compte n " + transaction.getCompte().getIdCompte() + " de solde " + transaction.getCompte().getSolde() + " after debit");
        }
    }
}
