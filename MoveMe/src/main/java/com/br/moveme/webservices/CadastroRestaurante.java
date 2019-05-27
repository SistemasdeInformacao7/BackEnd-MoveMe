/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.modelos.Restaurante;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lucas
 */
@Path("/cadastrorestaurante")
public class CadastroRestaurante {
    
    @POST
    @Path("/inserirrestaurante")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroAdministrador(String dadosRestaurante) {
        Gson gson = new Gson();
        
        //PassageiroDAO dao = new PassageiroDAO();
        Restaurante restaurante = new Gson().fromJson(dadosRestaurante, Restaurante.class);
        try {
            //parte de inserir com jpa aqui para quem for fazer
            //dao.inseriPassageiro(motorista);
        } catch (Exception ex) {
            System.out.println("Erro ao inserir restaurante");
        }
    }
}
