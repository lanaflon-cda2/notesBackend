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
@XmlRootElement(name = "programme")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Programme.deleteActive",query = "update Programme p set p.active = 0 where p.id = :idParam"),
    @NamedQuery(name = "Programme.findAllActive",query = "select p from Programme p where p.active=1"),
    @NamedQuery(name = "Programme.findByNiveauOption",query = "SELECT p FROM Programme p WHERE p.parcours.niveau = :param1 and p.parcours.option = :param2 and p.anneeAcademique = :param3 and p.semestre = :param4"),
    @NamedQuery(name = "Programme.findByAnnee", query = "SELECT p FROM Programme p WHERE p.anneeAcademique.id = :idParam")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"SEMESTRE_ID","ANNEEACADEMIQUE_ID","PARCOURS_ID","UNITEENSEIGNEMENT_ID"}))
public class Programme implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @EqualsAndHashCode.Exclude
    private AnneeAcademique anneeAcademique;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "UNITEENSEIGNEMENT_ID")
    @EqualsAndHashCode.Exclude
    private UniteEnseignement uniteEnseignement;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "PARCOURS_ID")
    @EqualsAndHashCode.Exclude
    private Parcours parcours;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "SEMESTRE_ID")
    @EqualsAndHashCode.Exclude
    private Semestre semestre;
    
    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Programme(){
        
    }
}