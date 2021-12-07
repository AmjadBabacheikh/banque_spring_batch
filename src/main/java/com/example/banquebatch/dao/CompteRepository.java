package com.example.banquebatch.dao;


import com.example.banquebatch.entities.Compte;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends CrudRepository<Compte,Integer> {

}
