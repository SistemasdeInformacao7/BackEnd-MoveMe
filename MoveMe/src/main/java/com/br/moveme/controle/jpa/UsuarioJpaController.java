/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.controle.jpa.exceptions.PreexistingEntityException;
import com.br.moveme.modelo.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getViagemCollection() == null) {
            usuario.setViagemCollection(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Viagem> attachedViagemCollection = new ArrayList<Viagem>();
            for (Viagem viagemCollectionViagemToAttach : usuario.getViagemCollection()) {
                viagemCollectionViagemToAttach = em.getReference(viagemCollectionViagemToAttach.getClass(), viagemCollectionViagemToAttach.getId());
                attachedViagemCollection.add(viagemCollectionViagemToAttach);
            }
            usuario.setViagemCollection(attachedViagemCollection);
            em.persist(usuario);
            for (Viagem viagemCollectionViagem : usuario.getViagemCollection()) {
                Usuario oldCpfusuarioOfViagemCollectionViagem = viagemCollectionViagem.getCpfusuario();
                viagemCollectionViagem.setCpfusuario(usuario);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
                if (oldCpfusuarioOfViagemCollectionViagem != null) {
                    oldCpfusuarioOfViagemCollectionViagem.getViagemCollection().remove(viagemCollectionViagem);
                    oldCpfusuarioOfViagemCollectionViagem = em.merge(oldCpfusuarioOfViagemCollectionViagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getCpf()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCpf());
            Collection<Viagem> viagemCollectionOld = persistentUsuario.getViagemCollection();
            Collection<Viagem> viagemCollectionNew = usuario.getViagemCollection();
            Collection<Viagem> attachedViagemCollectionNew = new ArrayList<Viagem>();
            for (Viagem viagemCollectionNewViagemToAttach : viagemCollectionNew) {
                viagemCollectionNewViagemToAttach = em.getReference(viagemCollectionNewViagemToAttach.getClass(), viagemCollectionNewViagemToAttach.getId());
                attachedViagemCollectionNew.add(viagemCollectionNewViagemToAttach);
            }
            viagemCollectionNew = attachedViagemCollectionNew;
            usuario.setViagemCollection(viagemCollectionNew);
            usuario = em.merge(usuario);
            for (Viagem viagemCollectionOldViagem : viagemCollectionOld) {
                if (!viagemCollectionNew.contains(viagemCollectionOldViagem)) {
                    viagemCollectionOldViagem.setCpfusuario(null);
                    viagemCollectionOldViagem = em.merge(viagemCollectionOldViagem);
                }
            }
            for (Viagem viagemCollectionNewViagem : viagemCollectionNew) {
                if (!viagemCollectionOld.contains(viagemCollectionNewViagem)) {
                    Usuario oldCpfusuarioOfViagemCollectionNewViagem = viagemCollectionNewViagem.getCpfusuario();
                    viagemCollectionNewViagem.setCpfusuario(usuario);
                    viagemCollectionNewViagem = em.merge(viagemCollectionNewViagem);
                    if (oldCpfusuarioOfViagemCollectionNewViagem != null && !oldCpfusuarioOfViagemCollectionNewViagem.equals(usuario)) {
                        oldCpfusuarioOfViagemCollectionNewViagem.getViagemCollection().remove(viagemCollectionNewViagem);
                        oldCpfusuarioOfViagemCollectionNewViagem = em.merge(oldCpfusuarioOfViagemCollectionNewViagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getCpf();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCpf();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Viagem> viagemCollection = usuario.getViagemCollection();
            for (Viagem viagemCollectionViagem : viagemCollection) {
                viagemCollectionViagem.setCpfusuario(null);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }
    
    public Usuario findUsuarioEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, email);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
