/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.modelo.Motorista;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
/**
 *
 * @author Lucas
 */
@Path("/cadastromotoritsta")
public class CadastroMotorista {
    
    @POST
    @Path("/inserirmotoritsta")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroMotorista(String dadosMotorista) {
        Gson gson = new Gson();
        
        //PassageiroDAO dao = new PassageiroDAO();
        Motorista motorista = new Gson().fromJson(dadosMotorista, Motorista.class);
        try {
            //parte de inserir com jpa aqui para quem for fazer
            //dao.inseriPassageiro(motorista);
        } catch (Exception ex) {
            System.out.println("Erro ao inserir motorista");
        }
    }
}