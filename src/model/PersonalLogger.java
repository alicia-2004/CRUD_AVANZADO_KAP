/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author 2dami
 */
public class PersonalLogger {
    private ArrayList<String> logMensajes;
    
    public PersonalLogger() {
        logMensajes = new ArrayList<>();
    }
    
     public void logMessage(String mensaje) {
        File fichLogs = new File("logs.dat");
        String logGuardado = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - *MENSAJE INFORMATIVO*:" + mensaje;
        logMensajes.add(logGuardado);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichLogs, true));
            oos.writeObject(logGuardado);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(logGuardado);
    }
     
     public void logError(String mensaje) {
        File fichLogs = new File("errors.dat");
        String logGuardado = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - *INFORMACIÓN ERROR*: " + mensaje;
        logMensajes.add(logGuardado);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichLogs, true));
            oos.writeObject(logGuardado);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         System.out.println("llega al log");
        System.out.println(logGuardado);
    }
}
