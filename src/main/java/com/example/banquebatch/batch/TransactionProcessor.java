package com.example.banquebatch.batch;

import com.example.banquebatch.entities.Compte;
import com.example.banquebatch.entities.Transaction;
import com.example.banquebatch.entities.TransactionDto;
import com.example.banquebatch.util.Randomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
public class TransactionProcessor implements ItemProcessor<TransactionDto, Transaction> {

    @Override
    public Transaction process(final TransactionDto transactionDto) throws Exception {
        Transaction transformedTransaction = null;
        try {
            int idTransaction = transactionDto.getIdTransaction();
            double montant = transactionDto.getMontant();
            Date dateTransaction = transactionDto.getDateTransaction();
            int idCompte = transactionDto.getIdCompte();
            Compte transformedCompte = Compte.builder().idCompte(idCompte).solde(Randomizer.generate(1000.0,10000.0)).build();
            transformedTransaction = new Transaction(idTransaction, montant, dateTransaction, new Date(), transformedCompte);
            log.info("Converting (" + transactionDto + ") into (" + transformedTransaction + ")");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return transformedTransaction;
    }
}
