/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.Usuario;
import com.br.moveme.modelo.Veiculo;
import com.br.moveme.modelo.Viagem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omupa
 */
public class ViagemJpaController implements Serializable {

    public ViagemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viagem viagem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario cpfusuario = viagem.getCpfusuario();
            if (cpfusuario != null) {
                cpfusuario = em.getReference(cpfusuario.getClass(), cpfusuario.getCpf());
                viagem.setCpfusuario(cpfusuario);
            }
            Veiculo idveiculo = viagem.getIdveiculo();
            if (idveiculo != null) {
                idveiculo = em.getReference(idveiculo.getClass(), idveiculo.getId());
                viagem.setIdveiculo(idveiculo);
            }
            em.persist(viagem);
            if (cpfusuario != null) {
                cpfusuario.getViagemCollection().add(viagem);
                cpfusuario = em.merge(cpfusuario);
            }
            if (idveiculo != null) {
                idveiculo.getViagemCollection().add(viagem);
                idveiculo = em.merge(idveiculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viagem viagem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem persistentViagem = em.find(Viagem.class, viagem.getId());
            Usuario cpfusuarioOld = persistentViagem.getCpfusuario();
            Usuario cpfusuarioNew = viagem.getCpfusuario();
            Veiculo idveiculoOld = persistentViagem.getIdveiculo();
            Veiculo idveiculoNew = viagem.getIdveiculo();
            if (cpfusuarioNew != null) {
                cpfusuarioNew = em.getReference(cpfusuarioNew.getClass(), cpfusuarioNew.getCpf());
                viagem.setCpfusuario(cpfusuarioNew);
            }
            if (idveiculoNew != null) {
                idveiculoNew = em.getReference(idveiculoNew.getClass(), idveiculoNew.getId());
                viagem.setIdveiculo(idveiculoNew);
            }
            viagem = em.merge(viagem);
            if (cpfusuarioOld != null && !cpfusuarioOld.equals(cpfusuarioNew)) {
                cpfusuarioOld.getViagemCollection().remove(viagem);
                cpfusuarioOld = em.merge(cpfusuarioOld);
            }
            if (cpfusuarioNew != null && !cpfusuarioNew.equals(cpfusuarioOld)) {
                cpfusuarioNew.getViagemCollection().add(viagem);
                cpfusuarioNew = em.merge(cpfusuarioNew);
            }
            if (idveiculoOld != null && !idveiculoOld.equals(idveiculoNew)) {
                idveiculoOld.getViagemCollection().remove(viagem);
                idveiculoOld = em.merge(idveiculoOld);
            }
            if (idveiculoNew != null && !idveiculoNew.equals(idveiculoOld)) {
                idveiculoNew.getViagemCollection().add(viagem);
                idveiculoNew = em.merge(idveiculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viagem.getId();
                if (findViagem(id) == null) {
                    throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem viagem;
            try {
                viagem = em.getReference(Viagem.class, id);
                viagem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.", enfe);
            }
            Usuario cpfusuario = viagem.getCpfusuario();
            if (cpfusuario != null) {
                cpfusuario.getViagemCollection().remove(viagem);
                cpfusuario = em.merge(cpfusuario);
            }
            Veiculo idveiculo = viagem.getIdveiculo();
            if (idveiculo != null) {
                idveiculo.getViagemCollection().remove(viagem);
                idveiculo = em.merge(idveiculo);
            }
            em.remove(viagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viagem> findViagemEntities() {
        return findViagemEntities(true, -1, -1);
    }

    public List<Viagem> findViagemEntities(int maxResults, int firstResult) {
        return findViagemEntities(false, maxResults, firstResult);
    }

    private List<Viagem> findViagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viagem.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Viagem findViagem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getViagemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viagem> rt = cq.from(Viagem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
