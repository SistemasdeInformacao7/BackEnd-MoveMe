/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.webservices;

import com.br.moveme.controle.jpa.MotoristaJpaController;
import com.br.moveme.controle.jpa.VeiculoJpaController;
import com.br.moveme.modelo.Motorista;
import com.br.moveme.modelo.Veiculo;
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
@Path("/veiculo")
public class VeiculoWS {

    @POST
    @Path("/inserir")
    @Consumes(MediaType.APPLICATION_JSON)
    public String inserir(String dadosVeiculo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        VeiculoJpaController veiculoJpaController = new VeiculoJpaController(emf);

        Veiculo veiculo = new Gson().fromJson(dadosVeiculo, Veiculo.class);

        try {
            veiculoJpaController.create(veiculo);
        } catch (Exception ex) {
            System.out.println("VeiculoWS - erro ao inserir: " + ex);
        }
        return new Gson().toJson(veiculoJpaController.findVeiculo(veiculo.getId()));
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        VeiculoJpaController veiculoJpaController = new VeiculoJpaController(emf);

        List<Veiculo> lista = null;
        String saida = null;

        try {
            lista = veiculoJpaController.findVeiculoEntities();
            saida = new Gson().toJson(lista);
        } catch (Exception e) {
            System.out.println("VeiculoWS - listar todos: " + e);
        }
        return saida;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        VeiculoJpaController veiculoJpaController = new VeiculoJpaController(emf);

        Veiculo veiculo = null;
        String saida = null;

        try {
            veiculo = veiculoJpaController.findVeiculo(id);
            saida = new Gson().toJson(veiculo);
        } catch (Exception e) {
            System.out.println("VeiculoWS - listar todos: " + e);
        }
        return saida;
    }

    @PUT
    @Path("/editar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String editar(String dadosMotorista) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        EntityManager em = emf.createEntityManager();

        Veiculo veiculo = new Gson().fromJson(dadosMotorista, Veiculo.class);
        Veiculo veiculo1 = em.find(Veiculo.class, veiculo.getId());

        try {
            em.getTransaction().begin();
            veiculo1.setNumeroVagas(veiculo.getNumeroVagas());
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("VeiculoWS - erro ao editar: " + e);
        }

        Veiculo verificaVeiculo = em.find(Veiculo.class, veiculo.getId());

        return new Gson().toJson(verificaVeiculo);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String remover(@PathParam("id") int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movemePU");
        VeiculoJpaController veiculoJpaController = new VeiculoJpaController(emf);

        String saida = null;

        try {
            veiculoJpaController.destroy(id);
            //saida = new Gson().toJson(usuarioJpaController.findUsuario(cpf));
        } catch (Exception e) {
            System.out.println("VeiculoWS - erro ao remover: " + e);
        }

        return new Gson().toJson(veiculoJpaController.findVeiculo(id));
    }
}
