/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.visao;

import com.br.moveme.controle.jpa.MotoristaJpaController;
import com.br.moveme.modelo.Motorista;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author omupa
 */
public class TesteInserirMotorista {
    public static void main(String[] args) {
        Motorista motorista = new Motorista();
        motorista.setNome("Don Juan");
        motorista.setCnh("54BG8");
        motorista.setEmail("motorista@email.com");
        motorista.setFone("+5564987545689");
        motorista.setStatus(1);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        MotoristaJpaController motoristaJpaController = new MotoristaJpaController(emf);
        
        try {
            motoristaJpaController.create(motorista);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }
}
