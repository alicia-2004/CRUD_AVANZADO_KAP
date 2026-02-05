/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * A logger class that saves messages and errors to files. This class helps
 * track what happens in the application. It saves information to log files for
 * debugging and monitoring.
 */
public class PersonalLogger {

    /**
     * List that stores all log messages in memory.
     */
    private ArrayList<String> logMensajes;

    /**
     * Creates a new PersonalLogger with an empty message list.
     */
    public PersonalLogger() {
        logMensajes = new ArrayList<>();
    }

    /**
     * Saves an information message to the log file. The message is saved with
     * the current date and time. It is stored in "logs.dat" file and printed to
     * console.
     *
     * @param mensaje The message to log (information or status update)
     */
    public void logMessage(String mensaje) {
        File fichLogs = new File("logs.dat");
        String logGuardado = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - *MENSAJE INFORMATIVO*:" + mensaje + System.lineSeparator();
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

    /**
     * Saves an error message to the error file. The error is saved with the
     * current date and time. It is stored in "errors.dat" file and printed to
     * console.
     *
     * @param mensaje The error message to log (problem or exception)
     */
    public void logError(String mensaje) {
        File fichLogs = new File("errors.dat");
        String logGuardado = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - *INFORMACIÓN ERROR*: " + mensaje + System.lineSeparator();
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
}
