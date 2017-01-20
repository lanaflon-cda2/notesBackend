package com.douwe.notes.projection;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
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
    
    private double moyenneMgp;
    
    private int rang;
    
    private int nombreCreditValides;
    
    private Map<String, Double> notes;
    
    private Map<String, Double> mgp;
    
    private Map<String, String> semestres;
    
    private boolean aToutValide;
    
    private Double mgpCycle;
    
    private AnneeAcademique anneeAcademique;
    
    Map<String, Session> sessions;

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

    public Map<String, Session> getSessions() {
        return sessions;
    }

    public void setSessions(Map<String, Session> sessions) {
        this.sessions = sessions;
    }

    public double getMoyenneMgp() {
        return moyenneMgp;
    }

    public void setMoyenneMgp(double moyenneMgp) {
        this.moyenneMgp = moyenneMgp;
    }

    public Map<String, Double> getMgp() {
        return mgp;
    }

    public void setMgp(Map<String, Double> mgp) {
        this.mgp = mgp;
    }

    public Map<String, String> getSemestres() {
        return semestres;
    }

    public void setSemestres(Map<String, String> semestres) {
        this.semestres = semestres;
    }

    public boolean isaToutValide() {
        return aToutValide;
    }

    public void setaToutValide(boolean aToutValide) {
        this.aToutValide = aToutValide;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Double getMgpCycle() {
        return mgpCycle;
    }

    public void setMgpCycle(Double mgpCycle) {
        this.mgpCycle = mgpCycle;
    }
}
