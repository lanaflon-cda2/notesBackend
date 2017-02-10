package com.douwe.notes.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public class MessageHelper {
    private static Properties properties;

    public MessageHelper() {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("properties/messages.properties");            
            properties = new Properties();
            properties.load(input);

        } catch (IOException ex) {
            Logger.getLogger(MessageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

 

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
}
