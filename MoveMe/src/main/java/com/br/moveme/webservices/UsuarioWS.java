/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.UsuarioJpaController;
import com.br.moveme.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lucas/Otavio
 */
@Path("/usuario")
public class UsuarioWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public void inserir(String dadosUsuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgcoelectaPU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Gson gson = new Gson();
        Usuario usuario = new Gson().fromJson(dadosUsuario, Usuario.class);

        try {
            usuarioJpaController.create(usuario);
        } catch (Exception ex) {
            System.out.println("UsuarioWS - erro ao inserir: " + ex);
        }
    }

    @GET
    @Path("/getall")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Usuario> getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgcoelectaPU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        List<Usuario> lista = null;

        try {
            lista = usuarioJpaController.findUsuarioEntities();
        } catch (Exception e) {
            System.out.println("UsuarioWS - listar todos: " + e);
        }
        return lista;
    }

    @GET
    @Path("/getid")
    @Consumes(MediaType.APPLICATION_JSON)
    public Usuario get(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgcoelectaPU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Usuario usuario = null;

        try {
            usuario = usuarioJpaController.findUsuario(id);
        } catch (Exception e) {
            System.out.println("UsuarioWS - listar todos: " + e);
        }
        return usuario;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editar(String dadosUsuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgcoelectaPU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);
        EntityManager em = emf.createEntityManager();

        Usuario usuario = new Gson().fromJson(dadosUsuario, Usuario.class);
        Usuario usuario1 = usuarioJpaController.findUsuario(usuario.getCpf());

        try {
            em.getTransaction().begin();
            usuario1.setEmail(usuario.getEmail());
            usuario1.setNome(usuario.getNome());
            usuario1.setSenha(usuario1.getSenha());
            usuario1.setTelefone(usuario.getTelefone());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("UsuarioWS - erro ao editar: " + e);
        }
    }

    @DELETE
    @Path("/remover")
    @Consumes(MediaType.APPLICATION_JSON)
    public void remover(int cpf) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sgcoelectaPU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        try {
            usuarioJpaController.destroy(cpf);
        } catch (Exception e) {
            System.out.println("UsuarioWS - erro ao remover: " + e);
        }
    }
}
