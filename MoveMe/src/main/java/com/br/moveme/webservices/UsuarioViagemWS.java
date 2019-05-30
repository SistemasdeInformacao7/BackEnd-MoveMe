/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.UsuarioViagemJpaController;
import com.br.moveme.controle.jpa.ViagemJpaController;
import com.br.moveme.modelo.Motorista;
import com.br.moveme.modelo.Usuario;
import com.br.moveme.modelo.UsuarioViagem;
import com.br.moveme.modelo.UsuarioViagemPK;
import com.br.moveme.modelo.Veiculo;
import com.br.moveme.modelo.Viagem;
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
@Path("/usuarioviagem")
public class UsuarioViagemWS {
    
    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosUsuarioViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioViagemJpaController usuarioViagemJpaController = new UsuarioViagemJpaController(emf);
        
        UsuarioViagem usuarioViagem = new Gson().fromJson(dadosUsuarioViagem, UsuarioViagem.class);
        
        try {
            usuarioViagemJpaController.create(usuarioViagem);
        } catch (Exception ex) {
            System.out.println("UsuarioViagemWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(usuarioViagemJpaController.findUsuarioViagem(usuarioViagem.getUsuarioViagemPK()));
    }
    
    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioViagemJpaController usuarioViagemJpaController = new UsuarioViagemJpaController(emf);
        
        List<UsuarioViagem> lista = null;
        String saida = null;
        
        try {
            lista = usuarioViagemJpaController.findUsuarioViagemEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("UsuarioViagemWS - listar todos: " + e);
        }
        return saida;
    }
    
    @GET
    @Path("/getusuario")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(String usuarioViagemPK) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioViagemJpaController usuarioViagemJpaController = new UsuarioViagemJpaController(emf);
        
        UsuarioViagemPK usuarioViagemPK1 = new Gson().fromJson(usuarioViagemPK, UsuarioViagemPK.class);
        UsuarioViagem usuarioViagem = null;
        String saida = null;
        
        try {
            usuarioViagem = usuarioViagemJpaController.findUsuarioViagem(usuarioViagemPK1);
            saida = new Gson().toJson(usuarioViagem);
        } catch (Exception e) {
            System.out.println("UsuarioViagemWS - listar todos: " + e);
        }
        return saida;
    }
    
    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosUsuarioViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();
        
        UsuarioViagem usuarioViagem = new Gson().fromJson(dadosUsuarioViagem, UsuarioViagem.class);
        UsuarioViagem usuarioViagem1 = em.find(UsuarioViagem.class, usuarioViagem.getUsuarioViagemPK());
        Usuario usuario = em.find(Usuario.class, usuarioViagem.getUsuario());
        UsuarioViagemPK usuarioViagemPK = em.find(UsuarioViagemPK.class, usuarioViagem.getUsuarioViagemPK());
        Viagem viagem = em.find(Viagem.class, usuarioViagem.getViagem());
        
        try {
            em.getTransaction().begin();
            usuarioViagem1.setAvaliacao(usuarioViagem.getAvaliacao());
            usuarioViagem1.setPreco(usuarioViagem.getPreco());
            usuarioViagem1.setUsuario(usuario);
            usuarioViagem1.setUsuarioViagemPK(usuarioViagemPK);
            usuarioViagem1.setViagem(viagem);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            System.out.println("UsuarioViagemWS - erro ao editar: " + e);
        }
        
        UsuarioViagem verificaUsuarioViagem = em.find(UsuarioViagem.class, usuarioViagem.getUsuarioViagemPK());
        
        return new Gson().toJson(verificaUsuarioViagem);
    }
    
    @DELETE
    @Path("/removerusuarioviagem")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(String dadosUsuarioViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        UsuarioViagemJpaController usuarioViagemJpaController = new UsuarioViagemJpaController(emf);
        
        UsuarioViagem usuarioViagem = new Gson().fromJson(dadosUsuarioViagem, UsuarioViagem.class);
        
        String saida = null;
        
        try {
            usuarioViagemJpaController.destroy(usuarioViagem.getUsuarioViagemPK());
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("UsuarioViagemWS - erro ao remover: " + e);
        }
        
        return new Gson().toJson(usuarioViagemJpaController.findUsuarioViagem(usuarioViagem.getUsuarioViagemPK()));
    }
}
