/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.dao;

import com.br.moveme.config.BDConfig;
import com.br.moveme.modelos.Passageiro;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Lucas
 */
public class PassageiroDAO {
    public void inseriPassageiro(Passageiro passageiro) throws Exception {
        try (Connection conexao = BDConfig.getConnection()) {
            String sql = "INSERT INTO passageiro (nome, sobrenome, cpf, telefone, email, senha) VALUES (?,?,?,?,?,?)";
            
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, passageiro.getNome());
                pstmt.setString(2, passageiro.getSobrenome());
                pstmt.setString(3, passageiro.getCpf());
                pstmt.setString(4, passageiro.getTelefone());
                pstmt.setString(5, passageiro.getEmail());
                pstmt.setString(6, passageiro.getSenha());
                
                pstmt.execute();
                
                System.out.println("Passageiro inserido com sucesso!");
                conexao.close();
            }
        }
    }
}
