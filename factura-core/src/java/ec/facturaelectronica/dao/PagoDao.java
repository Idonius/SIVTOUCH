/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.facturaelectronica.dao;

import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Pago;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface PagoDao extends GenericDao<Pago, Integer>{
    List<Pago> obtenerTodosLosPagos();
    List<Pago> obtenerPagosPorEmpresaPorMes(final Empresa empresa, final int mes);
}
