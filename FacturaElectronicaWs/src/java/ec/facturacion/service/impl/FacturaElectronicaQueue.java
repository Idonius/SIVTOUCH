/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturacion.service.impl;

import ec.facturacion.app.ComprobanteEnviado;
import ec.facturacion.service.IFacturaElectronica;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Christian
 */
@MessageDriven(mappedName = "jms/QueueFactura", activationConfig = {
    @ActivationConfigProperty(propertyName
            = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class FacturaElectronicaQueue implements MessageListener {

    @EJB
    IFacturaElectronica comprobante;

    public FacturaElectronicaQueue() {
    }

    @Override
    public void onMessage(Message message) {

        System.err.println("entra");
        try {
            ObjectMessage mensaje = (ObjectMessage) message;
            byte[] xmlData = (byte[]) mensaje.getObject();

            System.err.println(mensaje);

            ComprobanteEnviado respuesta;

            respuesta = comprobante.firmarEnviarAutorizar(xmlData);
            
            System.err.println(respuesta.getNovedades());

        } catch (Exception ex) {
            System.err.println("novedades");
        }
    }

}
