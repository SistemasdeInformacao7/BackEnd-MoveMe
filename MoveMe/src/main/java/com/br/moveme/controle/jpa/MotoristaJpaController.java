/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.modelo.Motorista;
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
public class MotoristaJpaController implements Serializable {

    public MotoristaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Motorista motorista) {
        if (motorista.getViagemCollection() == null) {
            motorista.setViagemCollection(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Viagem> attachedViagemCollection = new ArrayList<Viagem>();
            for (Viagem viagemCollectionViagemToAttach : motorista.getViagemCollection()) {
                viagemCollectionViagemToAttach = em.getReference(viagemCollectionViagemToAttach.getClass(), viagemCollectionViagemToAttach.getId());
                attachedViagemCollection.add(viagemCollectionViagemToAttach);
            }
            motorista.setViagemCollection(attachedViagemCollection);
            em.persist(motorista);
            for (Viagem viagemCollectionViagem : motorista.getViagemCollection()) {
                Motorista oldIdmotoristaOfViagemCollectionViagem = viagemCollectionViagem.getIdmotorista();
                viagemCollectionViagem.setIdmotorista(motorista);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
                if (oldIdmotoristaOfViagemCollectionViagem != null) {
                    oldIdmotoristaOfViagemCollectionViagem.getViagemCollection().remove(viagemCollectionViagem);
                    oldIdmotoristaOfViagemCollectionViagem = em.merge(oldIdmotoristaOfViagemCollectionViagem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Motorista motorista) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Motorista persistentMotorista = em.find(Motorista.class, motorista.getId());
            Collection<Viagem> viagemCollectionOld = persistentMotorista.getViagemCollection();
            Collection<Viagem> viagemCollectionNew = motorista.getViagemCollection();
            Collection<Viagem> attachedViagemCollectionNew = new ArrayList<Viagem>();
            for (Viagem viagemCollectionNewViagemToAttach : viagemCollectionNew) {
                viagemCollectionNewViagemToAttach = em.getReference(viagemCollectionNewViagemToAttach.getClass(), viagemCollectionNewViagemToAttach.getId());
                attachedViagemCollectionNew.add(viagemCollectionNewViagemToAttach);
            }
            viagemCollectionNew = attachedViagemCollectionNew;
            motorista.setViagemCollection(viagemCollectionNew);
            motorista = em.merge(motorista);
            for (Viagem viagemCollectionOldViagem : viagemCollectionOld) {
                if (!viagemCollectionNew.contains(viagemCollectionOldViagem)) {
                    viagemCollectionOldViagem.setIdmotorista(null);
                    viagemCollectionOldViagem = em.merge(viagemCollectionOldViagem);
                }
            }
            for (Viagem viagemCollectionNewViagem : viagemCollectionNew) {
                if (!viagemCollectionOld.contains(viagemCollectionNewViagem)) {
                    Motorista oldIdmotoristaOfViagemCollectionNewViagem = viagemCollectionNewViagem.getIdmotorista();
                    viagemCollectionNewViagem.setIdmotorista(motorista);
                    viagemCollectionNewViagem = em.merge(viagemCollectionNewViagem);
                    if (oldIdmotoristaOfViagemCollectionNewViagem != null && !oldIdmotoristaOfViagemCollectionNewViagem.equals(motorista)) {
                        oldIdmotoristaOfViagemCollectionNewViagem.getViagemCollection().remove(viagemCollectionNewViagem);
                        oldIdmotoristaOfViagemCollectionNewViagem = em.merge(oldIdmotoristaOfViagemCollectionNewViagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = motorista.getId();
                if (findMotorista(id) == null) {
                    throw new NonexistentEntityException("The motorista with id " + id + " no longer exists.");
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
            Motorista motorista;
            try {
                motorista = em.getReference(Motorista.class, id);
                motorista.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motorista with id " + id + " no longer exists.", enfe);
            }
            Collection<Viagem> viagemCollection = motorista.getViagemCollection();
            for (Viagem viagemCollectionViagem : viagemCollection) {
                viagemCollectionViagem.setIdmotorista(null);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
            }
            em.remove(motorista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Motorista> findMotoristaEntities() {
        return findMotoristaEntities(true, -1, -1);
    }

    public List<Motorista> findMotoristaEntities(int maxResults, int firstResult) {
        return findMotoristaEntities(false, maxResults, firstResult);
    }

    private List<Motorista> findMotoristaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Motorista.class));
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

    public Motorista findMotorista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Motorista.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotoristaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Motorista> rt = cq.from(Motorista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
