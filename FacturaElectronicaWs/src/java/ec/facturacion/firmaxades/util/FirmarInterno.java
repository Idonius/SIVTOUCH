/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.firmaxades.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import org.apache.log4j.Logger;

/**
 * Clase de ejemplo para la firma XAdES-BES enveloped de un documento
 * <p>
 * Para realizar la firma se utilizara el almacen PKCS#12 definido en la
 * variable <code>FirmaPKCS12Generica.archivoPKCS12</code>, al que se accedera
 * mediante el password definida en la variable
 * <code>FirmaPKCS12Generica.contraseniaPKCS12</code>. El directorio donde
 * quedara el archivo XML resultante sera el indicado en la variable
 * <code>FirmaPKCS12Generica.directorioSalidaFirma</code>
 * </p>
 *
 * @author Gabriel Eguiguren
 */
public class FirmarInterno extends FirmaPKCS12 {

    static Logger log = Logger.getLogger(FirmarInterno.class.getName());

    // Recurso a firmar
    private String pathArchivoAFirmar;

    // Fichero donde se desea guardar la firma
    private String nombreArchivoFirmado;

    /**
     *
     * @param archivoPKCS12
     * @param contraseniaPKCS12
     * @param directorioSalidaFirma
     * @param archivoAFirmar
     * @param archivoFirmado
     */
    public FirmarInterno(String archivoPKCS12, String contraseniaPKCS12,
            String directorioSalidaFirma, String archivoAFirmar, String archivoFirmado) {

        super(archivoPKCS12, contraseniaPKCS12, directorioSalidaFirma);

        this.pathArchivoAFirmar = archivoAFirmar;
        this.nombreArchivoFirmado = archivoFirmado;

    }

    public FirmarInterno(String archivoPKCS12, String contraseniaPKCS12) {

        super(archivoPKCS12, contraseniaPKCS12, null);

    }

    @Override
    protected DataToSign createDataToSign() {
        DataToSign datosAFirmar = new DataToSign();
        datosAFirmar.setXadesFormat(EnumFormatoFirma.XAdES_BES);
        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        Document docToSign = null;
        try {
            docToSign = getDocument(this.pathArchivoAFirmar);
        } catch (SAXException e) {
            log.error("createDataToSign:", e);
        } catch (IOException e) {
            log.error("createDataToSign:", e);
        } catch (ParserConfigurationException e) {
            log.error("createDataToSign:", e);
        }
        datosAFirmar.setDocument(docToSign);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign(
                "comprobante"), "contenido comprobante", null,
                "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");

        return datosAFirmar;
    }

    @Override
    protected String getSignatureFileName() {
        return this.nombreArchivoFirmado;
    }

    public String getPathArchivoAFirmar() {
        return pathArchivoAFirmar;
    }

    public void setPathArchivoAFirmar(String pathArchivoAFirmar) {
        this.pathArchivoAFirmar = pathArchivoAFirmar;
    }

    public String getNombreArchivoFirmado() {
        return nombreArchivoFirmado;
    }

    public void setNombreArchivoFirmado(String nombreArchivoFirmado) {
        this.nombreArchivoFirmado = nombreArchivoFirmado;
    }

}
