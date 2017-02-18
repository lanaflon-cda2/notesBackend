package com.douwe.notes.projection;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@XmlRootElement
public class AuthLoginElement implements Serializable{
    
    private String username;
    private String password;
 
    public AuthLoginElement(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthLoginElement() {
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
