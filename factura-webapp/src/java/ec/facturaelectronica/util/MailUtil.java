/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Armando
 */
public class MailUtil extends RecursosServices {

    private final Properties mailProperties;
    private final String MAIL_FROM = "armando.suarez.pons@gmail.com";
    private final Session mailSession;
    private final MimeMessage mailMessage;
    
    public MailUtil() {
        mailProperties = new Properties();
        
        mailProperties.put("mail.smtp.host", certificado.getString("mail.smtp.host"));
        mailProperties.put("mail.smtp.socketFactory.port", certificado.getString("mail.smtp.socketFactory.port"));
        mailProperties.put("mail.smtp.socketFactory.class", certificado.getString("mail.smtp.socketFactory.class"));
        mailProperties.put("mail.smtp.auth", certificado.getString("mail.smtp.auth"));
        mailProperties.put("mail.smtp.port", certificado.getString("mail.smtp.port"));     
        
        mailSession = Autenticar();
        mailMessage = new MimeMessage(mailSession);
    }
    
    private Session Autenticar() {
        return Session.getDefaultInstance(mailProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("armando.suarez.pons@gmail.com", "freesoftware21041503");
            }
        });        
    }
        
    public void from(final String mailFrom) throws MessagingException{
        if(mailFrom != null && !"".equals(mailFrom)){
            mailMessage.setFrom(new InternetAddress(mailFrom));
        }else{
            mailMessage.setFrom(new InternetAddress(MAIL_FROM));
        }
    }
    
    public void to(final List<String> mailsDestinatarios) throws MessagingException{
        for (String destinatario : mailsDestinatarios) {
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        }
        
    }
    
    public void to(final String destinatario) throws MessagingException{
        mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));        
    }
    
    public void cc(final List<String> mailsCC) throws MessagingException{
        for (String mail : mailsCC) {
            mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail));
        }                
    }
    
    public void cc(final String mailCC) throws MessagingException{
        mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
    }
    
    public void bcc(final List<String> mailsBCC) throws MessagingException{
        for (String mail : mailsBCC) {
            mailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail));
        }                        
    }
    public void bcc(final String mailBCC) throws MessagingException{
        mailMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailBCC));
    }
    
    public void subject(final String titulo) throws MessagingException{
        mailMessage.setSubject(titulo);
    }
    
    public void body(final String cuerpoHtml) throws MessagingException{
        mailMessage.setContent(cuerpoHtml, "text/html");
    }
    
    public void send() throws MessagingException{
        Transport.send(mailMessage);
    }
    
}
