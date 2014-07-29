/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.model.enumtype;

/**
 *
 * @author Armando
 */
public enum TipoComprobanteEnum {
    Facturas(1), NotaCredito(2), NotaDebito(3), Retenciones(4), GuiasDeRemision(5);

    private int value;
    
    private TipoComprobanteEnum(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
