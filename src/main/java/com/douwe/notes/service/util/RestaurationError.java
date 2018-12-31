/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author guidona
 */
@XmlRootElement(name = "erreur")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestaurationError {
    private int nombresErreurs;
    private String erreur;

    public RestaurationError() {
    }

    public RestaurationError(int nombresErreurs, String erreur) {
        this.nombresErreurs = nombresErreurs;
        this.erreur = erreur;
    }
    
    public int getNombresErreurs() {
        return nombresErreurs;
    }

    public void setNombresErreurs(int nombresErreurs) {
        this.nombresErreurs = nombresErreurs;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }
    
    
}
