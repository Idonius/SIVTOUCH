/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.CobroPago;
import ec.facturaelectronica.model.Pago;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Christian
 */
@Local
public interface CobroPagoService {
    List<Pago> obtenerPagosGeneradosService() throws ServicesException;
    void guardarCobroPago(final CobroPago cobroPago) throws ServicesException;
    List<Catalogo> obtenerCatalogosPorGrupoTransacciones() throws ServicesException;
}
