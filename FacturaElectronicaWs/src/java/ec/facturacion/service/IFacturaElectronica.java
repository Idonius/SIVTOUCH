/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.service;

import ec.facturacion.app.ComprobanteEnviado;
import ec.facturacion.exception.FacturaFirmaException;
import ec.facturacionelectronica.aut.Autorizacion;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface IFacturaElectronica {

    public ComprobanteEnviado firmarEnviarAutorizar(byte[] archivoAFirmar,String email,String codigoEmpresa) throws Exception;

    public Autorizacion consultaAutorizacion(String claveDeAcceso) throws FacturaFirmaException;
    
    public boolean validarP12(String filenameP12,String clave);
    
    public ComprobanteEnviado generarAutorizacion(String claveDeAcceso, int ambiente, int numeroIntentos);
}