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
@Table(name = "viagem")
@NamedQueries({
    @NamedQuery(name = "Viagem.findAll", query = "SELECT v FROM Viagem v")})
public class Viagem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "dia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dia;
    @Column(name = "nota")
    private Integer nota;
    @Column(name = "cpfpassageiro")
    private Integer cpfpassageiro;
    @Column(name = "idmotorista")
    private Integer idmotorista;
    @Column(name = "idveiculo")
    private Integer idveiculo;

    public Viagem() {
    }

    public Viagem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public Integer getCpfpassageiro() {
        return cpfpassageiro;
    }

    public void setCpfpassageiro(Integer cpfpassageiro) {
        this.cpfpassageiro = cpfpassageiro;
    }

    public Integer getIdmotorista() {
        return idmotorista;
    }

    public void setIdmotorista(Integer idmotorista) {
        this.idmotorista = idmotorista;
    }

    public Integer getIdveiculo() {
        return idveiculo;
    }

    public void setIdveiculo(Integer idveiculo) {
        this.idveiculo = idveiculo;
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
        if (!(object instanceof Viagem)) {
            return false;
        }
        Viagem other = (Viagem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.config.Viagem[ id=" + id + " ]";
    }
    
}
