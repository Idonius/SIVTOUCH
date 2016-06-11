/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.constantes.FacturaConstantes;
import ec.facturaelectronica.datatable.model.CertificadoDataTableModel;
import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.EmpresaListModel;
import ec.facturaelectronica.model.Catalogo;
import ec.facturaelectronica.model.Certificado;
import ec.facturaelectronica.model.Empresa;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Christian
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
    private String nombreArchivoRealCertificado;
    private Catalogo estado;
    private String claveCertificado;
    private Empresa empresa;
    private List<Empresa> empresas;
    private Empresa selectedEmpresa;
    private Empresa selectedEmpresaEdit;

    private StringBuffer path;
    private String contentType;
    private Certificado cert;
    private CertificadoDataTableModel certificadoModel;
    private int idSelected;
    private String newPass;
    private long estadoSelected;
    private boolean isVisible = false;
    private boolean isSuperAdm = false;

    private Empresa selectedEmpresaBusqueda;

    @PostConstruct
    public void init() {

        try {
            empresas = admService.getEmpresas();
            EmpresaListModel.empresaModel = empresas;

           // if (FacturaConstantes.SUPER_ADMINISTRADOR.equals(loginBean.getUsuarioLogin().getIdPerfil().getNombrePerfil())) {
                certificados = new ArrayList<>();
                isSuperAdm = true;
                if (selectedEmpresaBusqueda != null) {
                    certificados = certificadoService.getCertificadosFiltrados(selectedEmpresaBusqueda);

                    certificadoModel = new CertificadoDataTableModel(certificados);

                }
          //  } else {
//                certificados = certificadoService.getCertificadosFiltrados(loginBean.getUsuarioLogin().getIdEmpresa());
//                certificadoModel = new CertificadoDataTableModel(certificados);
//                selectedEmpresaBusqueda = loginBean.getUsuarioLogin().getIdEmpresa();
//                selectedEmpresaEdit = loginBean.getUsuarioLogin().getIdEmpresa();
//                System.out.println(selectedEmpresaEdit);
//                isSuperAdm = false;
            //}

        } catch (ServicesException ex) {
            errorMessages(recurso.getString("certificados.header"), ex.getMessage(), recurso.getString("editar.mensaje.error"));
        }
    }

    public void seleccionarEmpresa() {

        if (selectedEmpresaBusqueda != null) {
            certificados = certificadoService.getCertificadosFiltrados(selectedEmpresaBusqueda);
            certificadoModel = new CertificadoDataTableModel(certificados);

        } else {
            certificados = new ArrayList<>();
            certificadoModel = new CertificadoDataTableModel();
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
        try {
            copiarArchivo(evt.getFile().getInputstream());
            setNombreArchivoCertificado(evt.getFile().getFileName());

        } catch (FileNotFoundException fne) {
            //LOG.info("Error al subir el archivo", fne);
        } catch (IOException ex) {
            //LOG.info("Error al subir el archivo", ex);
        }
    }

    private void copiarArchivo(final InputStream in) throws FileNotFoundException, IOException {
        //cambiar el nombre al archivo

        Date date = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
        String cadenaFecha = formato.format(date);
        cadenaFecha = cadenaFecha + ".p12";

        OutputStream out = new FileOutputStream(new File(certificado.getString("path.certificados").concat(cadenaFecha)));
        int leer = 0;
        byte[] bytes = new byte[1024];

        while ((leer = in.read(bytes)) != -1) {
            out.write(bytes, 0, leer);
        }

        in.close();
        out.flush();
        out.close();

        setNombreArchivoRealCertificado(cadenaFecha);
        //LOG.info("Nuevo archivo creado...");

    }

    private boolean fechasValidate(final Date inicio, final Date fin) {
        if (inicio != null && fin != null) {
            return fin.compareTo(inicio) > 0 ? true : false;
        }
        return false;
    }

    public void registrar() {
        FacesMessage msg;

        try {

            if (fechasValidate(fechaIngreso, fechaCaducidad)) {
                cert.setNombre(nombreCertificado);
                cert.setFechaIngreso(fechaIngreso);
                cert.setFechaCaducidad(fechaCaducidad);
                cert.setEmpresa(selectedEmpresaEdit);

                if (cert.getIdCertificado() == null) {
                    cert.setNombreArchivoCertificado(getNombreArchivoRealCertificado());
                    cert.setClaveCertificado(Util.Encriptar(claveCertificado));

                    String fileP12 = certificado.getString("path.certificados").concat(getNombreArchivoRealCertificado());
                    System.out.println(fileP12);
                    if (certificadoService.validarP12(fileP12, claveCertificado)) {
                         certificadoService.registrarCertificado(cert);
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("certificados.header"), recurso.getString("editar.mensaje"));
                        FacesContext.getCurrentInstance().addMessage("frmCertificadosId:growl", msg);
                        RequestContext.getCurrentInstance().update("frmCertificadosId:growl");
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').hide()");
                        init();
                    } else {
                        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, recurso.getString("certificados.header"), recurso.getString("certificado.editar.mensaje.error.clave"));
                        FacesContext.getCurrentInstance().addMessage("frmCertificadosId:growl", msg);
                        RequestContext.getCurrentInstance().update("frmCertificadosId:growl");
                    }

                } else {
                    certificadoService.actualizarCertificado(cert);
                }

            } else {

                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, recurso.getString("certificados.header"), recurso.getString("certificado.editar.mensaje.error.fecha"));
                FacesContext.getCurrentInstance().addMessage("frmCertificadosId:growl", msg);
                RequestContext.getCurrentInstance().update("frmCertificadosId:growl");
            }
        } catch (Exception ex) {
            //LOG.error("Error al registrar el certificado ", ex);
//            e.printStackTrace();
        }
    }

    public void resetearClave(final ActionEvent evt) {
        RequestContext.getCurrentInstance().execute("PF('dlgCambiarClave').show()");
    }

    public void cambiar(final ActionEvent evt) {
        this.estadoSelected = -1L;
        RequestContext.getCurrentInstance().execute("PF('dlgModificarEstado').show()");
    }

    public void nuevaClave() {
        Certificado certificado = getSelectedCertificado();
        if (certificado != null) {
            try {
                certificado.setClaveCertificado(Util.Encriptar(getNewPass()));
                certificadoService.actualizarCertificado(certificado);
                infoMessages(recurso.getString("certificados.header"), recurso.getString("certificados.mensaje.cambio.clave"), ":frmCertificadosId:growl");
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
        init();
        if (estadoSelected == 2) {
            infoMessages(recurso.getString("certificados.header"), recurso.getString("certificados.mensaje"), "frmCertificadosId:growl");
        }
        RequestContext.getCurrentInstance().execute("PF('dlgModificarEstado').hide()");
    }

    private void limpiar() {
        this.nombreCertificado = null;
        this.fechaIngreso = null;
        this.fechaCaducidad = null;
        this.claveCertificado = null;
        this.selectedEmpresa = null;
        //this.selectedEmpresaEdit = null;
    }

    public void cancelar() {
        RequestContext.getCurrentInstance().execute("PF('dlgEditarCertificado').hide()");
    }

    public void nuevo() {
        limpiar();
        setIsVisible(true);
        cert = new Certificado();
        setFechaIngreso(new Date());
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
            this.selectedEmpresaEdit = cert.getEmpresa();
            setIsVisible(false);
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

    public boolean isIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * @return the nombreArchivoRealCertificado
     */
    public String getNombreArchivoRealCertificado() {
        return nombreArchivoRealCertificado;
    }

    /**
     * @param nombreArchivoRealCertificado the nombreArchivoRealCertificado to
     * set
     */
    public void setNombreArchivoRealCertificado(String nombreArchivoRealCertificado) {
        this.nombreArchivoRealCertificado = nombreArchivoRealCertificado;
    }

    /**
     * @return the selectedEmpresaBusqueda
     */
    public Empresa getSelectedEmpresaBusqueda() {
        return selectedEmpresaBusqueda;
    }

    /**
     * @param selectedEmpresaBusqueda the selectedEmpresaBusqueda to set
     */
    public void setSelectedEmpresaBusqueda(Empresa selectedEmpresaBusqueda) {
        this.selectedEmpresaBusqueda = selectedEmpresaBusqueda;
    }

    /**
     * @return the isSuperAdm
     */
    public boolean isIsSuperAdm() {
        return isSuperAdm;
    }

    /**
     * @param isSuperAdm the isSuperAdm to set
     */
    public void setIsSuperAdm(boolean isSuperAdm) {
        this.isSuperAdm = isSuperAdm;
    }

    /**
     * @return the selectedEmpresaEdit
     */
    public Empresa getSelectedEmpresaEdit() {
        return selectedEmpresaEdit;
    }

    /**
     * @param selectedEmpresaEdit the selectedEmpresaEdit to set
     */
    public void setSelectedEmpresaEdit(Empresa selectedEmpresaEdit) {
        this.selectedEmpresaEdit = selectedEmpresaEdit;
    }

}
