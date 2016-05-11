/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

/**
 *
 * @author Armando
 */
public class CredencialesUtils {
    
    public static Boolean verificar(String password, String confirmPassword){
        Boolean result = Boolean.FALSE;
        Boolean noNullPassword = password != null && !password.equals("");
        Boolean noNullConfirmPassword = confirmPassword != null && !confirmPassword.equals("");
        Boolean iguales = password.equals(confirmPassword);
        
        if(noNullConfirmPassword && noNullPassword && iguales){
            result = Boolean.TRUE;
        }
        
        return result;
    }
}
