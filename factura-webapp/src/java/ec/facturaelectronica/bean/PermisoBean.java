/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.bean;

import ec.facturaelectronica.exception.ServicesException;
import ec.facturaelectronica.list.model.PerfilListModel;
import ec.facturaelectronica.model.Menu;
import ec.facturaelectronica.model.Perfil;
import ec.facturaelectronica.service.AdministracionService;
import ec.facturaelectronica.service.SeguridadService;
import ec.facturaelectronica.util.RecursosServices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author desarrollotic
 */
@ManagedBean(name = "permisoBean")
@ViewScoped
public class PermisoBean extends RecursosServices implements Serializable {

    @EJB
    SeguridadService seguridadService;
    @EJB
    AdministracionService admService;
    private List<Perfil> listaPerfiles;
    private Perfil selectedPerfil;
    private TreeNode root;
    private TreeNode selectedNode;
    private TreeNode rootP;
    private TreeNode selectedNodeP;
    private boolean visiblePnl;

    public PermisoBean() {
    }

    public boolean isVisiblePnl() {
        return visiblePnl;
    }

    public TreeNode getRootP() {
        return rootP;
    }

    public void setRootP(TreeNode rootP) {
        this.rootP = rootP;
    }

    public TreeNode getSelectedNodeP() {
        return selectedNodeP;
    }

    public void setSelectedNodeP(TreeNode selectedNodeP) {
        this.selectedNodeP = selectedNodeP;
    }

    public void setVisiblePnl(boolean visiblePnl) {
        this.visiblePnl = visiblePnl;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(List<Perfil> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public Perfil getSelectedPerfil() {
        return selectedPerfil;
    }

    public void setSelectedPerfil(Perfil selectedPerfil) {
        this.selectedPerfil = selectedPerfil;
    }

    @PostConstruct
    public void initBean() {
        FacesMessage msg;
        try {

            root = new DefaultTreeNode("root", null);
            rootP = new DefaultTreeNode("rootP", null);
            listaPerfiles = admService.getPerfiles();
            PerfilListModel.perfilModel = listaPerfiles;

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("permisos.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void buscarPermisos() {

        if (selectedPerfil != null) {
            visiblePnl = true;
            armaMenuPermisos();
            armarMenuxPermiso();
        }
    }

    private void armarMenuxPermiso() {
        FacesMessage msg;
        List<Menu> menu;
        try {
            menu = seguridadService.getMenuByPerfil(null, selectedPerfil);
            rootP = new DefaultTreeNode("rootP", null);

            for (Menu m : menu) {
                TreeNode documents = new DefaultTreeNode(m, rootP);
                armamenuHijoxPermiso(documents, m);
            }

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("permisos.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    private void armamenuHijoxPermiso(TreeNode node, Menu m) {
        FacesMessage msg;
        List<Menu> menu;

        try {
            menu = seguridadService.getMenuByPerfil(m, selectedPerfil);

            for (Menu me : menu) {
                // armaMenuHtml(m);
                TreeNode documents = new DefaultTreeNode(me, node);
                armamenuHijoxPermiso(documents, me);
            }

        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("permisos.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    private void armaMenuPermisos() {
        root = new DefaultTreeNode("root", null);

        FacesMessage msg;
        try {

            List<Menu> listaMenus = admService.getMenusPadre();
            for (Menu m : listaMenus) {
                TreeNode documents = new DefaultTreeNode(m, root);
                armaMenuHijos(documents, m);
            }
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("permisos.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    private void armaMenuHijos(TreeNode node, Menu menu) {
        FacesMessage msg;
        try {

            List<Menu> listaMenus = admService.getMenusHijos(menu);

            for (Menu me : listaMenus) {
                TreeNode documents = new DefaultTreeNode(me, node);
                armaMenuHijos(documents, me);

            }
        } catch (ServicesException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, recurso.getString("permisos.header"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void asignarMenuPerfil() {
        FacesMessage msg;

        Menu menu;
        if (selectedNode != null) {
            menu = (Menu) selectedNode.getData();
            if (menu.getUrlMenu() != null || menu.getMenIdMenu()==null) {
                try {
                    if (!seguridadService.buscarMenuxPerfil(menu, selectedPerfil)) {
                        seguridadService.asignarPermiso(menu, selectedPerfil);
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("permisos.header"), "Permiso registrado satisfactoriamente");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        RequestContext.getCurrentInstance().update("form:growl");
                        armarMenuxPermiso();
                    } else {
                        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("permisos.header"), "Ya se encuentra asignado el permiso");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        RequestContext.getCurrentInstance().update("form:growl");
                    }
                } catch (ServicesException ex) {
                }

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("permisos.header"), "Debe seleccionar un módulo que contenga la actividad");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        }
    }

    public void eliminarPermiso() {
        Menu menu;
        FacesMessage msg;

        if (selectedNodeP != null) {
            menu = (Menu) selectedNodeP.getData();

            try {
                seguridadService.eliminarPermiso(menu, selectedPerfil);

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, recurso.getString("permisos.header"), "Permiso eliminado satisfactoriamente");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                armarMenuxPermiso();

            } catch (ServicesException ex) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, recurso.getString("permisos.header"), "Debe seleccionar un módulo que contenga la actividad. "+ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

//           
        }
    }
}
