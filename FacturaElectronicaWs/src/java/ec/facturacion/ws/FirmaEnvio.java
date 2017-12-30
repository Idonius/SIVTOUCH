/*
 * www.facturacionelectronica.ec
 *
 * © Gabriel Eguiguren P. 2012-2014 
 * Todos los derechos reservados
 */
package ec.facturacion.ws;

import ec.facturacion.app.ComprobanteEnviado;
import ec.facturacion.app.FacturaElectronica;
import ec.facturacion.exception.FacturaFirmaException;
import ec.facturacion.firmaxades.util.xml.FileUtils;
import ec.facturacion.service.IFacturaElectronica;
import ec.facturacionelectronica.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * FirmaEnvio expuestos relacionados con facturacion electronica
 *
 * @author Gabriel Eguiguren P.
 */
@WebService(serviceName = "FirmaEnvio")
public class FirmaEnvio {

    @EJB
    IFacturaElectronica comprobante;

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FirmaEnvio.class.getName());

    /**
     * Funcion expuesta del web service que permite firmar un archivoXML
     * proporcionado y luego enviarlo al SRI y consultar su autorizacion en un
     * solo paso
     *
     * @param archivoAFirmar archivo que representa el comprobante XML
     * @param email notifica a ese eemail el comprobante
     * 0 o produccion valor 1
     *
     * @return devuelve un objeto tipo ComprobanteEnviado con los datos de clave
     * de acceso, numero autorizacion objeto tipo File con comprobante XML
     * autorizado o no autorizado
     * @throws ec.facturacion.exception.FacturaFirmaException
     */
    @WebMethod(operationName = "firmarEnviarAutorizar")
    public ComprobanteEnviado firmarEnviarAutorizar(@WebParam(name = "archivoAFirmar") byte[] archivoAFirmar,@WebParam(name = "email") String email,@WebParam(name = "codigoEmpresa") String codigoEmpresa) throws FacturaFirmaException {
        ComprobanteEnviado respuesta=null;
        try {

            respuesta = comprobante.firmarEnviarAutorizar(archivoAFirmar,email,codigoEmpresa);

        } catch (Exception ex) {
            Logger.getLogger(FirmaEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

   

    /**
     * Funcion expuesta del web service que permite consultar el estado de
     * autorizacion de un comprobante
     *
     * @param claveDeAcceso archivo codificado que representa el comprobante XML
     * @param ambiente bandera que indica si se envia a los WS de pruebas valor
     * 0 o produccion valor 1
     * @param numeroIntentos numero de veces consecutivas de llamada al servicio
     *
     * @return devuelve un objeto tipo Autorizacion con los datos del
     * comprobante XML
     */
    @WebMethod(operationName = "consultaAutorizacion")
    public Autorizacion consultaAutorizacion(@WebParam(name = "claveDeAcceso") String claveDeAcceso, @WebParam(name = "ambiente") int ambiente, @WebParam(name = "numeroIntentos") int numeroIntentos) throws FacturaFirmaException {

        Autorizacion respuesta;
        respuesta = comprobante.consultaAutorizacion(claveDeAcceso);

        return respuesta;
    }

   

}
