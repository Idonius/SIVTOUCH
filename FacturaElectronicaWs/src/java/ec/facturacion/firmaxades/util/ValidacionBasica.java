
package ec.facturacion.firmaxades.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.ValidarFirmaXML;

/**
 * Clase de ejemplo para realizar la validacion basica (es decir, sin comprobar
 * la cadena de confianza ni ninguna politica) de una firma XAdES utilizando la libreria XADES
 *  
 * @author Gabriel Eguiguren
 * 
 */
public class ValidacionBasica {

	private static final Logger LOG = Logger.getLogger(ValidacionBasica.class);

	private final static String ARCHIVO_XADES_VALIDO = "/factura-XAdES-BES.xml";

	// constructor por defecto
	public ValidacionBasica() {
	}

	/**
	 * Punto de entrada al programa para demo
	 * 
	 * @param args
	 *            Argumentos del programa
	 */
	public static void main(String[] args) {
		ValidacionBasica validador = new ValidacionBasica();

		if (validador.validarFichero(ValidacionBasica.class.getResourceAsStream(ARCHIVO_XADES_VALIDO))) {
			System.out.println("valido");
		}
	}

	/**
	 * @param archivo
	 * @return
	 */
	public boolean validarArchivo(File archivo) {
		ValidacionBasica validador = new ValidacionBasica();
		boolean esValido = false;

		try {
			InputStream is = new FileInputStream(archivo);
			esValido = validador.validarFichero(is);
		} catch (FileNotFoundException e) {
			LOG.error(e);
		}
		return esValido;
	}

	/**
	 * Metodo que realiza la validacion de firma digital XAdES a un fichero y muestra el resultado
	 * @param archivo
	 *            Archivo a validar
	 */
	public boolean validarFichero(InputStream archivo) {

		boolean esValido = true;

		// Se declara la estructura de datos que almacenar� el resultado de la validaci�n
		ArrayList<ResultadoValidacion> results = null;

		// Se convierte el InputStream a Document
		Document doc = parseaDoc(archivo);

		if (doc != null) {

			// Se instancia el validador y se realiza la validacion
			try {
				ValidarFirmaXML vXml = new ValidarFirmaXML();
				results = vXml.validar(doc, "./", null);
			} catch (Exception e) {
				LOG.error(e);
			}

			// Se muestra por consola el resultado de la validacion
			ResultadoValidacion result = null;
			Iterator<ResultadoValidacion> it = results.iterator();
			while (it.hasNext()) {
				result = it.next();
				esValido = result.isValidate();
				
				 if(esValido){
		                
		                System.out.println("La firma es valida.\n" + result.getNivelValido() 
		                        + "\nFirmado el: " + result.getDatosFirma().getFechaFirma()
		                        + "\nNodos firmados: " + result.getFirmados());
		            } else {
		                System.out.println("La firma NO es valida\n" + result.getLog());
		            }

			}
		}
		return esValido;
	}

	/**
	 * Parsea un documento XML y lo introduce en un DOM.
	 * 
	 * @param uriFirma
	 *            URI al fichero XML
	 * @return Docuemnto parseado
	 */
	private Document parseaDoc(InputStream fichero) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			LOG.error("Error interno al parsear la firma", ex);
			return null;
		}

		Document doc = null;
		try {
			doc = db.parse(fichero);
			return doc;
		} catch (SAXException ex) {
			doc = null;
		} catch (IOException ex) {
			LOG.error("Error interno al validar firma", ex);
		} finally {
			dbf = null;
			db = null;
		}

		return null;
	}
}