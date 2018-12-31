package com.douwe.notes.projection;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Session;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public class EtudiantNotes {
    
    private String matricule;
    
    private String nom;
    
    private Map<String,Double> note;
    
    private Session session;
    
    private AnneeAcademique annee;
    
    private Map<String, Integer> details;
    
    public Optional<Double> getMoyenne(){
        double res = 0;
        boolean isClean = true;
        for (Map.Entry<String, Integer> col : details.entrySet()) {
            if(note.containsKey(col.getKey())){
                res += note.get(col.getKey()) * col.getValue();
            }else{
                isClean = false;
            }
        }
        
        if (isClean || annee.getNumeroDebut() < 2017){
            return Optional.of(res / 100);
        }
        return Optional.empty();
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Map<String, Double> getNote() {
        return note;
    }

    public void setNote(Map<String, Double> note) {
        this.note = note;
    }

    public Map<String, Integer> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Integer> details) {
        this.details = details;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public AnneeAcademique getAnnee() {
        return annee;
    }

    public void setAnnee(AnneeAcademique annee) {
        this.annee = annee;
    }
    
    
   
}
