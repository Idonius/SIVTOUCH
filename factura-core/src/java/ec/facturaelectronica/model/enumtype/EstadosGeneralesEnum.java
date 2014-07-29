/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.model.enumtype;

/**
 *
 * @author desarrollotic
 */
public enum EstadosGeneralesEnum {

    Activo(1L), Desactivado(2L), Eliminado(3L);
    private Long orden;

    private EstadosGeneralesEnum(Long orden) {
        this.orden = orden;
    }

    public Long getOrden() {
        return orden;
    }
}
