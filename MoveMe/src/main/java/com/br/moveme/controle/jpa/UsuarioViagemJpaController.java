/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.controle.jpa.exceptions.PreexistingEntityException;
import com.br.moveme.modelo.UsuarioViagem;
import com.br.moveme.modelo.UsuarioViagemPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.Viagem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omupa
 */
public class UsuarioViagemJpaController implements Serializable {

    public UsuarioViagemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioViagem usuarioViagem) throws PreexistingEntityException, Exception {
        if (usuarioViagem.getUsuarioViagemPK() == null) {
            usuarioViagem.setUsuarioViagemPK(new UsuarioViagemPK());
        }
        usuarioViagem.getUsuarioViagemPK().setIdviagem(usuarioViagem.getViagem().getId());
        usuarioViagem.getUsuarioViagemPK().setCpfusuario(usuarioViagem.getUsuario().getCpf());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem viagem = usuarioViagem.getViagem();
            if (viagem != null) {
                viagem = em.getReference(viagem.getClass(), viagem.getId());
                usuarioViagem.setViagem(viagem);
            }
            em.persist(usuarioViagem);
            if (viagem != null) {
                viagem.getUsuarioViagemCollection().add(usuarioViagem);
                viagem = em.merge(viagem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioViagem(usuarioViagem.getUsuarioViagemPK()) != null) {
                throw new PreexistingEntityException("UsuarioViagem " + usuarioViagem + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioViagem usuarioViagem) throws NonexistentEntityException, Exception {
        usuarioViagem.getUsuarioViagemPK().setIdviagem(usuarioViagem.getViagem().getId());
        usuarioViagem.getUsuarioViagemPK().setCpfusuario(usuarioViagem.getUsuario().getCpf());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioViagem persistentUsuarioViagem = em.find(UsuarioViagem.class, usuarioViagem.getUsuarioViagemPK());
            Viagem viagemOld = persistentUsuarioViagem.getViagem();
            Viagem viagemNew = usuarioViagem.getViagem();
            if (viagemNew != null) {
                viagemNew = em.getReference(viagemNew.getClass(), viagemNew.getId());
                usuarioViagem.setViagem(viagemNew);
            }
            usuarioViagem = em.merge(usuarioViagem);
            if (viagemOld != null && !viagemOld.equals(viagemNew)) {
                viagemOld.getUsuarioViagemCollection().remove(usuarioViagem);
                viagemOld = em.merge(viagemOld);
            }
            if (viagemNew != null && !viagemNew.equals(viagemOld)) {
                viagemNew.getUsuarioViagemCollection().add(usuarioViagem);
                viagemNew = em.merge(viagemNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioViagemPK id = usuarioViagem.getUsuarioViagemPK();
                if (findUsuarioViagem(id) == null) {
                    throw new NonexistentEntityException("The usuarioViagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioViagemPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioViagem usuarioViagem;
            try {
                usuarioViagem = em.getReference(UsuarioViagem.class, id);
                usuarioViagem.getUsuarioViagemPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioViagem with id " + id + " no longer exists.", enfe);
            }
            Viagem viagem = usuarioViagem.getViagem();
            if (viagem != null) {
                viagem.getUsuarioViagemCollection().remove(usuarioViagem);
                viagem = em.merge(viagem);
            }
            em.remove(usuarioViagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioViagem> findUsuarioViagemEntities() {
        return findUsuarioViagemEntities(true, -1, -1);
    }

    public List<UsuarioViagem> findUsuarioViagemEntities(int maxResults, int firstResult) {
        return findUsuarioViagemEntities(false, maxResults, firstResult);
    }

    private List<UsuarioViagem> findUsuarioViagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioViagem.class));
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

    public UsuarioViagem findUsuarioViagem(UsuarioViagemPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioViagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioViagemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioViagem> rt = cq.from(UsuarioViagem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
