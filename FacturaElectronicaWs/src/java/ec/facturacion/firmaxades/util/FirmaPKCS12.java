/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.firmaxades.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.javasign.pkstore.CertStoreException;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;

/**
 * <p>
 * Clase que deberan extender para realizar para realizar firmas XML utilizando
 * un repositorio PKCS12 dado.
 * </p>
 *
 * @author MITyC, Gabriel Eguiguren
 *
 */
public abstract class FirmaPKCS12 {

    private static final Logger log = Logger.getLogger(FirmaPKCS12.class);

    /**
     * <p>
     * Almacen PKCS12 con el que se desea realizar la firma
     * </p>
     */
    private String archivoPKCS12;

    /**
     * <p>
     * Constrasenia de acceso a la clave privada del usuario
     * </p>
     */
    private String contraseniaPKCS12;

    /**
     * <p>
     * Directorio donde se almacenara el resultado de la firma
     * </p>
     */
    private String directorioSalidaFirma;

    /**
     * Constructor de la clase
     *
     * @param archivoPKCS12
     * @param contraseniaPKCS12
     * @param directorioSalidaFirma
     */
    public FirmaPKCS12(String archivoPKCS12, String contraseniaPKCS12,
            String directorioSalidaFirma) {
        super();
        this.archivoPKCS12 = archivoPKCS12;
        this.contraseniaPKCS12 = contraseniaPKCS12;
        this.directorioSalidaFirma = directorioSalidaFirma;
    }

    public boolean validar() {
        IPKStoreManager storeManager = getPKStoreManager();
        if (storeManager == null) {
            log.error("El gestor de claves no se ha obtenido correctamente.");
            return false;
        }

        return true;
    }

    /**
     * <p>
     * La ejecucion consistira en la firma de los datos creados por el metodo
     * abstracto <code>createDataToSign</code> mediante el certificado declarado
     * en la variable <code>archivoPKCS12</code>. El resultado del proceso de
     * firma sera almacenado en un fichero XML en el directorio correspondiente
     * a la variable <code>directorioSalidaFirma</code> del usuario bajo el
     * nombre devuelto por el metodo abstracto <code>getSignFileName</code>
     * </p>
     */
    public void execute() {

        // Obtencion del gestor de claves
        IPKStoreManager storeManager = getPKStoreManager();
        if (storeManager == null) {
            log.error("El gestor de claves no se ha obtenido correctamente.");
            return;
        }

        // Obtencion del certificado para firmar. Utilizaremos el primer
        // certificado del almacen.
        X509Certificate certificado = getFirstCertificate(storeManager);

        if (certificado == null) {
            log.error("No existe ningun certificado para firmar.");
            return;
        }

        try {
            log.debug("getType: " + certificado.getType());
            log.debug("getClass: " + certificado.getClass());
            log.debug("getKeyUsage: " + certificado.getKeyUsage());
            log.debug("ExtendedKeyUsage: " + certificado.getExtendedKeyUsage());
        } catch (CertificateParsingException e1) {
            log.error(e1);
        }

        // Obtencion de la clave privada asociada al certificado
        PrivateKey privateKey;
        try {
            privateKey = storeManager.getPrivateKey(certificado);
        } catch (CertStoreException e) {
            log.error("Error al acceder al obtener la clave privada del almacen.");
            return;
        }

        // Obtencion del provider encargado de las labores criptograficas
        Provider provider = storeManager.getProvider(certificado);

        // Creacion del objeto que contiene tanto los datos a firmar como la
        // configuracion del tipo de firma
        DataToSign datosAFirmar = createDataToSign();

        // Firmamos el documento
        Document documentoFirmado = null;
        try {

            // Creacion del objeto encargado de realizar la firma
            FirmaXML firma = createFirmaXML();
            Object[] res = firma.signFile(certificado, datosAFirmar,
                    privateKey, provider);
            documentoFirmado = (Document) res[0];
        } catch (Exception ex) {
            log.error("Error realizando la firma", ex);
            return;
        }

        saveDocumentToFile(documentoFirmado, getSignatureFileName());
    }

    /**
     * <p>
     * Crea el objeto DataToSign que contiene toda la informacion de la firma
     * que se desea realizar. Todas las implementaciones deberan proporcionar
     * una implementacion de este metodo
     * </p>
     *
     * @return El objeto DataToSign que contiene toda la informacion de la firma
     * a realizar
     */
    protected abstract DataToSign createDataToSign();

    /**
     * <p>
     * Nombre del fichero donde se desea guardar la firma generada. Todas las
     * implementaciones deberan proporcionar este nombre.
     * </p>
     *
     * @return El nombre donde se desea guardar la firma generada
     */
    protected abstract String getSignatureFileName();

