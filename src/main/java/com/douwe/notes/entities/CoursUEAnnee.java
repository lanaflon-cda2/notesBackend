package com.douwe.notes.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * I really think we don't need this class
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Data
@Deprecated
@Entity
@XmlRootElement(name = "coursUeAnnee")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ANNEEACADEMIQUE_ID","UNITEENSEIGNEMENT_ID","COURS_ID"}))
public class CoursUEAnnee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "COURS_ID")    
    @EqualsAndHashCode.Exclude
    private Cours cours;
    
    @ManyToOne
    @JoinColumn(name = "UNITEENSEIGNEMENT_ID")
    @EqualsAndHashCode.Exclude
    private UniteEnseignement uniteEnseignement;
    
    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @EqualsAndHashCode.Exclude
    private AnneeAcademique anneeAcademique;
}
