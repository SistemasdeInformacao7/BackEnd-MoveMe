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
import com.br.moveme.modelo.Restaurante;
import com.br.moveme.modelo.UsuarioAvaliaRestaurante;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class UsuarioAvaliaRestauranteJpaController implements Serializable {

    public UsuarioAvaliaRestauranteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioAvaliaRestaurante usuarioAvaliaRestaurante) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Restaurante idrestaurante = usuarioAvaliaRestaurante.getIdrestaurante();
            if (idrestaurante != null) {
                idrestaurante = em.getReference(idrestaurante.getClass(), idrestaurante.getId());
                usuarioAvaliaRestaurante.setIdrestaurante(idrestaurante);
            }
            em.persist(usuarioAvaliaRestaurante);
            if (idrestaurante != null) {
                idrestaurante.getUsuarioAvaliaRestauranteCollection().add(usuarioAvaliaRestaurante);
                idrestaurante = em.merge(idrestaurante);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioAvaliaRestaurante usuarioAvaliaRestaurante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioAvaliaRestaurante persistentUsuarioAvaliaRestaurante = em.find(UsuarioAvaliaRestaurante.class, usuarioAvaliaRestaurante.getId());
            Restaurante idrestauranteOld = persistentUsuarioAvaliaRestaurante.getIdrestaurante();
            Restaurante idrestauranteNew = usuarioAvaliaRestaurante.getIdrestaurante();
            if (idrestauranteNew != null) {
                idrestauranteNew = em.getReference(idrestauranteNew.getClass(), idrestauranteNew.getId());
                usuarioAvaliaRestaurante.setIdrestaurante(idrestauranteNew);
            }
            usuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestaurante);
            if (idrestauranteOld != null && !idrestauranteOld.equals(idrestauranteNew)) {
                idrestauranteOld.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestaurante);
                idrestauranteOld = em.merge(idrestauranteOld);
            }
            if (idrestauranteNew != null && !idrestauranteNew.equals(idrestauranteOld)) {
                idrestauranteNew.getUsuarioAvaliaRestauranteCollection().add(usuarioAvaliaRestaurante);
                idrestauranteNew = em.merge(idrestauranteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarioAvaliaRestaurante.getId();
                if (findUsuarioAvaliaRestaurante(id) == null) {
                    throw new NonexistentEntityException("The usuarioAvaliaRestaurante with id " + id + " no longer exists.");
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
            UsuarioAvaliaRestaurante usuarioAvaliaRestaurante;
            try {
                usuarioAvaliaRestaurante = em.getReference(UsuarioAvaliaRestaurante.class, id);
                usuarioAvaliaRestaurante.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioAvaliaRestaurante with id " + id + " no longer exists.", enfe);
            }
            Restaurante idrestaurante = usuarioAvaliaRestaurante.getIdrestaurante();
            if (idrestaurante != null) {
                idrestaurante.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestaurante);
                idrestaurante = em.merge(idrestaurante);
            }
            em.remove(usuarioAvaliaRestaurante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioAvaliaRestaurante> findUsuarioAvaliaRestauranteEntities() {
        return findUsuarioAvaliaRestauranteEntities(true, -1, -1);
    }

    public List<UsuarioAvaliaRestaurante> findUsuarioAvaliaRestauranteEntities(int maxResults, int firstResult) {
        return findUsuarioAvaliaRestauranteEntities(false, maxResults, firstResult);
    }

    private List<UsuarioAvaliaRestaurante> findUsuarioAvaliaRestauranteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioAvaliaRestaurante.class));
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

    public UsuarioAvaliaRestaurante findUsuarioAvaliaRestaurante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioAvaliaRestaurante.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioAvaliaRestauranteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioAvaliaRestaurante> rt = cq.from(UsuarioAvaliaRestaurante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
