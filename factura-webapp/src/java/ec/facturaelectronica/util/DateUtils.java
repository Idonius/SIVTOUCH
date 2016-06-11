/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.facturaelectronica.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Armando
 */
public class DateUtils {

    private static DateFormat dateFormat;
    
    public static String modifyDaysInDateFormat(Integer days){
        Calendar today = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        today.add(Calendar.DATE, days);
        
        return dateFormat.format(today.getTime());
    }
}
