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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lucas/Otavio
 */
@Path("/passageiro")
public class PassageiroWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public void inserir(String dadosUsuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Gson gson = new Gson();
        Usuario usuario = new Gson().fromJson(dadosUsuario, Usuario.class);

        try {
            usuarioJpaController.create(usuario);
        } catch (Exception ex) {
            System.out.println("PassageiroWS - erro ao inserir: " + ex);
        }
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Gson gson = new Gson();
        List<Usuario> lista = null;
        String saida = null;
        List<Usuario> listaSimples = null;
        try {
            lista = usuarioJpaController.findUsuarioEntities();
            
            for(Usuario u : lista){
                System.out.println("Usuario lista: " + u.toString());
            }
            
            saida = new Gson().toJson(lista);
            
            System.out.println("Saida JSON: " + saida);
        } catch (Exception e) {
            System.out.println("PassageiroWS - listar todos: " + e);
        }
        return saida;
    }

    
    @GET
    @Path("/{cpf}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("cpf") int cpf)  {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Usuario usuario = null;
        Gson gson = new Gson();
        String saida = null;

        try {
            usuario = usuarioJpaController.findUsuario(cpf);
            saida = new Gson().toJson(usuario);
        } catch (Exception e) {
            System.out.println("PassageiroWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosUsuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);
        EntityManager em = emf.createEntityManager();

        Usuario usuario = new Gson().fromJson(dadosUsuario, Usuario.class);
        Usuario usuario1 = usuarioJpaController.findUsuario(usuario.getCpf());
        Gson gson = new Gson();
        
        try {
            em.getTransaction().begin();
            usuario1.setEmail(usuario.getEmail());
            usuario1.setNome(usuario.getNome());
            usuario1.setSenha(usuario1.getSenha());
            usuario1.setTelefone(usuario.getTelefone());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("PassageiroWS - erro ao editar: " + e);
        }
        
        Usuario verificaUsuario = usuarioJpaController.findUsuario(usuario1.getCpf());
        
        return new Gson().toJson(verificaUsuario);
    }

    @DELETE
    @Path("/remover")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(int cpf) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioJpaController usuarioJpaController = new UsuarioJpaController(emf);

        Gson gson = new Gson();
        String saida = null;

        try {
            usuarioJpaController.destroy(cpf);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("PassageiroWS - erro ao remover: " + e);
        }
        
        return new Gson().toJson(usuarioJpaController.findUsuario(cpf));
    }
}
