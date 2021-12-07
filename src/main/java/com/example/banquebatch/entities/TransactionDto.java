package com.example.banquebatch.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private int idTransaction;
    private int idCompte;
    private double montant;
    private Date dateTransaction;
}
