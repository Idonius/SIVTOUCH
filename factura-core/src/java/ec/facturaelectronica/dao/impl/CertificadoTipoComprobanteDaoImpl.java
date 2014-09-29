/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.dao.impl;

import ec.facturaelectronica.dao.CertificadoTipoComprobanteDao;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.CertificadoTipoComprobante;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.TipoComprobante;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Armando
 */
@Stateless
public class CertificadoTipoComprobanteDaoImpl extends GenericDaoImpl<CertificadoTipoComprobante, Integer> implements CertificadoTipoComprobanteDao, Serializable {

    public CertificadoTipoComprobanteDaoImpl() {
        super(CertificadoTipoComprobante.class);
    }

    @Override
    public List<CertificadoTipoComprobante> obtenerCertificadoTipoComprobanteList(Catalogo estado, Empresa empresa) {
        List<CertificadoTipoComprobante> result;

        Query qry = em.createNamedQuery("CertificadoTipoComprobante.findEmpresa");
        qry.setParameter("catalogo", estado);
        qry.setParameter("empresa", empresa);

        result = qry.getResultList();

        return result;
    }

    @Override
    public List<CertificadoTipoComprobante> obtenerCertificadoPorEmpresaYTipoComprobante(Catalogo estado, Empresa empresa, TipoComprobante tipoComprobante) {
        List<CertificadoTipoComprobante> result;

        Query qry = em.createNamedQuery("CertificadoTipoComprobante.findEmpresaTipo");
        qry.setParameter("catalogo", estado);
        qry.setParameter("empresa", empresa);
        qry.setParameter("tipoComprobante", tipoComprobante);

        result = qry.getResultList();

        return result;
    }

}
