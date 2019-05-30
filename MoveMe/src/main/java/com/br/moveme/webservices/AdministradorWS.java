/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.AdministradorJpaController;
import com.br.moveme.modelo.Administrador;
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
@Path("/administrador")
public class AdministradorWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosAdministrador) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        AdministradorJpaController administradorJpaController = new AdministradorJpaController(emf);

        Administrador administrador = new Gson().fromJson(dadosAdministrador, Administrador.class);

        try {
            administradorJpaController.create(administrador);
        } catch (Exception ex) {
            System.out.println("AdministradorWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(administradorJpaController.findAdministrador(administrador.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        AdministradorJpaController administradorJpaController = new AdministradorJpaController(emf);

        List<Administrador> lista = null;
        String saida = null;
        
        try {
            lista = administradorJpaController.findAdministradorEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("AdministradorWS - listar todos: " + e);
        }
        return saida;
    }

    
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id)  {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        AdministradorJpaController administradorJpaController = new AdministradorJpaController(emf);

        Administrador motorista = null;
        String saida = null;

        try {
            motorista = administradorJpaController.findAdministrador(id);
            saida = new Gson().toJson(motorista);
        } catch (Exception e) {
            System.out.println("AdministradorWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosAdministrador) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        Administrador administrador = new Gson().fromJson(dadosAdministrador, Administrador.class);
        Administrador administrador1 = em.find(Administrador.class, administrador.getId());
        
        try {
            em.getTransaction().begin();
            administrador1.setNome(administrador.getNome());
            administrador1.setEmail(administrador.getEmail());
            administrador1.setFoto(administrador.getFoto());
            administrador1.setSenha(administrador.getSenha());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("AdministradorWS - erro ao editar: " + e);
        }
        
        Administrador verificaAdministrador = em.find(Administrador.class, administrador.getId());
        
        return new Gson().toJson(verificaAdministrador);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        AdministradorJpaController administradorJpaController = new AdministradorJpaController(emf);

        String saida = null;

        try {
            administradorJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("AdministradorWS - erro ao remover: " + e);
        }
        
        return new Gson().toJson(administradorJpaController.findAdministrador(id));
    }
}
