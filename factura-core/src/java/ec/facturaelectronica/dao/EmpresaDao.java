/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao;


import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Plan;
import ec.facturaelectronica.model.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author desarrollotic
 */
@Local
public interface EmpresaDao extends GenericDao<Empresa, Integer>{
    public List<Empresa> getEmpresas();
    public List<Empresa> getEmpresasMinusUsuario(Usuario usuario);
    public List<Empresa> getEmpresaPorPlan(Plan plan);
}
