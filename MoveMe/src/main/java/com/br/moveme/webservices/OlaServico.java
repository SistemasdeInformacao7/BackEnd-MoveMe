/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

/**
 *
 * @author Lucas
 */
import com.br.moveme.dao.PassageiroDAO;
import com.br.moveme.modelos.Passageiro;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/servicopassageiro")
public class OlaServico {

    @POST
    @Path("/cadastro-passageiro")
    @Consumes(MediaType.APPLICATION_JSON)
    public void cadastroPassageiro(String dadosPassageiro) {
        Gson gson = new Gson();
        PassageiroDAO dao = new PassageiroDAO();
        
        
        Passageiro passageiro = new Gson().fromJson(dadosPassageiro, Passageiro.class);
        try {
            dao.inseriPassageiro(passageiro);
        } catch (Exception ex) {
            Logger.getLogger(OlaServico.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(passageiro.toString());
    }
}
