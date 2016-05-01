/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import ec.facturaelectronica.model.Empresa;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface FacturaService {
    public List<Comprobante> getComprobantesByEmpresa(Empresa empresa,Date desde,Date hasta) throws ServicesException;
    
    public void guardarComprobanteWS(Comprobante comprobante) throws ServicesException;
    
    public void actualizarComprobanteWS(Comprobante comprobante) throws ServicesException;
    
}
