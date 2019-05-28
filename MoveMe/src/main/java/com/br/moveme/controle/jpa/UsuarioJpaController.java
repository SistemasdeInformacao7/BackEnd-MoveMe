/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.IllegalOrphanException;
import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import com.br.moveme.modelo.Usuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.Viagem;
import java.util.ArrayList;
import java.util.Collection;
import com.br.moveme.modelo.UsuarioViagem;
import com.br.moveme.modelo.UsuarioAvaliaRestaurante;
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

    public void create(Usuario usuario) {
        if (usuario.getViagemCollection() == null) {
            usuario.setViagemCollection(new ArrayList<Viagem>());
        }
        if (usuario.getUsuarioViagemCollection() == null) {
            usuario.setUsuarioViagemCollection(new ArrayList<UsuarioViagem>());
        }
        if (usuario.getUsuarioAvaliaRestauranteCollection() == null) {
            usuario.setUsuarioAvaliaRestauranteCollection(new ArrayList<UsuarioAvaliaRestaurante>());
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
            Collection<UsuarioViagem> attachedUsuarioViagemCollection = new ArrayList<UsuarioViagem>();
            for (UsuarioViagem usuarioViagemCollectionUsuarioViagemToAttach : usuario.getUsuarioViagemCollection()) {
                usuarioViagemCollectionUsuarioViagemToAttach = em.getReference(usuarioViagemCollectionUsuarioViagemToAttach.getClass(), usuarioViagemCollectionUsuarioViagemToAttach.getUsuarioViagemPK());
                attachedUsuarioViagemCollection.add(usuarioViagemCollectionUsuarioViagemToAttach);
            }
            usuario.setUsuarioViagemCollection(attachedUsuarioViagemCollection);
            Collection<UsuarioAvaliaRestaurante> attachedUsuarioAvaliaRestauranteCollection = new ArrayList<UsuarioAvaliaRestaurante>();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach : usuario.getUsuarioAvaliaRestauranteCollection()) {
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach = em.getReference(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach.getClass(), usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach.getId());
                attachedUsuarioAvaliaRestauranteCollection.add(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestauranteToAttach);
            }
            usuario.setUsuarioAvaliaRestauranteCollection(attachedUsuarioAvaliaRestauranteCollection);
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
            for (UsuarioViagem usuarioViagemCollectionUsuarioViagem : usuario.getUsuarioViagemCollection()) {
                Usuario oldUsuarioOfUsuarioViagemCollectionUsuarioViagem = usuarioViagemCollectionUsuarioViagem.getUsuario();
                usuarioViagemCollectionUsuarioViagem.setUsuario(usuario);
                usuarioViagemCollectionUsuarioViagem = em.merge(usuarioViagemCollectionUsuarioViagem);
                if (oldUsuarioOfUsuarioViagemCollectionUsuarioViagem != null) {
                    oldUsuarioOfUsuarioViagemCollectionUsuarioViagem.getUsuarioViagemCollection().remove(usuarioViagemCollectionUsuarioViagem);
                    oldUsuarioOfUsuarioViagemCollectionUsuarioViagem = em.merge(oldUsuarioOfUsuarioViagemCollectionUsuarioViagem);
                }
            }
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante : usuario.getUsuarioAvaliaRestauranteCollection()) {
                Usuario oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.getCpfusuario();
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.setCpfusuario(usuario);
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                if (oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante != null) {
                    oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                    oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCpf());
            Collection<Viagem> viagemCollectionOld = persistentUsuario.getViagemCollection();
            Collection<Viagem> viagemCollectionNew = usuario.getViagemCollection();
            Collection<UsuarioViagem> usuarioViagemCollectionOld = persistentUsuario.getUsuarioViagemCollection();
            Collection<UsuarioViagem> usuarioViagemCollectionNew = usuario.getUsuarioViagemCollection();
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollectionOld = persistentUsuario.getUsuarioAvaliaRestauranteCollection();
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollectionNew = usuario.getUsuarioAvaliaRestauranteCollection();
            List<String> illegalOrphanMessages = null;
            for (UsuarioViagem usuarioViagemCollectionOldUsuarioViagem : usuarioViagemCollectionOld) {
                if (!usuarioViagemCollectionNew.contains(usuarioViagemCollectionOldUsuarioViagem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioViagem " + usuarioViagemCollectionOldUsuarioViagem + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Viagem> attachedViagemCollectionNew = new ArrayList<Viagem>();
            for (Viagem viagemCollectionNewViagemToAttach : viagemCollectionNew) {
                viagemCollectionNewViagemToAttach = em.getReference(viagemCollectionNewViagemToAttach.getClass(), viagemCollectionNewViagemToAttach.getId());
                attachedViagemCollectionNew.add(viagemCollectionNewViagemToAttach);
            }
            viagemCollectionNew = attachedViagemCollectionNew;
            usuario.setViagemCollection(viagemCollectionNew);
            Collection<UsuarioViagem> attachedUsuarioViagemCollectionNew = new ArrayList<UsuarioViagem>();
            for (UsuarioViagem usuarioViagemCollectionNewUsuarioViagemToAttach : usuarioViagemCollectionNew) {
                usuarioViagemCollectionNewUsuarioViagemToAttach = em.getReference(usuarioViagemCollectionNewUsuarioViagemToAttach.getClass(), usuarioViagemCollectionNewUsuarioViagemToAttach.getUsuarioViagemPK());
                attachedUsuarioViagemCollectionNew.add(usuarioViagemCollectionNewUsuarioViagemToAttach);
            }
            usuarioViagemCollectionNew = attachedUsuarioViagemCollectionNew;
            usuario.setUsuarioViagemCollection(usuarioViagemCollectionNew);
            Collection<UsuarioAvaliaRestaurante> attachedUsuarioAvaliaRestauranteCollectionNew = new ArrayList<UsuarioAvaliaRestaurante>();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach : usuarioAvaliaRestauranteCollectionNew) {
                usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach = em.getReference(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach.getClass(), usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach.getId());
                attachedUsuarioAvaliaRestauranteCollectionNew.add(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestauranteToAttach);
            }
            usuarioAvaliaRestauranteCollectionNew = attachedUsuarioAvaliaRestauranteCollectionNew;
            usuario.setUsuarioAvaliaRestauranteCollection(usuarioAvaliaRestauranteCollectionNew);
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
            for (UsuarioViagem usuarioViagemCollectionNewUsuarioViagem : usuarioViagemCollectionNew) {
                if (!usuarioViagemCollectionOld.contains(usuarioViagemCollectionNewUsuarioViagem)) {
                    Usuario oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem = usuarioViagemCollectionNewUsuarioViagem.getUsuario();
                    usuarioViagemCollectionNewUsuarioViagem.setUsuario(usuario);
                    usuarioViagemCollectionNewUsuarioViagem = em.merge(usuarioViagemCollectionNewUsuarioViagem);
                    if (oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem != null && !oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem.equals(usuario)) {
                        oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem.getUsuarioViagemCollection().remove(usuarioViagemCollectionNewUsuarioViagem);
                        oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem = em.merge(oldUsuarioOfUsuarioViagemCollectionNewUsuarioViagem);
                    }
                }
            }
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollectionOld) {
                if (!usuarioAvaliaRestauranteCollectionNew.contains(usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante)) {
                    usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante.setCpfusuario(null);
                    usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionOldUsuarioAvaliaRestaurante);
                }
            }
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollectionNew) {
                if (!usuarioAvaliaRestauranteCollectionOld.contains(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante)) {
                    Usuario oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.getCpfusuario();
                    usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.setCpfusuario(usuario);
                    usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                    if (oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante != null && !oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.equals(usuario)) {
                        oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante.getUsuarioAvaliaRestauranteCollection().remove(usuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                        oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante = em.merge(oldCpfusuarioOfUsuarioAvaliaRestauranteCollectionNewUsuarioAvaliaRestaurante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getCpf();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioViagem> usuarioViagemCollectionOrphanCheck = usuario.getUsuarioViagemCollection();
            for (UsuarioViagem usuarioViagemCollectionOrphanCheckUsuarioViagem : usuarioViagemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioViagem " + usuarioViagemCollectionOrphanCheckUsuarioViagem + " in its usuarioViagemCollection field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Viagem> viagemCollection = usuario.getViagemCollection();
            for (Viagem viagemCollectionViagem : viagemCollection) {
                viagemCollectionViagem.setCpfusuario(null);
                viagemCollectionViagem = em.merge(viagemCollectionViagem);
            }
            Collection<UsuarioAvaliaRestaurante> usuarioAvaliaRestauranteCollection = usuario.getUsuarioAvaliaRestauranteCollection();
            for (UsuarioAvaliaRestaurante usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante : usuarioAvaliaRestauranteCollection) {
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante.setCpfusuario(null);
                usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante = em.merge(usuarioAvaliaRestauranteCollectionUsuarioAvaliaRestaurante);
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
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
