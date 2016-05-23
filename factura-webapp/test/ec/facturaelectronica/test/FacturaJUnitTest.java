package ec.facturaelectronica.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ec.facturaelectronica.util.MailUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Armando
 */
public class FacturaJUnitTest {
    
    public FacturaJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     @Ignore
     public void mailTest() {
         MailUtil mail = new MailUtil();
         StringBuilder body = new StringBuilder();
         body.append("<H1>Hola Armandito</H1>");
         
        try {
            mail.from("armando.suarez.pons@gmail.com");
            mail.cc("quatelmoc@yopmail.com");
            mail.to("mscblazer@yahoo.com");
            mail.subject("Test para Armando");
            mail.body(body.toString());
            mail.send();
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
     }
}
