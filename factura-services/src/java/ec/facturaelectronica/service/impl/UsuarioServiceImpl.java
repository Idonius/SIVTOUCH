/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.UsuarioDao;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.service.UsuarioService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class UsuarioServiceImpl implements UsuarioService{

    @EJB
    private UsuarioDao usuarioDao;
    
    @Override
    public Usuario getUsuarioPorCedula(final String cedula) {
        return usuarioDao.getUsuarioByCedula(cedula);
    }
    
}
