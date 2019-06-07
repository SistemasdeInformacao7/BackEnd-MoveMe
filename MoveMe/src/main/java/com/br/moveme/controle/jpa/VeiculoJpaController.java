/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.modelo.Veiculo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.Viagem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omupa
 */
public class VeiculoJpaController implements Serializable {

    public VeiculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Veiculo veiculo) {
        if (veiculo.getViagemCollection() == null) {
            veiculo.setViagemCollection(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Viagem> attachedViagemCollection = new ArrayList<Viagem>();
            for (Viagem viagemCollectionViagemToAttach : veiculo.getViagemCollection()) {
                viagemCollectionViagemToAttach = em.getReference(viagemCollectionViagemToAttach.getClass(), viagemCollectionViagemToAttach.getId());
                attachedViagemCollection.add(viagemCollectionViagemToAttach);
            }
            veiculo.setViagemCollection(attachedViagemCollection);
            em.persist(veiculo);
            for (Viagem viagemCollectionViagem : veiculo.getViagemCollection()) {
                Veiculo oldIdveiculoOfViagemCollectionViagem = viagemCollectionViagem.getIdveiculo();
                viagemCollectionViagem.setIdveiculo(veiculo);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
                if (oldIdveiculoOfViagemCollectionViagem != null) {
                    oldIdveiculoOfViagemCollectionViagem.getViagemCollection().remove(viagemCollectionViagem);
                    oldIdveiculoOfViagemCollectionViagem = em.merge(oldIdveiculoOfViagemCollectionViagem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Veiculo veiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Veiculo persistentVeiculo = em.find(Veiculo.class, veiculo.getId());
            Collection<Viagem> viagemCollectionOld = persistentVeiculo.getViagemCollection();
            Collection<Viagem> viagemCollectionNew = veiculo.getViagemCollection();
            Collection<Viagem> attachedViagemCollectionNew = new ArrayList<Viagem>();
            for (Viagem viagemCollectionNewViagemToAttach : viagemCollectionNew) {
                viagemCollectionNewViagemToAttach = em.getReference(viagemCollectionNewViagemToAttach.getClass(), viagemCollectionNewViagemToAttach.getId());
                attachedViagemCollectionNew.add(viagemCollectionNewViagemToAttach);
            }
            viagemCollectionNew = attachedViagemCollectionNew;
            veiculo.setViagemCollection(viagemCollectionNew);
            veiculo = em.merge(veiculo);
            for (Viagem viagemCollectionOldViagem : viagemCollectionOld) {
                if (!viagemCollectionNew.contains(viagemCollectionOldViagem)) {
                    viagemCollectionOldViagem.setIdveiculo(null);
                    viagemCollectionOldViagem = em.merge(viagemCollectionOldViagem);
                }
            }
            for (Viagem viagemCollectionNewViagem : viagemCollectionNew) {
                if (!viagemCollectionOld.contains(viagemCollectionNewViagem)) {
                    Veiculo oldIdveiculoOfViagemCollectionNewViagem = viagemCollectionNewViagem.getIdveiculo();
                    viagemCollectionNewViagem.setIdveiculo(veiculo);
                    viagemCollectionNewViagem = em.merge(viagemCollectionNewViagem);
                    if (oldIdveiculoOfViagemCollectionNewViagem != null && !oldIdveiculoOfViagemCollectionNewViagem.equals(veiculo)) {
                        oldIdveiculoOfViagemCollectionNewViagem.getViagemCollection().remove(viagemCollectionNewViagem);
                        oldIdveiculoOfViagemCollectionNewViagem = em.merge(oldIdveiculoOfViagemCollectionNewViagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = veiculo.getId();
                if (findVeiculo(id) == null) {
                    throw new NonexistentEntityException("The veiculo with id " + id + " no longer exists.");
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
            Veiculo veiculo;
            try {
                veiculo = em.getReference(Veiculo.class, id);
                veiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The veiculo with id " + id + " no longer exists.", enfe);
            }
            Collection<Viagem> viagemCollection = veiculo.getViagemCollection();
            for (Viagem viagemCollectionViagem : viagemCollection) {
                viagemCollectionViagem.setIdveiculo(null);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
            }
            em.remove(veiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Veiculo> findVeiculoEntities() {
        return findVeiculoEntities(true, -1, -1);
    }

    public List<Veiculo> findVeiculoEntities(int maxResults, int firstResult) {
        return findVeiculoEntities(false, maxResults, firstResult);
    }

    private List<Veiculo> findVeiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Veiculo.class));
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

    public Veiculo findVeiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Veiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVeiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Veiculo> rt = cq.from(Veiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
