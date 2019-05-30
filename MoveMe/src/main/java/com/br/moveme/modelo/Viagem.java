/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author omupa
 */
@Entity
@Table(name = "viagem")
@NamedQueries({
    @NamedQuery(name = "Viagem.findAll", query = "SELECT v FROM Viagem v")})
public class Viagem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id")
    private Integer id;
    @Column(name = "dia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dia;
    @Column(name = "nota")
    private Integer nota;
    @JoinColumn(name = "idmotorista", referencedColumnName = "id")
    @ManyToOne
    private Motorista idmotorista;
    @JoinColumn(name = "cpfusuario", referencedColumnName = "cpf")
    @ManyToOne
    private Usuario cpfusuario;
    @JoinColumn(name = "idveiculo", referencedColumnName = "id")
    @ManyToOne
    private Veiculo idveiculo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viagem")
    private Collection<UsuarioViagem> usuarioViagemCollection;

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

    public Motorista getIdmotorista() {
        return idmotorista;
    }

    public void setIdmotorista(Motorista idmotorista) {
        this.idmotorista = idmotorista;
    }

    public Usuario getCpfusuario() {
        return cpfusuario;
    }

    public void setCpfusuario(Usuario cpfusuario) {
        this.cpfusuario = cpfusuario;
    }

    public Veiculo getIdveiculo() {
        return idveiculo;
    }

    public void setIdveiculo(Veiculo idveiculo) {
        this.idveiculo = idveiculo;
    }

    public Collection<UsuarioViagem> getUsuarioViagemCollection() {
        return usuarioViagemCollection;
    }

    public void setUsuarioViagemCollection(Collection<UsuarioViagem> usuarioViagemCollection) {
        this.usuarioViagemCollection = usuarioViagemCollection;
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
        return "com.br.moveme.modelo.Viagem[ id=" + id + " ]";
    }
    
}
