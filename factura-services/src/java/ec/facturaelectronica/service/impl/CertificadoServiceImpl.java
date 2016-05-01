/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.service.impl;

import ec.facturaelectronica.dao.CatalogoDao;
import ec.facturaelectronica.dao.CertificadoDao;
import ec.facturaelectronica.dao.EmpresaDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.util.PassStoreKSGenerica;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoServiceImpl implements CertificadoService {

    @EJB
    private CertificadoDao certificadoDao;

    @EJB
    private CatalogoDao catalogoDao;

    @EJB
    private EmpresaDao empresaDao;

    @Override
    public List<Certificado> getTodosLosCertificados() {
        return certificadoDao.obtenerTodosLosCertificados();
    }

    @Override
    public boolean registrarCertificado(final Certificado certificado) {
        boolean result = Boolean.FALSE;
        Catalogo catalogo;

        catalogo = catalogoDao.load(EstadosGeneralesEnum.Activo.getOrden());

        if (certificado != null) {
            certificado.setEstado(catalogo);
            certificadoDao.insert(certificado);
            result = Boolean.TRUE;
        }
        return result;
    }

    @Override
    public boolean actualizarCertificado(final Certificado certificado) {
        boolean result = Boolean.FALSE;
        if (certificado != null) {
            certificadoDao.update(certificado);
            result = Boolean.TRUE;
        }
        return result;
    }

    @Override
    public Catalogo getCatalogoPorId(Long idCatalogo) {
        return catalogoDao.load(idCatalogo);
    }

    @Override
    public List<Certificado> getCertificadosFiltrados(Empresa empresa) {
        List<Certificado> result;
        Catalogo estado = catalogoDao.load(EstadosGeneralesEnum.Activo.getOrden());

        result = certificadoDao.obtenerCertificadosFiltrados(empresa, estado);

        return result;
    }

    @Override
    public Empresa getEmpresaPorId(int idEmpresa) {
        return empresaDao.load(idEmpresa);
    }

    @Override
    public Certificado getCertificadoPorId(Long id) {
        return certificadoDao.load(id);
    }

    @Override
    public boolean validarP12(String filenameP12, String clave) {

        IPKStoreManager storeManager = getPKStoreManager(filenameP12, clave);
        if (storeManager == null) {
            // log.error("El gestor de claves no se ha obtenido correctamente.");
            return false;
        }

        return true;

    }

    private IPKStoreManager getPKStoreManager(String archivoP12, String clave) {
        IPKStoreManager storeManager = null;

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");

            java.io.FileInputStream fis = null;
            try {
                fis = new java.io.FileInputStream(archivoP12);
                ks.load(fis, clave.toCharArray());
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            storeManager = new KSStore(ks, new PassStoreKSGenerica(clave));
        } catch (KeyStoreException ex) {
            //    log.error("KeyStoreException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (NoSuchAlgorithmException ex) {
            //  log.error("NoSuchAlgorithmException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (CertificateException ex) {
            //log.error("CertificateException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (IOException ex) {
            //log.error("IOException: No se puede generar KeyStore PKCS12");
            //System.exit(-1);
        }
        return storeManager;
    }

}
