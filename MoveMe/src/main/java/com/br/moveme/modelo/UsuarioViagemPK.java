/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.moveme.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author omupa
 */
@Embeddable
public class UsuarioViagemPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idviagem")
    private int idviagem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cpfusuario")
    private String cpfusuario;

    public UsuarioViagemPK() {
    }

    public UsuarioViagemPK(int idviagem, String cpfusuario) {
        this.idviagem = idviagem;
        this.cpfusuario = cpfusuario;
    }

    public int getIdviagem() {
        return idviagem;
    }

    public void setIdviagem(int idviagem) {
        this.idviagem = idviagem;
    }

    public String getCpfusuario() {
        return cpfusuario;
    }

    public void setCpfusuario(String cpfusuario) {
        this.cpfusuario = cpfusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idviagem;
        hash += (cpfusuario != null ? cpfusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioViagemPK)) {
            return false;
        }
        UsuarioViagemPK other = (UsuarioViagemPK) object;
        if (this.idviagem != other.idviagem) {
            return false;
        }
        if ((this.cpfusuario == null && other.cpfusuario != null) || (this.cpfusuario != null && !this.cpfusuario.equals(other.cpfusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.modelo.UsuarioViagemPK[ idviagem=" + idviagem + ", cpfusuario=" + cpfusuario + " ]";
    }
    
}
