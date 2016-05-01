/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.vo;

/**
 *
 * @author Christian
 */
public class PerfilVo {
    
    private Boolean renderEdit;
    private String nombre;

    
    public void activarEdicion(){
        renderEdit=Boolean.FALSE;
        
    }
    
    public void desactivarEdicion(){
    renderEdit=Boolean.TRUE;
    }
    /**
     * @return the renderEdit
     */
    public Boolean getRenderEdit() {
        return renderEdit;
    }

    /**
     * @param renderEdit the renderEdit to set
     */
    public void setRenderEdit(Boolean renderEdit) {
        this.renderEdit = renderEdit;
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
}
