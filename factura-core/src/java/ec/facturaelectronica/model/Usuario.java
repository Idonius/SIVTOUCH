/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Christian
 */
@Entity
@Table(name = "usuario")
@SequenceGenerator(sequenceName = "seq_usuario", name = "seq_usuario_gen", allocationSize = 1)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u where u.idEstadoCatalogo.idCatalogo=:idEstadoCatalogo order by u.nombreUsuario"),
    @NamedQuery(name = "Usuario.findByPerfil", query = "SELECT u FROM Usuario u where u.idPerfil=:idPerfil and u.idEstadoCatalogo.idCatalogo=:idEstadoCatalogo"),
    @NamedQuery(name = "Usuario.ValidateUser", query = "SELECT u FROM Usuario u where u.nickUsuario=:nickUsuario and u.claveUsuario=:claveUsuario and u.idEstadoCatalogo.idCatalogo=:idEstadoCatalogo"),
    @NamedQuery(name = "Usuario.findByEmpresa", query = "SELECT u FROM Usuario u where u.idEstadoCatalogo.idCatalogo=:idEstadoCatalogo and u.idEmpresa=:idEmpresa"),
    @NamedQuery(name = "Usuario.findByNick", query = "SELECT u FROM Usuario u where u.nickUsuario=:nickUsuario "),
    @NamedQuery(name = "Usuario.findByCedulaUsuario", query = "SELECT u FROM Usuario u where u.cedulaUsuario=:cedula"),
    @NamedQuery(name = "Usuario.findByUsuarioToken", query = "SELECT u FROM Usuario u where u.token=:token")    
})  

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "seq_usuario_gen")
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Size(max = 150)
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Size(max = 50)
    @Column(name = "nick_usuario")
    private String nickUsuario;
    @Size(max = 200)
    @Column(name = "clave_usuario")
    private String claveUsuario;
    @Size(max = 200)
    @Transient
    private String confirmarClave;
    @Size(max = 13)
    @Column(name = "cedula_usuario")
    private String cedulaUsuario;
    @Size(max = 100)
    @Column(name = "cargo_usuario")
    private String cargoUsuario;
    @Size(max = 200)
    @Column(name = "email_usuario")
    private String emailUsuario;
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @ManyToOne
    private Perfil idPerfil;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa idEmpresa;
    @JoinColumn(name = "id_estado_catalogo", referencedColumnName = "id_catalogo")
    @ManyToOne
    private Catalogo idEstadoCatalogo;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_expiracion")
    private Date fechaExpiracion;
    private String token;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNickUsuario() {
        return nickUsuario;
    }

    public void setNickUsuario(String nickUsuario) {
        this.nickUsuario = nickUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getConfirmarClave() {
        return confirmarClave;
    }

    public void setConfirmarClave(String confirmarClave) {
        this.confirmarClave = confirmarClave;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getCargoUsuario() {
        return cargoUsuario;
    }

    public void setCargoUsuario(String cargoUsuario) {
        this.cargoUsuario = cargoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Perfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Perfil idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Catalogo getIdEstadoCatalogo() {
        return idEstadoCatalogo;
    }

    public void setIdEstadoCatalogo(Catalogo idEstadoCatalogo) {
        this.idEstadoCatalogo = idEstadoCatalogo;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
   @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.facturaelectronica.model.Usuario[ idUsuario=" + idUsuario + " ]";
    }

}
