package com.douwe.notes.projection;

import java.io.Serializable;

/**
 *
 * @author cellule
 */
public class EtudiantNiveau implements Serializable {

    private String niveau;

    private String matricule;

    private Double mgp;

    private Integer credit;

    public EtudiantNiveau() {
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Double getMgp() {
        return mgp;
    }

    public void setMgp(Double mgp) {
        this.mgp = mgp;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

}
