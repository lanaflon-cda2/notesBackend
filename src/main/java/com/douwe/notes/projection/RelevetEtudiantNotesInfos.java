package com.douwe.notes.projection;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RelevetEtudiantNotesInfos {
    
    private double moyenne;
    
    private int rang;
    
    private int nombreCreditValides;
    
    Map<String, Double> notes;

    public double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getNombreCreditValides() {
        return nombreCreditValides;
    }

    public void setNombreCreditValides(int nombreCreditValides) {
        this.nombreCreditValides = nombreCreditValides;
    }

    public Map<String, Double> getNotes() {
        return notes;
    }

    public void setNotes(Map<String, Double> notes) {
        this.notes = notes;
    }
    
    
    
}
