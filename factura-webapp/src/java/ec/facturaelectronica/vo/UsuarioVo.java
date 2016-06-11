/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Christian
 */
public class UsuarioVo implements Serializable{

    private List<SelectItem> listaPerfiles;
    private Integer selectedPerfil;

    private List<SelectItem> listaEmpresas;
    private Integer selectedEmpresa;

    private String nombre;
    private String nick;
    private String clave;
    private String cedula;
    private String cargo;
    private String email;

    private String rclave;
    private boolean editUser;

    private String nclave;
    private String rnclave;
    private boolean renderEmpresa;
    private boolean renderActionNewEdit;
    private boolean renderActionClave;

    public UsuarioVo() {
        listaPerfiles = new ArrayList<>();
    }

    public void nuevoUsuario() {
        setRenderActionNewEdit(Boolean.FALSE);
        setRenderEmpresa(Boolean.TRUE);
    }

    public void initUsuario() {
        setRenderActionNewEdit(Boolean.TRUE);
        setRenderEmpresa(Boolean.TRUE);
        setRenderActionClave(Boolean.FALSE);
    }

    public void cambiarClave() {
        setRenderActionClave(Boolean.TRUE);
        setRenderActionNewEdit(Boolean.FALSE);
    }

    /**
     * @return the listaPerfiles
     */
    public List<SelectItem> getListaPerfiles() {
        return listaPerfiles;
    }

    /**
     * @param listaPerfiles the listaPerfiles to set
     */
    public void setListaPerfiles(List<SelectItem> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    /**
     * @return the selectedPerfil
     */
    public Integer getSelectedPerfil() {
        return selectedPerfil;
    }

    /**
     * @param selectedPerfil the selectedPerfil to set
     */
    public void setSelectedPerfil(Integer selectedPerfil) {
        this.selectedPerfil = selectedPerfil;
    }

    /**
     * @return the listaEmpresas
     */
    public List<SelectItem> getListaEmpresas() {
        return listaEmpresas;
    }

    /**
     * @param listaEmpresas the listaEmpresas to set
     */
    public void setListaEmpresas(List<SelectItem> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    /**
     * @return the selectedEmpresa
     */
    public Integer getSelectedEmpresa() {
        return selectedEmpresa;
    }

    /**
     * @param selectedEmpresa the selectedEmpresa to set
     */
    public void setSelectedEmpresa(Integer selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the cedula
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * @param cedula the cedula to set
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo the cargo to set
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the rclave
     */
    public String getRclave() {
        return rclave;
    }

    /**
     * @param rclave the rclave to set
     */
    public void setRclave(String rclave) {
        this.rclave = rclave;
    }

    /**
     * @return the editUser
     */
    public boolean isEditUser() {
        return editUser;
    }

    /**
     * @param editUser the editUser to set
     */
    public void setEditUser(boolean editUser) {
        this.editUser = editUser;
    }

    /**
     * @return the nclave
     */
    public String getNclave() {
        return nclave;
    }

    /**
     * @param nclave the nclave to set
     */
    public void setNclave(String nclave) {
        this.nclave = nclave;
    }

    /**
     * @return the rnclave
     */
    public String getRnclave() {
        return rnclave;
    }

    /**
     * @param rnclave the rnclave to set
     */
    public void setRnclave(String rnclave) {
        this.rnclave = rnclave;
    }

    /**
     * @return the renderEmpresa
     */
    public boolean isRenderEmpresa() {
        return renderEmpresa;
    }

    /**
     * @param renderEmpresa the renderEmpresa to set
     */
    public void setRenderEmpresa(boolean renderEmpresa) {
        this.renderEmpresa = renderEmpresa;
    }

    /**
     * @return the renderActionNewEdit
     */
    public boolean isRenderActionNewEdit() {
        return renderActionNewEdit;
    }

    /**
     * @param renderActionNewEdit the renderActionNewEdit to set
     */
    public void setRenderActionNewEdit(boolean renderActionNewEdit) {
        this.renderActionNewEdit = renderActionNewEdit;
    }

    /**
     * @return the renderActionClave
     */
    public boolean isRenderActionClave() {
        return renderActionClave;
    }

    /**
     * @param renderActionClave the renderActionClave to set
     */
    public void setRenderActionClave(boolean renderActionClave) {
        this.renderActionClave = renderActionClave;
    }
}
