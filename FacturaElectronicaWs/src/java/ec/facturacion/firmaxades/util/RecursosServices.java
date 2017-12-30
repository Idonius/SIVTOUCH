/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.firmaxades.util;

import java.io.FileInputStream;
import java.util.Properties;


/**
 *
 * @author desarrollotic
 */
public class RecursosServices {
    
    public Properties recurso; //= ResourceBundle.getBundle("ec.facturaelectronica.resources.etiquetas");
   
    
    
    public RecursosServices() {
        
        String strRecurso = System.getProperty("property.config.certificados");
      
        recurso = new Properties();
       
        
        try {
            FileInputStream fisRecurso = new FileInputStream(strRecurso);
            
            recurso.load(fisRecurso);
            
        } catch (Exception ex) {
            
            
        }
        
    }
    
    public Properties getRecurso() {
        return recurso;
    }
    

    
    public void setRecurso(Properties recurso) {
        this.recurso = recurso;
    }
    
   
}
