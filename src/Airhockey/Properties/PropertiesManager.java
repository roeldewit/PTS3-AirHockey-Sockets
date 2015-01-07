/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Properties;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public final class PropertiesManager {

    public static Properties props = new Properties();

    public static String loadProperty(String key) {
        try {
            props.load(PropertiesManager.class.getResourceAsStream("Properties.properties"));
            return props.getProperty(key);

        } catch (IOException ex) {
            Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveProperty(String key, String value) {
        try {
            props.load(PropertiesManager.class.getResourceAsStream("Properties.properties"));
            props.setProperty(key, value);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
