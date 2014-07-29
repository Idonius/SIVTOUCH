/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.datatable.model.CertificadoDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.EmpresaListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.Empresa;
import ec.facturaelectronica.model.enumtype.EstadosGeneralesEnum;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.CertificadoService;
import ec.facturaelectronica.service.util.Util;
import ec.facturaelectronica.util.RecursosServices;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Armando
 */
@ManagedBean
@ViewScoped
public class CertificadoBean extends RecursosServices implements Serializable {

    private final String MIME_TYPE = "application/x-pkcs12";

    @EJB
    private CertificadoService certificadoService;

    @EJB
    private AdministracionService admService;

    @ManagedProperty(value = "#{loginAccessBean}")
    private LoginAccessBean loginBean;

    private final static Logger LOGGER = Logger.getLogger(CertificadoBean.class.getName());

    private List<Certificado> certificados;
    private Certificado selectedCertificado;
    private List<Certificado> filterCertificados;
    private String nombreCertificado;
    private Date fechaIngreso;
    private Date fechaCaducidad;
    private String nombreArchivoCertificado;
    private Catalogo estado;
    private String claveCertificado;
    private Empresa empresa;
    private List<Empresa> empresas;
    private Empresa selectedEmpresa;
    private StringBuffer path;
    private String contentType;
    private Certificado cert;
    private CertificadoDataTableModel certificadoModel;
    private int idSelected;
    private String newPass;
    private long estadoSelected;

