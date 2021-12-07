package com.example.banquebatch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte implements Serializable {
    @Id
    private int idCompte;
    private double solde;

    public void debiter(double montant){
        this.solde=solde-montant;
    }

}
