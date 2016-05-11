/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service;

import ec.facturaelectronica.exception.ServicesException;
import javax.ejb.Local;

/**
 *
 * @author Armando
 */
@Local
public interface RegistroService {
    void registrarPerfilUsuario() throws ServicesException;
}