    @PostConstruct
    public void init() {
        limpiar();
        certificados = certificadoService.getCertificadosFiltrados();

        try {
            empresas = admService.getEmpresas();
            certificadoModel = new CertificadoDataTableModel(certificados);
            EmpresaListModel.empresaModel = empresas;
        } catch (ServicesException ex) {
            Logger.getLogger(CertificadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void download(Certificado selected) {
        path = new StringBuffer(certificado.getString("path.certificados"));
        setSelectedCertificado(selected);
        path.append(selectedCertificado.getNombreArchivoCertificado());
        contentType = MIME_TYPE;
    }

    public StreamedContent getFile() throws IOException {
        return new DefaultStreamedContent(new FileInputStream(path.toString()), contentType, getSelectedCertificado().getNombreArchivoCertificado());
    }

    public void upload(final FileUploadEvent evt) {
        FacesMessage msg = new FacesMessage("Exito!! ", evt.getFile().getFileName().concat("subido exitosamente"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            copiarArchivo(evt.getFile().getFileName(), evt.getFile().getInputstream());
            setNombreArchivoCertificado(evt.getFile().getFileName());
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void copiarArchivo(final String nombreArchivo, final InputStream in) throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(new File(certificado.getString("path.certificados").concat(nombreArchivo)));
        int leer = 0;
        byte[] bytes = new byte[1024];

        while ((leer = in.read(bytes)) != -1) {
            out.write(bytes, 0, leer);
        }

        in.close();
        out.flush();
        out.close();

        LOG.info("Nuevo archivo creado...");
    }

    public String getNombreEmpresa() {
        String result = "";
        int empresaId = loginBean.getUsuarioLogin().getIdEmpresa().getIdEmpresa();
        Empresa empresa = certificadoService.getEmpresaPorId(empresaId);
        if (empresa != null) {
            result = empresa.getNombreEmpresa();
        }
        return result;
    }

    private boolean fechasValidate(final Date inicio, final Date fin) {
        if (inicio != null && fin != null) {
            return fin.compareTo(inicio) > 0 ? true : false;
        }
        return false;
    }

    public void registrar() {
        FacesMessage msg;
        Catalogo catalogo = new Catalogo();
        try {
            if (cert == null) {
                cert = new Certificado();
                cert.setNombreArchivoCertificado(getNombreArchivoCertificado());
            }else{
                Certificado oldCertificado = certificadoService.getCertificadoPorId(cert.getId());
                cert.setNombreArchivoCertificado(oldCertificado.getNombreArchivoCertificado());
            }
            if (fechasValidate(fechaIngreso, fechaCaducidad)) {
                cert.setNombre(nombreCertificado);
                cert.setFechaIngreso(fechaIngreso);
                cert.setFechaCaducidad(fechaCaducidad);
                catalogo.setIdCatalogo(EstadosGeneralesEnum.Activo.getOrden());                
                cert.setEstado(catalogo);
                cert.setClaveCertificado(Util.md5(claveCertificado));
                cert.setEmpresa(selectedEmpresa);
                
                if (cert.getId() == null) {
                    certificadoService.registrarCertificado(cert);
                } else {
                    certificadoService.actualizarCertificado(cert);
                }
                
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificados.header"), recurso.getString("editar.mensaje"));
                FacesContext.getCurrentInstance().addMessage("frmCertificadosId:growl", msg);
                RequestContext.getCurrentInstance().update("frmCertificadosId:growl");                
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, recurso.getString("certificados.header"), recurso.getString("editar.mensaje.error"));
                FacesContext.getCurrentInstance().addMessage("frmCertificadosId:growl", msg);
                RequestContext.getCurrentInstance().update("frmCertificadosId:growl");                                
            }
            init();
            RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').hide()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiar(final Certificado certificado) {
        if (certificado != null) {
            setSelectedCertificado(certificado);
        }
    }

    public void nuevaClave() {
        Certificado certificado = getSelectedCertificado();
        if (certificado != null) {
            try {
                certificado.setClaveCertificado(Util.md5(getNewPass()));
                certificadoService.actualizarCertificado(certificado);
            } catch (Exception ex) {
                Logger.getLogger(CertificadoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        RequestContext.getCurrentInstance().execute("PF('dlgCambiarClave').hide()");
    }

    public void nuevoEstado() {
        Certificado certificado = getSelectedCertificado();
        Catalogo catalogo = certificadoService.getCatalogoPorId(estadoSelected);
        if (certificado != null) {
            certificado.setEstado(catalogo);
            certificadoService.actualizarCertificado(certificado);
        }
        certificados = certificadoService.getCertificadosFiltrados();
        RequestContext.getCurrentInstance().execute("PF('dlgModificarEstado').hide()");
    }

    private void limpiar() {
        this.nombreCertificado = "";
        this.fechaIngreso = null;
        this.fechaCaducidad = null;
        this.claveCertificado = "";
        this.selectedEmpresa = null;
    }

    public void cancelar() {
        RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').hide()");
    }

    public void nuevo() {
//        limpiar();
        RequestContext ctx = RequestContext.getCurrentInstance();
        ctx.reset(":formDialogEditarId:gridEditar");
        RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').show()");
    }

    public void editar() {
        if (selectedCertificado != null) {
            cert = selectedCertificado;
            this.nombreCertificado = cert.getNombre();
            this.fechaIngreso = cert.getFechaIngreso();
            this.fechaCaducidad = cert.getFechaCaducidad();
            this.claveCertificado = cert.getClaveCertificado();
            this.selectedEmpresa = cert.getEmpresa();
            RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').show()");
        }
    }

    //Getters & Setters
    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public Certificado getSelectedCertificado() {
        return selectedCertificado;
    }

    public void setSelectedCertificado(Certificado selectedCertificado) {
        this.selectedCertificado = selectedCertificado;
    }

    public List<Certificado> getFilterCertificados() {
        return filterCertificados;
    }

    public void setFilterCertificados(List<Certificado> filterCertificados) {
        this.filterCertificados = filterCertificados;
    }

    public String getNombreCertificado() {
        return nombreCertificado;
    }

    public void setNombreCertificado(String nombreCertificado) {
        this.nombreCertificado = nombreCertificado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getNombreArchivoCertificado() {
        return nombreArchivoCertificado;
    }

    public void setNombreArchivoCertificado(String nombreArchivoCertificado) {
        this.nombreArchivoCertificado = nombreArchivoCertificado;
    }

    public Catalogo getEstado() {
        return estado;
    }

    public void setEstado(Catalogo estado) {
        this.estado = estado;
    }

    public String getClaveCertificado() {
        return claveCertificado;
    }

    public void setClaveCertificado(String claveCertificado) {
        this.claveCertificado = claveCertificado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Empresa getSelectedEmpresa() {
        return selectedEmpresa;
    }

    public void setSelectedEmpresa(Empresa selectedEmpresa) {
        this.selectedEmpresa = selectedEmpresa;
    }

    public CertificadoDataTableModel getCertificadoModel() {
        return certificadoModel;
    }

    public void setCertificadoModel(CertificadoDataTableModel certificadoModel) {
        this.certificadoModel = certificadoModel;
    }

    public Certificado getCert() {
        return cert;
    }

    public void setCert(Certificado cert) {
        this.cert = cert;
    }

    public int getIdSelected() {
        return idSelected;
    }

    public void setIdSelected(int idSelected) {
        this.idSelected = idSelected;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public long getEstadoSelected() {
        return estadoSelected;
    }

    public void setEstadoSelected(long estadoSelected) {
        this.estadoSelected = estadoSelected;
    }

    public void setLoginBean(LoginAccessBean loginBean) {
        this.loginBean = loginBean;
    }

}
