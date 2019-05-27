/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.modelos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Lucas
 */
@Entity
@Table(name = "restaurante")
@NamedQueries({
    @NamedQuery(name = "Restaurante.findAll", query = "SELECT r FROM Restaurante r")})
public class Restaurante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "cidade")
    private String cidade;
    @Size(max = 100)
    @Column(name = "moeda")
    private String moeda;
    @Size(max = 100)
    @Column(name = "cozinha")
    private String cozinha;
    @Column(name = "servico_online")
    private Integer servicoOnline;
    @Column(name = "servico_agora")
    private Integer servicoAgora;
    @Column(name = "faixa_preco")
    private Long faixaPreco;

    public Restaurante() {
    }

    public Restaurante(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getCozinha() {
        return cozinha;
    }

    public void setCozinha(String cozinha) {
        this.cozinha = cozinha;
    }

    public Integer getServicoOnline() {
        return servicoOnline;
    }

    public void setServicoOnline(Integer servicoOnline) {
        this.servicoOnline = servicoOnline;
    }

    public Integer getServicoAgora() {
        return servicoAgora;
    }

    public void setServicoAgora(Integer servicoAgora) {
        this.servicoAgora = servicoAgora;
    }

    public Long getFaixaPreco() {
        return faixaPreco;
    }

    public void setFaixaPreco(Long faixaPreco) {
        this.faixaPreco = faixaPreco;
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
        if (!(object instanceof Restaurante)) {
            return false;
        }
        Restaurante other = (Restaurante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.config.Restaurante[ id=" + id + " ]";
    }
    
}
