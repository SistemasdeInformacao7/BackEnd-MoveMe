/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.RestauranteJpaController;
import com.br.moveme.modelo.Restaurante;
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
@Path("/restaurante")
public class RestauranteWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosRestaurante) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        RestauranteJpaController restauranteJpaController = new RestauranteJpaController(emf);

        Restaurante restaurante = new Gson().fromJson(dadosRestaurante, Restaurante.class);

        try {
            restauranteJpaController.create(restaurante);
        } catch (Exception ex) {
            System.out.println("RestauranteWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(restauranteJpaController.findRestaurante(restaurante.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        RestauranteJpaController restauranteJpaController = new RestauranteJpaController(emf);

        List<Restaurante> lista = null;
        String saida = null;

        try {
            lista = restauranteJpaController.findRestauranteEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("RestauranteWS - listar todos: " + e);
        }
        return saida;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        RestauranteJpaController restauranteJpaController = new RestauranteJpaController(emf);

        Restaurante restaurante = null;
        String saida = null;

        try {
            restaurante = restauranteJpaController.findRestaurante(id);
            saida = new Gson().toJson(restaurante);
        } catch (Exception e) {
            System.out.println("RestauranteWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosAdministrador) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        Restaurante restaurante = new Gson().fromJson(dadosAdministrador, Restaurante.class);
        Restaurante restaurante1 = em.find(Restaurante.class, restaurante.getId());

        try {
            em.getTransaction().begin();
            restaurante1.setCidade(restaurante.getCidade());
            restaurante1.setCozinha(restaurante.getCozinha());
            restaurante1.setFaixaPreco(restaurante.getFaixaPreco());
            restaurante1.setMoeda(restaurante.getMoeda());
            restaurante1.setServicoAgora(restaurante.getServicoAgora());
            restaurante1.setServicoOnline(restaurante.getServicoOnline());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("RestauranteWS - erro ao editar: " + e);
        }

        Restaurante verificaRestaurante = em.find(Restaurante.class, restaurante.getId());

        return new Gson().toJson(verificaRestaurante);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        RestauranteJpaController restauranteJpaController = new RestauranteJpaController(emf);

        String saida = null;

        try {
            restauranteJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("RestauranteWS - erro ao remover: " + e);
        }

        return new Gson().toJson(restauranteJpaController.findRestaurante(id));
    }
}
