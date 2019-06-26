/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.MotoristaJpaController;
import com.br.moveme.modelo.Motorista;
import com.google.gson.Gson;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lucas/Otavio
 */
@Path("/motorista")
public class MotoristaWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosMotorista) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        MotoristaJpaController motoristaJpaController = new MotoristaJpaController(emf);

        Motorista motorista = new Gson().fromJson(dadosMotorista, Motorista.class);

        try {
            motoristaJpaController.create(motorista);
        } catch (Exception ex) {
            System.out.println("MotoristaWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(motoristaJpaController.findMotorista(motorista.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        MotoristaJpaController motoristaJpaController = new MotoristaJpaController(emf);

        List<Motorista> lista = null;
        String saida = null;
        
        try {
            lista = motoristaJpaController.findMotoristaEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("MotoristaWS - listar todos: " + e);
        }
        return saida;
    }
    
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id)  {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        MotoristaJpaController motoristaJpaController = new MotoristaJpaController(emf);

        Motorista motorista = null;
        String saida = null;

        try {
            motorista = motoristaJpaController.findMotorista(id);
            saida = new Gson().toJson(motorista);
        } catch (Exception e) {
            System.out.println("MotoristaWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosMotorista) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        Motorista motorista = new Gson().fromJson(dadosMotorista, Motorista.class);
        Motorista motorista1 = em.find(Motorista.class, motorista.getId());
        
        try {
            em.getTransaction().begin();
            motorista1.setNome(motorista.getNome());
            motorista1.setCnh(motorista.getCnh());
            motorista1.setEmail(motorista.getEmail());
            motorista1.setFone(motorista.getFone());
            motorista1.setStatus(motorista.getStatus());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("MotoristaWS - erro ao editar: " + e);
        }
        
        Motorista verificaMotorista = em.find(Motorista.class, motorista.getId());
        
        return new Gson().toJson(verificaMotorista);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        MotoristaJpaController motoristaJpaController = new MotoristaJpaController(emf);

        String saida = null;

        try {
            motoristaJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("MotoristaWS - erro ao remover: " + e);
        }
        
        return new Gson().toJson(motoristaJpaController.findMotorista(id));
    }
}
