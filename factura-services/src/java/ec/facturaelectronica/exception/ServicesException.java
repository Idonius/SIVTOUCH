/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.exception;

/**
 *
 * @author desarrollotic
 */
public class ServicesException extends Exception {

    public ServicesException(String mensaje) {
        super(mensaje);
    }

    public ServicesException(String mensaje, Exception ex) {
        super(mensaje, ex);
    }
}
