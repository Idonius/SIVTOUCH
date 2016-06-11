/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

import java.util.UUID;

/**
 *
 * @author Armando
 */
public class CredencialesUtils {
    
    public static Boolean verificar(String password, String confirmPassword){
        Boolean noNullPassword = password != null && !password.equals("");
        Boolean noNullConfirmPassword = confirmPassword != null && !confirmPassword.equals("");
        Boolean iguales = password.equals(confirmPassword);
        
        Boolean result = noNullConfirmPassword && noNullPassword && iguales ? Boolean.TRUE : Boolean.FALSE;
        
        return result;
    }
    
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