    /**
     * <p>
     * Crea el objeto <code>FirmaXML</code> con las configuraciones necesarias
     * que se encargara de realizar la firma del documento. En el caso mas
     * simple no es necesaria ninguna configuracion especifica.
     * </p>
     *
     * @return firmaXML Objeto <code>FirmaXML</code> configurado listo para
     * usarse
     */
    protected FirmaXML createFirmaXML() {
        return new FirmaXML();
    }

    /**
     * <p>
     * Escribe el documento a un fichero.
     * </p>
     *
     * @param document El documento a imprmir
     * @param pathfile El path del fichero donde se quiere escribir.
     */
    private void saveDocumentToFile(Document document, String pathfile) {
        try {
            FileOutputStream fos = new FileOutputStream(pathfile);
            UtilidadTratarNodo.saveDocumentToOutputStream(document, fos, true);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException: Error al salvar el documento", e);
            //System.exit(-1);
        }
    }

    /**
     * <p>
     * Devuelve el <code>Document</code> correspondiente al
     * <code>resource</code> pasado como parametro
     * </p>
     *
     * @param resource El recurso que se desea obtener
     * @return El <code>Document</code> asociado al <code>resource</code>
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    protected Document getDocument(String filepath) throws SAXException,
            IOException, ParserConfigurationException {
        Document doc = null;

        File file = new File(filepath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(file);

        return doc;

    }

    /**
     * <p>
     * Devuelve el contenido del documento XML correspondiente al
     * <code>resource</code> pasado como parametro
     * </p>
     * como un <code>String</code>
     *
     * @param resource El recurso que se desea obtener
     * @return El contenido del documento XML como un <code>String</code>
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    protected String getDocumentAsString(String resource) throws SAXException,
            IOException, ParserConfigurationException {
        Document doc = getDocument(resource);
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        StringWriter stringWriter = new StringWriter();
        try {
            serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(
                    stringWriter));
        } catch (TransformerException e) {
            log.error("Error al imprimir el documento");

        }

        return stringWriter.toString();
    }

    /**
     * <p>
     * Devuelve el gestor de claves que se va a utilizar
     * </p>
     *
     * @return El gestor de claves que se va a utilizar</p>
     */
    private IPKStoreManager getPKStoreManager() {
        IPKStoreManager storeManager = null;

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");

            java.io.FileInputStream fis = null;
            try {
                fis = new java.io.FileInputStream(this.archivoPKCS12);
                ks.load(fis, this.contraseniaPKCS12.toCharArray());
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            storeManager = new KSStore(ks, new PassStoreKSGenerica(this.contraseniaPKCS12));
        } catch (KeyStoreException ex) {
            log.error("KeyStoreException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (NoSuchAlgorithmException ex) {
            log.error("NoSuchAlgorithmException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (CertificateException ex) {
            log.error("CertificateException: No se puede generar KeyStore PKCS12", ex);
            //System.exit(-1);
        } catch (IOException ex) {
            log.error("IOException: No se puede generar KeyStore PKCS12");
            //System.exit(-1);
        }
        return storeManager;
    }

    /**
     * <p>
     * Recupera el primero de los certificados del almacen.
     * </p>
     *
     * @param storeManager Interfaz de acceso al almacen
     * @return Primer certificado disponible en el almacen
     */
    private X509Certificate getFirstCertificate(
            final IPKStoreManager storeManager) {
        List<X509Certificate> certs = null;
        try {
            certs = storeManager.getSignCertificates();
        } catch (CertStoreException ex) {
            System.err.println("Fallo obteniendo listado de certificados");
            System.exit(-1);
        }
        if ((certs == null) || (certs.size() == 0)) {
            log.error("Lista de certificados vacia");
            return null;
        }

        X509Certificate certificado = certs.get(0);
        return certificado;
    }

    /**
     * @return the archivoPKCS12
     */
    public String getArchivoPKCS12() {
        return archivoPKCS12;
    }

    /**
     * @param archivoPKCS12 the archivoPKCS12 to set
     */
    public void setArchivoPKCS12(String archivoPKCS12) {
        this.archivoPKCS12 = archivoPKCS12;
    }

    /**
     * @return the contraseniaPKCS12
     */
    public String getContraseniaPKCS12() {
        return contraseniaPKCS12;
    }

    /**
     * @param contraseniaPKCS12 the contraseniaPKCS12 to set
     */
    public void setContraseniaPKCS12(String contraseniaPKCS12) {
        this.contraseniaPKCS12 = contraseniaPKCS12;
    }

    /**
     * @return the directorioSalidaFirma
     */
    public String getDirectorioSalidaFirma() {
        return directorioSalidaFirma;
    }

    /**
     * @param directorioSalidaFirma the directorioSalidaFirma to set
     */
    public void setDirectorioSalidaFirma(String directorioSalidaFirma) {
        this.directorioSalidaFirma = directorioSalidaFirma;
    }

}
