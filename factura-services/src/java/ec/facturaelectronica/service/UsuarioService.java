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
    Usuario getUsuarioPorNick(final String nick);
    Usuario getUsuarioPorCedula(final String cedula);
    Usuario getUsuarioPorToken(final String token);
    void crearUsuario(final Usuario usuario);
    void actualizarUsuario(final Usuario usuario);
}
