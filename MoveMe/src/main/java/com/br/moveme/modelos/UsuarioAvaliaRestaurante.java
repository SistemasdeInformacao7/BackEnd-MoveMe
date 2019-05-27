/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.modelos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lucas
 */
@Entity
@Table(name = "usuario_avalia_restaurante")
@NamedQueries({
    @NamedQuery(name = "UsuarioAvaliaRestaurante.findAll", query = "SELECT u FROM UsuarioAvaliaRestaurante u")})
public class UsuarioAvaliaRestaurante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "avaliacao")
    private Integer avaliacao;
    @Column(name = "dia")
    @Temporal(TemporalType.DATE)
    private Date dia;
    @Column(name = "idrestaurante")
    private Integer idrestaurante;
    @Column(name = "cpfpassageiro")
    private Integer cpfpassageiro;

    public UsuarioAvaliaRestaurante() {
    }

    public UsuarioAvaliaRestaurante(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Integer getIdrestaurante() {
        return idrestaurante;
    }

    public void setIdrestaurante(Integer idrestaurante) {
        this.idrestaurante = idrestaurante;
    }

    public Integer getCpfpassageiro() {
        return cpfpassageiro;
    }

    public void setCpfpassageiro(Integer cpfpassageiro) {
        this.cpfpassageiro = cpfpassageiro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioAvaliaRestaurante)) {
            return false;
        }
        UsuarioAvaliaRestaurante other = (UsuarioAvaliaRestaurante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.config.UsuarioAvaliaRestaurante[ id=" + id + " ]";
    }
    
}
