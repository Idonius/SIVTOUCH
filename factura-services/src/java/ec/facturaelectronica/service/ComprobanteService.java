/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Comprobante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface ComprobanteService {
    List<Comprobante> obtenerComprobantePorIdentificadorComprobanteList(final String identificador) throws ServicesException;
}
