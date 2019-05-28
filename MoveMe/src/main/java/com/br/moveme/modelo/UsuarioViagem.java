/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author omupa
 */
@Entity
@Table(name = "usuario_viagem")
@NamedQueries({
    @NamedQuery(name = "UsuarioViagem.findAll", query = "SELECT u FROM UsuarioViagem u"),
    @NamedQuery(name = "UsuarioViagem.findByIdviagem", query = "SELECT u FROM UsuarioViagem u WHERE u.usuarioViagemPK.idviagem = :idviagem"),
    @NamedQuery(name = "UsuarioViagem.findByIdusuario", query = "SELECT u FROM UsuarioViagem u WHERE u.usuarioViagemPK.idusuario = :idusuario"),
    @NamedQuery(name = "UsuarioViagem.findByAvaliacao", query = "SELECT u FROM UsuarioViagem u WHERE u.avaliacao = :avaliacao"),
    @NamedQuery(name = "UsuarioViagem.findByPreco", query = "SELECT u FROM UsuarioViagem u WHERE u.preco = :preco")})
public class UsuarioViagem implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioViagemPK usuarioViagemPK;
    @Column(name = "avaliacao")
    private Integer avaliacao;
    @Column(name = "preco")
    private Long preco;
    @JoinColumn(name = "idusuario", referencedColumnName = "cpf", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "idviagem", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Viagem viagem;

    public UsuarioViagem() {
    }

    public UsuarioViagem(UsuarioViagemPK usuarioViagemPK) {
        this.usuarioViagemPK = usuarioViagemPK;
    }

    public UsuarioViagem(int idviagem, int idusuario) {
        this.usuarioViagemPK = new UsuarioViagemPK(idviagem, idusuario);
    }

    public UsuarioViagemPK getUsuarioViagemPK() {
        return usuarioViagemPK;
    }

    public void setUsuarioViagemPK(UsuarioViagemPK usuarioViagemPK) {
        this.usuarioViagemPK = usuarioViagemPK;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Long getPreco() {
        return preco;
    }

    public void setPreco(Long preco) {
        this.preco = preco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioViagemPK != null ? usuarioViagemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioViagem)) {
            return false;
        }
        UsuarioViagem other = (UsuarioViagem) object;
        if ((this.usuarioViagemPK == null && other.usuarioViagemPK != null) || (this.usuarioViagemPK != null && !this.usuarioViagemPK.equals(other.usuarioViagemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.modelo.UsuarioViagem[ usuarioViagemPK=" + usuarioViagemPK + " ]";
    }
    
}
