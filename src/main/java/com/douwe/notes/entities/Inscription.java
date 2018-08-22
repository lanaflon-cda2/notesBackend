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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Data
@Entity
@XmlRootElement(name = "inscription")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
@NamedQuery(name = "Inscription.deleteActive",query = "update Inscription i set i.active = 0 where i.id = :idParam"),
@NamedQuery(name = "Inscription.findAllActive",query = "select i from Inscription i where i.active=1"),
@NamedQuery(name = "Inscription.findByEtudiant",query = "SELECT i from Inscription i WHERE i.etudiant = :param1 and i.anneeAcademique = :param")

})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ETUDIANT_ID","ANNEEACADEMIQUE_ID"}))
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
    @EqualsAndHashCode.Exclude
    private Etudiant etudiant;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private AnneeAcademique anneeAcademique;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "PARCOURS_ID")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private Parcours parcours;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Inscription(){
        
    }

}