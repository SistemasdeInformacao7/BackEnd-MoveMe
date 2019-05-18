/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;


import com.br.moveme.modelos.Passageiro;
import com.br.moveme.dao.PassageiroDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Lucas
 */
@Path("/cadastropassageiro")
public class CadastroPassageiro {
    
    @POST
    @Path("/inserirpassageiro")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroPassageiro(String dadosPassageiro) {
        Gson gson = new Gson();
        
        PassageiroDAO dao = new PassageiroDAO();
        Passageiro passageiro = new Gson().fromJson(dadosPassageiro, Passageiro.class);
        try {
            dao.inseriPassageiro(passageiro);
        } catch (Exception ex) {
            System.out.println("Erro ao inserir passageiro");
        }
    }
}