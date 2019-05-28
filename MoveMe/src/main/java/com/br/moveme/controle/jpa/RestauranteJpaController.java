/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.modelo.Restaurante;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.UsuarioAvaliaRestaurante;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omupa
 */
public class RestauranteJpaController implements Serializable {

    public RestauranteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Restaurante restaurante) {
        if (restaurante.getUsuarioAvaliaRestauranteCollection() == null) {
            restaurante.setUsuarioAvaliaRestauranteCollection(new ArrayList<UsuarioAvaliaRestaurante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsuarioAvaliaRestaurante> attachedUsuarioAvaliaRestauranteCollection = new ArrayList<UsuarioAvaliaRestaurante>();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach : restaurante.getUsuarioAvaliaRestauranteCollection()) {
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach = em.getReference(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach.getClass(), usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach.getId());
                attachedUsuarioAvaliaRestauranteCollection.add(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach);
            }
            restaurante.setUsuarioAvaliaRestauranteCollection(attachedUsuarioAvaliaRestauranteCollection);
            em.persist(restaurante);
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante : restaurante.getUsuarioAvaliaRestauranteCollection()) {
                Restaurante oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.getIdrestaurante();
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.setIdrestaurante(restaurante);
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                if (oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante != null) {
                    oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                    oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Restaurante restaurante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Restaurante persistentRestaurante = em.find(Restaurante.class, restaurante.getId());
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollectionOld = persistentRestaurante.getUsuarioAvaliaRestauranteCollection();
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollectionNew = restaurante.getUsuarioAvaliaRestauranteCollection();
            Collection<UsuarioAvaliaRestaurante> attachedUsuarioAvaliaRestauranteCollectionNew = new ArrayList<UsuarioAvaliaRestaurante>();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach : usuarioAvaliaRestauranteCollectionNew) {
                usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach = em.getReference(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach.getClass(), usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach.getId());
                attachedUsuarioAvaliaRestauranteCollectionNew.add(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach);
            }
            usuarioAvaliaRestauranteCollectionNew = attachedUsuarioAvaliaRestauranteCollectionNew;
            restaurante.setUsuarioAvaliaRestauranteCollection(usuarioAvaliaRestauranteCollectionNew);
            restaurante = em.merge(restaurante);
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollectionOld) {
                if (!usuarioAvaliaRestauranteCollectionNew.contains(usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante)) {
                    usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante.setIdrestaurante(null);
                    usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante);
                }
            }
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollectionNew) {
                if (!usuarioAvaliaRestauranteCollectionOld.contains(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante)) {
                    Restaurante oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.getIdrestaurante();
                    usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.setIdrestaurante(restaurante);
                    usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                    if (oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante != null && !oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.equals(restaurante)) {
                        oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                        oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = em.merge(oldIdrestauranteOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = restaurante.getId();
                if (findRestaurante(id) == null) {
                    throw new NonexistentEntityException("The restaurante with id " + id + " no longer exists.");
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
            Restaurante restaurante;
            try {
                restaurante = em.getReference(Restaurante.class, id);
                restaurante.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The restaurante with id " + id + " no longer exists.", enfe);
            }
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollection = restaurante.getUsuarioAvaliaRestauranteCollection();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollection) {
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.setIdrestaurante(null);
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
            }
            em.remove(restaurante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Restaurante> findRestauranteEntities() {
        return findRestauranteEntities(true, -1, -1);
    }

    public List<Restaurante> findRestauranteEntities(int maxResults, int firstResult) {
        return findRestauranteEntities(false, maxResults, firstResult);
    }

    private List<Restaurante> findRestauranteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Restaurante.class));
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

    public Restaurante findRestaurante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Restaurante.class, id);
        } finally {
            em.close();
        }
    }

    public int getRestauranteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Restaurante> rt = cq.from(Restaurante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
