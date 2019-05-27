/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.modelos.Administrador;
import com.google.gson.Gson;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lucas
 */
@Path("/cadastroadministrador")
public class CadastroAdministrador {
    
    @POST
    @Path("/inseriradministrador")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroAdministrador(String dadosAdministrador) {
        Gson gson = new Gson();
        
        //PassageiroDAO dao = new PassageiroDAO();
        Administrador administrador = new Gson().fromJson(dadosAdministrador, Administrador.class);
        try {
            //parte de inserir com jpa aqui para quem for fazer
            //dao.inseriPassageiro(motorista);
        } catch (Exception ex) {
            System.out.println("Erro ao inserir administrador");
        }
    }
}
