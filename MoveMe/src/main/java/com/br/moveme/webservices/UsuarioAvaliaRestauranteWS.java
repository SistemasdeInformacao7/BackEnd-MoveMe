/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.UsuarioAvaliaRestauranteJpaController;
import com.br.moveme.modelo.Restaurante;
import com.br.moveme.modelo.Usuario;
import com.br.moveme.modelo.UsuarioAvaliaRestaurante;
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
@Path("/avaliarestaurante")
public class UsuarioAvaliaRestauranteWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioAvaliaRestauranteJpaController usuarioAvaliaRestauranteJpaController = new UsuarioAvaliaRestauranteJpaController(emf);

        UsuarioAvaliaRestaurante usuarioAvaliaRestaurante = new Gson().fromJson(dadosViagem, UsuarioAvaliaRestaurante.class);

        try {
            usuarioAvaliaRestauranteJpaController.create(usuarioAvaliaRestaurante);
        } catch (Exception ex) {
            System.out.println("UsuarioAvaliaRestauranteWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(usuarioAvaliaRestauranteJpaController.findUsuarioAvaliaRestaurante(usuarioAvaliaRestaurante.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioAvaliaRestauranteJpaController usuarioAvaliaRestauranteJpaController = new UsuarioAvaliaRestauranteJpaController(emf);

        List<UsuarioAvaliaRestaurante> lista = null;
        String saida = null;

        try {
            lista = usuarioAvaliaRestauranteJpaController.findUsuarioAvaliaRestauranteEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("UsuarioAvaliaRestauranteWS - listar todos: " + e);
        }
        return saida;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioAvaliaRestauranteJpaController usuarioAvaliaRestauranteJpaController = new UsuarioAvaliaRestauranteJpaController(emf);

        UsuarioAvaliaRestaurante usuarioAvaliaRestaurante = null;
        String saida = null;

        try {
            usuarioAvaliaRestaurante = usuarioAvaliaRestauranteJpaController.findUsuarioAvaliaRestaurante(id);
            saida = new Gson().toJson(usuarioAvaliaRestaurante);
        } catch (Exception e) {
            System.out.println("UsuarioAvaliaRestauranteWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosAvaliacao) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        UsuarioAvaliaRestaurante usuarioAvaliaRestaurante = new Gson().fromJson(dadosAvaliacao, UsuarioAvaliaRestaurante.class);
        UsuarioAvaliaRestaurante usuarioAvaliaRestaurante1 = em.find(UsuarioAvaliaRestaurante.class, usuarioAvaliaRestaurante.getId());
        Usuario usuario = em.find(Usuario.class, usuarioAvaliaRestaurante.getCpfusuario().getCpf());
        Restaurante restaurante = em.find(Restaurante.class, usuarioAvaliaRestaurante.getIdrestaurante().getId());

        try {
            em.getTransaction().begin();
            usuarioAvaliaRestaurante1.setAvaliacao(usuarioAvaliaRestaurante.getAvaliacao());
            usuarioAvaliaRestaurante1.setDia(usuarioAvaliaRestaurante.getDia());
            usuarioAvaliaRestaurante1.setCpfusuario(usuario);
            usuarioAvaliaRestaurante1.setIdrestaurante(restaurante);
            em.getTransaction().commit();
        } catch (Exception e) {

            System.out.println("UsuarioAvaliaRestauranteWS - erro ao editar: " + e);
        }

        UsuarioAvaliaRestaurante verificaAvaliacao = em.find(UsuarioAvaliaRestaurante.class, usuarioAvaliaRestaurante.getId());

        return new Gson().toJson(verificaAvaliacao);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioAvaliaRestauranteJpaController usuarioAvaliaRestauranteJpaController = new UsuarioAvaliaRestauranteJpaController(emf);

        String saida = null;

        try {
            usuarioAvaliaRestauranteJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("UsuarioAvaliaRestauranteWS - erro ao remover: " + e);
        }

        return new Gson().toJson(usuarioAvaliaRestauranteJpaController.findUsuarioAvaliaRestaurante(id));
    }
}
