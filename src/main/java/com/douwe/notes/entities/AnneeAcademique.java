package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Entity
@XmlRootElement(name = "annee")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({

@NamedQuery(name = "Annee.findAllActive",query = "SELECT a from AnneeAcademique a WHERE a.active = 1"),
@NamedQuery(name = "Annee.findByNumeroAnnee", query = "select a from AnneeAcademique a where a.numeroDebut = :param")

//@NamedQuery(name = "Annee.findByString",query = " SELECT a from AnneeAcademique a WHERE a.dateString = :param")


})
public class AnneeAcademique implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date debut;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date fin;
    
    @Column(unique = true, nullable = false)
    private Integer numeroDebut;
    
  
    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
   
    
    public AnneeAcademique(){     
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    @JsonIgnore
    public int getVersion() {
        return version;
    }

    @JsonIgnore
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonIgnore
    public int getActive() {
        return active;
    }

    @JsonIgnore
    public void setActive(int active) {
        this.active = active;
    }

    public Integer getNumeroDebut() {
        return numeroDebut;
    }

    public void setNumeroDebut(Integer numeroDebut) {
        this.numeroDebut = numeroDebut;
    }
 
    @Override
    public String toString() {
        return String.format("%s - %s",numeroDebut, numeroDebut + 1);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnneeAcademique other = (AnneeAcademique) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
    
    
    
}
