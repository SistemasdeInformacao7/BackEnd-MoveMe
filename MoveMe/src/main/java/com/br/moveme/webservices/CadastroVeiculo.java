/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.modelos.Administrador;
import com.br.moveme.modelos.Veiculo;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lucas
 */
@Path("/cadastroveiculo")
public class CadastroVeiculo {
    
    @POST
    @Path("/inserirveiculo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroVeiculo(String dadosVeiculo) {
        Gson gson = new Gson();
        
        //PassageiroDAO dao = new PassageiroDAO();
        Veiculo administrador = new Gson().fromJson(dadosVeiculo, Veiculo.class);
        try {
            //parte de inserir com jpa aqui para quem for fazer
            //dao.inseriPassageiro(motorista);
        } catch (Exception ex) {
            System.out.println("Erro ao inserir veiculo");
        }
    }
}
