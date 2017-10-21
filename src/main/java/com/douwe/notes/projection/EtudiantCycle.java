package com.douwe.notes.projection;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author cellule
 */
public class EtudiantCycle implements Serializable{
    
    private String matricule;
    
    private String nom;
    
    private final HashMap<String,Double> mgps;
    
    private final HashMap<String,Integer> credits;
    
    private Double mgp;
    
    public EtudiantCycle(){
        mgps = new HashMap<>();
        credits = new HashMap<>();
        mgp = null;
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
    
    public Double getMGPNiveau(String niveau){
        if(mgps.containsKey(niveau))
            return mgps.get(niveau);
        return null;
    }
    
    public void addMGPNiveau(String niveau, Double mpg){
        mgps.put(niveau, mpg);
    }
    
    public Integer getCreditNiveau(String niveau){
        if(credits.containsKey(niveau))
            return credits.get(niveau);
        return null;
    }
    
    public void addCreditNiveau(String niveau, Integer credit){
        credits.put(niveau, credit);
    }

    public Double getMgp() {
        return mgp;
    }

    public void setMgp(Double mgp) {
        this.mgp = mgp;
    }
}
