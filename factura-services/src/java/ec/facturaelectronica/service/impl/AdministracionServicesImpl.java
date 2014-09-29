/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.dao.MenuDao;
import ec.facturaelectronica.dao.PerfilDao;
import ec.facturaelectronica.dao.TipoComprobanteDao;
import ec.facturaelectronica.dao.UsuarioDao;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.model.TipoComprobante;
import ec.facturaelectronica.model.Usuario;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.util.Util;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author desarrollotic
 */
@Stateless
public class AdministracionServicesImpl implements AdministracionService {

    @EJB
    PerfilDao perfilDao;
    @EJB
    CatalogoDao catalogoDao;
    @EJB
    UsuarioDao usuarioDao;
    @EJB
    EmpresaDao empresaDao;
    @EJB
    MenuDao menDao;
    @EJB
    TipoComprobanteDao tipoComprobanteDao;

    @Override
    public List<Perfil> getPerfiles() throws ServicesException {
        return perfilDao.getPerfiles();
    }

    @Override
    public void guardarPerfil(Perfil perfil) throws ServicesException {
        try {

            Catalogo catalogo;
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Activo.getOrden());
            perfil.setIdEstadoCatalogo(catalogo);
            perfilDao.insert(perfil);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

    }

    @Override
    public void eliminarPerfil(Perfil perfil) throws ServicesException {

        try {

            List<Usuario> usuarios = usuarioDao.getUsuariosxPerfil(perfil);

            if (!usuarios.isEmpty()) {
                throw new ServicesException("Existe Usuarios activos con este perfíl, no se puede eliminar el perfíl seleccionado");
            }

            Catalogo catalogo;
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Desactivado.getOrden());
            perfil.setIdEstadoCatalogo(catalogo);

            perfilDao.update(perfil);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

    }

    @Override
    public void actualizarPerfil(Perfil perfil) throws ServicesException {
        try {
            perfilDao.update(perfil);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public List<Usuario> getUsuariosxPerfil(Perfil perfil) throws ServicesException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuario> getUsuarios() {

        return usuarioDao.getUsuarios();

    }

    @Override
    public void guardarUsuario(Usuario usuario) throws ServicesException {
        try {

            Catalogo catalogo;

            //revisar si existe el nombre
            if (usuarioDao.getUsuarioByNick(usuario.getNickUsuario()) != null) {
                throw new ServicesException("El nick del usuario ya esta registrado");
            }
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Activo.getOrden());
            usuario.setClaveUsuario(Util.md5(usuario.getClaveUsuario()));
            usuario.setIdEstadoCatalogo(catalogo);
            usuarioDao.insert(usuario);

        } catch (Exception ex) {

            throw new ServicesException(ex.getMessage());

        }

    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws ServicesException {

        try {
            usuarioDao.update(usuario);

        } catch (Exception ex) {

            throw new ServicesException(ex.getMessage());

        }
    }

    @Override
    public void eliminarUsuario(Usuario usuario) throws ServicesException {
        Catalogo catalogo;

        try {

            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Desactivado.getOrden());
            usuario.setIdEstadoCatalogo(catalogo);
            usuarioDao.update(usuario);

        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public void resetClaveUsuario(Usuario usuario, String clave) throws ServicesException {
        try {

            //encriptar clave
            String pass = Util.md5(clave);
            usuario.setClaveUsuario(pass);

            usuarioDao.update(usuario);

        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public List<Empresa> getEmpresas() throws ServicesException {
        return empresaDao.getEmpresas();
    }

    @Override
    public void guardarEmpresa(Empresa empresa) throws ServicesException {
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Activo.getOrden());
            empresa.setIdEstadoCatalogo(catalogo);
            empresaDao.insert(empresa);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

    }

    @Override
    public void actualizarEmpresa(Empresa empresa) throws ServicesException {
        try {
            empresaDao.update(empresa);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public void eliminarEmpresa(Empresa empresa) throws ServicesException {
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load((long) EstadosGeneralesEnum.Desactivado.getOrden());
            List<Usuario> listaUsuario = usuarioDao.getUsuarioByEmpresa(empresa);
            if (!listaUsuario.isEmpty()) {
                throw new ServicesException("La empresa tiene usuarios que se encuentran activos, la operación no pudo ser concluida");
            }
            empresa.setIdEstadoCatalogo(catalogo);
            empresaDao.update(empresa);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }
    }

    @Override
    public List<Usuario> getUsuariosByEmpresas(Empresa empresa) throws ServicesException {
        return usuarioDao.getUsuarioByEmpresa(empresa);
    }

    @Override
    public List<Menu> getMenusPadre() throws ServicesException {
        return menDao.getMenusPadres();
    }

    @Override
    public List<Menu> getMenusHijos(Menu padre) throws ServicesException {
        return menDao.getMenusHijas(padre);
    }

    @Override
    public Catalogo getCatalogo(Long numero) throws ServicesException {
        Catalogo catalogo;

        try {
            catalogo = catalogoDao.load(numero);
            return catalogo;

        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage() + " No encontro el catalogo");
        }
    }

    @Override
    public Empresa getEmpresa(String ruc) throws ServicesException {
        Empresa empresa;
        try {
            empresa = empresaDao.getEmpresaByRuc(ruc);
        } catch (Exception ex) {
            throw new ServicesException(ex.getMessage());
        }

        return empresa;
    }

    @Override
    public boolean buscaEmpresaActiva(String ruc) throws ServicesException {
        Empresa empresa;
        boolean val = false;
        try {
            empresa = empresaDao.getEmpresaByRuc(ruc);
            if (empresa != null) {
                val = true;
            } else {
                val = false;
            }
        } catch (Exception ex) {
            throw new ServicesException("La empresa no se encuentra registrada en el sistema", ex);
        }

        return val;
    }

    @Override
    public TipoComprobante buscarTipoComprobante(String codigo) throws ServicesException {
        return tipoComprobanteDao.obtenerTipoComprobante(codigo);
    }

}
