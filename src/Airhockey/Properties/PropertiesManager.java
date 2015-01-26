package Airhockey.Properties;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used for setting and retreiving data from the properties file.
 *
 * @author Sam
 */
public final class PropertiesManager {

    public static Properties props = new Properties();

//    public static createPropertiesFile(){
//        
//    }
    
    /**
     * Retreive a propert from the file with a specific key.
     *
     * @param key The key to identify the property.
     * @return The requested property value.
     */
    public static String loadProperty(String key) {
        try {
            props.load(PropertiesManager.class.getResourceAsStream("AIProperties.properties"));
            return props.getProperty(key);

        } catch (IOException ex) {
            Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Set a property with a given key and value.
     *
     * @param key The key to retrieve the property value with.
     * @param value The value of the property.
     */
    public static void saveProperty(String key, String value) {
        try {
            props.load(PropertiesManager.class.getResourceAsStream("AIProperties.properties"));
            props.setProperty(key, value);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
