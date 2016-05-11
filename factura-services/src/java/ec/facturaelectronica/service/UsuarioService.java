/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service;

import ec.facturaelectronica.model.Usuario;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface UsuarioService {
    Usuario getUsuarioPorCedula(final String cedula);
}
