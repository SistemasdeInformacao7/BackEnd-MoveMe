/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.ViagemJpaController;
import com.br.moveme.modelo.Motorista;
import com.br.moveme.modelo.Usuario;
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
@Path("/viagem")
public class ViagemWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        ViagemJpaController viagemJpaController = new ViagemJpaController(emf);

        Viagem viagem = new Gson().fromJson(dadosViagem, Viagem.class);

        try {
            viagemJpaController.create(viagem);
        } catch (Exception ex) {
            System.out.println("ViagemWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(viagemJpaController.findViagem(viagem.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        ViagemJpaController viagemJpaController = new ViagemJpaController(emf);

        List<Viagem> lista = null;
        String saida = null;

        try {
            lista = viagemJpaController.findViagemEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("ViagemWS - listar todos: " + e);
        }
        return saida;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        ViagemJpaController viagemJpaController = new ViagemJpaController(emf);

        Viagem restaurante = null;
        String saida = null;

        try {
            restaurante = viagemJpaController.findViagem(id);
            saida = new Gson().toJson(restaurante);
        } catch (Exception e) {
            System.out.println("ViagemWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosViagem) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        Viagem viagem = new Gson().fromJson(dadosViagem, Viagem.class);
        Viagem viagem1 = em.find(Viagem.class, viagem.getId());
        Usuario usuario = em.find(Usuario.class, viagem.getCpfusuario());
        Motorista motorista = em.find(Motorista.class, viagem.getIdmotorista());
        Veiculo veiculo = em.find(Veiculo.class, viagem.getIdveiculo());
        
        try {
            em.getTransaction().begin();
            viagem1.setDia(viagem.getDia());
            viagem1.setNota(viagem.getNota());
            viagem1.setCpfusuario(usuario);
            viagem1.setIdmotorista(motorista);
            viagem1.setIdveiculo(veiculo);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            System.out.println("ViagemWS - erro ao editar: " + e);
        }

        Viagem verificaViagem = em.find(Viagem.class, viagem.getId());

        return new Gson().toJson(verificaViagem);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        ViagemJpaController viagemJpaController = new ViagemJpaController(emf);

        String saida = null;

        try {
            viagemJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("ViagemWS - erro ao remover: " + e);
        }

        return new Gson().toJson(viagemJpaController.findViagem(id));
    }
}
