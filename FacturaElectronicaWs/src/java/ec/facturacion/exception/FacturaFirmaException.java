/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.exception;

/**
 *
 * @author Christian
 */
public class FacturaFirmaException extends Exception {

    public FacturaFirmaException(String mensaje) {
        super(mensaje);
    }

    public FacturaFirmaException(String mensaje, Exception ex) {
        super(mensaje, ex);
    }
}
