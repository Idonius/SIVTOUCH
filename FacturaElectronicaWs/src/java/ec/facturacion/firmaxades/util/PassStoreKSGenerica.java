/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.firmaxades.util;

import java.security.cert.X509Certificate;

import es.mityc.javasign.pkstore.IPassStoreKS;

/**
 * <p>Permite automatizar el acceso a las contrasenas de los almacenes de certificados de testeo.</p>
 * 
 * @author  Ministerio de Industria, Turismo y Comercio
 * @version 1.0
 */ 
public class PassStoreKSGenerica implements IPassStoreKS { 
	
	/** Contrasena de acceso al almacen. */
	private transient String password;
	
	/**
	 * <p>Crea una instancia con la contrasena que se utilizara con el almacen relacionado.</p>
	 * @param pass Contrase�a del almacen
	 */
	public PassStoreKSGenerica(final String pass) {
		this.password = new String(pass);
	}

	/**
	 * <p>Devuelve la contrasena configurada para este almacen.</p>
	 * @param certificate No se utiliza
	 * @param alias no se utiliza
	 * @return contrasena configurada para este almacen
	 * @see es.mityc.javasign.pkstore.IPassStoreKS#getPassword(java.security.cert.X509Certificate, java.lang.String)
	 */
	public char[] getPassword(final X509Certificate certificate, final String alias) {
		return password.toCharArray();
	}

}
