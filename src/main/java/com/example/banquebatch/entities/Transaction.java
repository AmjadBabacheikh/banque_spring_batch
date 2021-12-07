package com.example.banquebatch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction  implements Serializable {
    @Id
    private int idTransaction;
    private double montant;
    private Date dateTransaction;
    private Date dateDebit;

    @OneToOne(cascade = {CascadeType.ALL})
    private Compte compte;

}
