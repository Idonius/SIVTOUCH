/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;


import ec.facturaelectronica.datatable.model.ComprobanteDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.service.FacturaService;
import ec.facturaelectronica.util.RecursosServices;
import ec.facturaelectronica.vo.ComprobanteVo;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Christian
 */
@ManagedBean(name = "comprobantesBean")
@ViewScoped
public class ComprobantesBean extends RecursosServices implements Serializable {

    @EJB
    FacturaService facturaService;

    @ManagedProperty("#{loginAccessBean}")
    private LoginAccessBean login;

    private ComprobanteVo comprobanteVo;

    private DefaultStreamedContent descarga;

    public ComprobantesBean() {
        comprobanteVo = new ComprobanteVo();
    }

    @PostConstruct
    public void initBean() {

        try {
            comprobanteVo.setDesde(new Date());
            comprobanteVo.setHasta(new Date());
            comprobanteVo.setListComprobantes(facturaService.getComprobantesByEmpresa(login.getUsuarioLogin().getIdEmpresa(), comprobanteVo.getDesde(), comprobanteVo.getHasta()));
            comprobanteVo.setModelComprobante(new ComprobanteDataTableModel(comprobanteVo.getListComprobantes()));
//            service.init(login.getUsuarioLogin().getIdEmpresa());
//            listComprobantes = service.getListComprobantes();
        } catch (ServicesException ex) {

            LOG.info(ex);

        }
    }

    public void buscarComprobantes() {
        try {
            comprobanteVo.setListComprobantes(facturaService.getComprobantesByEmpresa(login.getUsuarioLogin().getIdEmpresa(), comprobanteVo.getDesde(), comprobanteVo.getHasta()));
            comprobanteVo.setModelComprobante(new ComprobanteDataTableModel(comprobanteVo.getListComprobantes()));
            LOG.info("Ok");

        } catch (ServicesException ex) {

            LOG.info(ex);

        }
    }
    
    public void verNovedades(){
      
        
          RequestContext.getCurrentInstance().execute("PF('dlgNov').show()");
    
    }

    public void prepDownload() throws Exception {
        File file = new File("D:\\comprobantes\\autorizado.xml");
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDescarga(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
      
    }

    /**
     * @return the comprobanteVo
     */
    public ComprobanteVo getComprobanteVo() {
        return comprobanteVo;
    }

    /**
     * @param comprobanteVo the comprobanteVo to set
     */
    public void setComprobanteVo(ComprobanteVo comprobanteVo) {
        this.comprobanteVo = comprobanteVo;
    }

    /**
     * @return the login
     */
    public LoginAccessBean getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(LoginAccessBean login) {
        this.login = login;
    }

    /**
     * @return the descarga
     */
    public DefaultStreamedContent getDescarga() {
        return descarga;
    }

    /**
     * @param descarga the descarga to set
     */
    public void setDescarga(DefaultStreamedContent descarga) {
        this.descarga = descarga;
    }

}
