/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.controle.jpa;

import com.br.moveme.controle.jpa.exceptions.IllegalOrphanException;
import com.br.moveme.controle.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.br.moveme.modelo.Motorista;
import com.br.moveme.modelo.Usuario;
import com.br.moveme.modelo.Veiculo;
import com.br.moveme.modelo.UsuarioViagem;
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
public class ViagemJpaController implements Serializable {

    public ViagemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viagem viagem) {
        if (viagem.getUsuarioViagemCollection() == null) {
            viagem.setUsuarioViagemCollection(new ArrayList<UsuarioViagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Motorista idmotorista = viagem.getIdmotorista();
            if (idmotorista != null) {
                idmotorista = em.getReference(idmotorista.getClass(), idmotorista.getId());
                viagem.setIdmotorista(idmotorista);
            }
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
            Collection<UsuarioViagem> attachedUsuarioViagemCollection = new ArrayList<UsuarioViagem>();
            for (UsuarioViagem usuarioViagemCollectionUsuarioViagemToAttach : viagem.getUsuarioViagemCollection()) {
                usuarioViagemCollectionUsuarioViagemToAttach = em.getReference(usuarioViagemCollectionUsuarioViagemToAttach.getClass(), usuarioViagemCollectionUsuarioViagemToAttach.getUsuarioViagemPK());
                attachedUsuarioViagemCollection.add(usuarioViagemCollectionUsuarioViagemToAttach);
            }
            viagem.setUsuarioViagemCollection(attachedUsuarioViagemCollection);
            em.persist(viagem);
            if (idmotorista != null) {
                idmotorista.getViagemCollection().add(viagem);
                idmotorista = em.merge(idmotorista);
            }
            if (cpfusuario != null) {
                cpfusuario.getViagemCollection().add(viagem);
                cpfusuario = em.merge(cpfusuario);
            }
            if (idveiculo != null) {
                idveiculo.getViagemCollection().add(viagem);
                idveiculo = em.merge(idveiculo);
            }
            for (UsuarioViagem usuarioViagemCollectionUsuarioViagem : viagem.getUsuarioViagemCollection()) {
                Viagem oldViagemOfUsuarioViagemCollectionUsuarioViagem = usuarioViagemCollectionUsuarioViagem.getViagem();
                usuarioViagemCollectionUsuarioViagem.setViagem(viagem);
                usuarioViagemCollectionUsuarioViagem = em.merge(usuarioViagemCollectionUsuarioViagem);
                if (oldViagemOfUsuarioViagemCollectionUsuarioViagem != null) {
                    oldViagemOfUsuarioViagemCollectionUsuarioViagem.getUsuarioViagemCollection().remove(usuarioViagemCollectionUsuarioViagem);
                    oldViagemOfUsuarioViagemCollectionUsuarioViagem = em.merge(oldViagemOfUsuarioViagemCollectionUsuarioViagem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viagem viagem) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem persistentViagem = em.find(Viagem.class, viagem.getId());
            Motorista idmotoristaOld = persistentViagem.getIdmotorista();
            Motorista idmotoristaNew = viagem.getIdmotorista();
            Usuario cpfusuarioOld = persistentViagem.getCpfusuario();
            Usuario cpfusuarioNew = viagem.getCpfusuario();
            Veiculo idveiculoOld = persistentViagem.getIdveiculo();
            Veiculo idveiculoNew = viagem.getIdveiculo();
            Collection<UsuarioViagem> usuarioViagemCollectionOld = persistentViagem.getUsuarioViagemCollection();
            Collection<UsuarioViagem> usuarioViagemCollectionNew = viagem.getUsuarioViagemCollection();
            List<String> illegalOrphanMessages = null;
            for (UsuarioViagem usuarioViagemCollectionOldUsuarioViagem : usuarioViagemCollectionOld) {
                if (!usuarioViagemCollectionNew.contains(usuarioViagemCollectionOldUsuarioViagem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioViagem " + usuarioViagemCollectionOldUsuarioViagem + " since its viagem field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idmotoristaNew != null) {
                idmotoristaNew = em.getReference(idmotoristaNew.getClass(), idmotoristaNew.getId());
                viagem.setIdmotorista(idmotoristaNew);
            }
            if (cpfusuarioNew != null) {
                cpfusuarioNew = em.getReference(cpfusuarioNew.getClass(), cpfusuarioNew.getCpf());
                viagem.setCpfusuario(cpfusuarioNew);
            }
            if (idveiculoNew != null) {
                idveiculoNew = em.getReference(idveiculoNew.getClass(), idveiculoNew.getId());
                viagem.setIdveiculo(idveiculoNew);
            }
            Collection<UsuarioViagem> attachedUsuarioViagemCollectionNew = new ArrayList<UsuarioViagem>();
            for (UsuarioViagem usuarioViagemCollectionNewUsuarioViagemToAttach : usuarioViagemCollectionNew) {
                usuarioViagemCollectionNewUsuarioViagemToAttach = em.getReference(usuarioViagemCollectionNewUsuarioViagemToAttach.getClass(), usuarioViagemCollectionNewUsuarioViagemToAttach.getUsuarioViagemPK());
                attachedUsuarioViagemCollectionNew.add(usuarioViagemCollectionNewUsuarioViagemToAttach);
            }
            usuarioViagemCollectionNew = attachedUsuarioViagemCollectionNew;
            viagem.setUsuarioViagemCollection(usuarioViagemCollectionNew);
            viagem = em.merge(viagem);
            if (idmotoristaOld != null && !idmotoristaOld.equals(idmotoristaNew)) {
                idmotoristaOld.getViagemCollection().remove(viagem);
                idmotoristaOld = em.merge(idmotoristaOld);
            }
            if (idmotoristaNew != null && !idmotoristaNew.equals(idmotoristaOld)) {
                idmotoristaNew.getViagemCollection().add(viagem);
                idmotoristaNew = em.merge(idmotoristaNew);
            }
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
            for (UsuarioViagem usuarioViagemCollectionNewUsuarioViagem : usuarioViagemCollectionNew) {
                if (!usuarioViagemCollectionOld.contains(usuarioViagemCollectionNewUsuarioViagem)) {
                    Viagem oldViagemOfUsuarioViagemCollectionNewUsuarioViagem = usuarioViagemCollectionNewUsuarioViagem.getViagem();
                    usuarioViagemCollectionNewUsuarioViagem.setViagem(viagem);
                    usuarioViagemCollectionNewUsuarioViagem = em.merge(usuarioViagemCollectionNewUsuarioViagem);
                    if (oldViagemOfUsuarioViagemCollectionNewUsuarioViagem != null && !oldViagemOfUsuarioViagemCollectionNewUsuarioViagem.equals(viagem)) {
                        oldViagemOfUsuarioViagemCollectionNewUsuarioViagem.getUsuarioViagemCollection().remove(usuarioViagemCollectionNewUsuarioViagem);
                        oldViagemOfUsuarioViagemCollectionNewUsuarioViagem = em.merge(oldViagemOfUsuarioViagemCollectionNewUsuarioViagem);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioViagem> usuarioViagemCollectionOrphanCheck = viagem.getUsuarioViagemCollection();
            for (UsuarioViagem usuarioViagemCollectionOrphanCheckUsuarioViagem : usuarioViagemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Viagem (" + viagem + ") cannot be destroyed since the UsuarioViagem " + usuarioViagemCollectionOrphanCheckUsuarioViagem + " in its usuarioViagemCollection field has a non-nullable viagem field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Motorista idmotorista = viagem.getIdmotorista();
            if (idmotorista != null) {
                idmotorista.getViagemCollection().remove(viagem);
                idmotorista = em.merge(idmotorista);
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
