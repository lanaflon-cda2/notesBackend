package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@XmlRootElement(name = "inscription")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Inscription.deleteActive",query = "update Inscription i set i.active = 0 where i.id = :idParam"),
    @NamedQuery(name = "Inscription.findByAnnee", query = "SELECT i from Inscription i WHERE i.anneeAcademique.id = :idParam"),
    @NamedQuery(name = "Inscription.findAllActive",query = "select i from Inscription i where i.active=1"),
    @NamedQuery(name = "Inscription.findByEtudiant",query = "SELECT i from Inscription i WHERE i.etudiant.id = :param1 and i.anneeAcademique.id = :param2"),
    @NamedQuery(name = "Inscription.findByEtudiantOrdered",query = "select i from Inscription i join i.anneeAcademique a on i.anneeAcademique.id = a.id where i.etudiant.id = :idParam order by a.numeroDebut DESC")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ETUDIANT_ID","ANNEEACADEMIQUE_ID","PARCOURS_ID"}))
public class Inscription implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ETUDIANT_ID")
    @XmlTransient
    private Etudiant etudiant;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @XmlTransient
    private AnneeAcademique anneeAcademique;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "PARCOURS_ID")
    @XmlTransient
    private Parcours parcours;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Inscription(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Parcours getParcours() {
        return parcours;
    }

    public void setParcours(Parcours parcours) {
        this.parcours = parcours;
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

    @Override
    public String toString() {
        return "Inscription{" + "id=" + id + ", version=" + version + ", etudiant=" + etudiant + ", anneeAcademique=" + anneeAcademique + ", parcours=" + parcours + ", active=" + active + '}';
    }
    
    
}
