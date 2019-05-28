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
    @Column(name = "idusuario")
    private int idusuario;

    public UsuarioViagemPK() {
    }

    public UsuarioViagemPK(int idviagem, int idusuario) {
        this.idviagem = idviagem;
        this.idusuario = idusuario;
    }

    public int getIdviagem() {
        return idviagem;
    }

    public void setIdviagem(int idviagem) {
        this.idviagem = idviagem;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idviagem;
        hash += (int) idusuario;
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
        if (this.idusuario != other.idusuario) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.br.moveme.modelo.UsuarioViagemPK[ idviagem=" + idviagem + ", idusuario=" + idusuario + " ]";
    }
    
}
