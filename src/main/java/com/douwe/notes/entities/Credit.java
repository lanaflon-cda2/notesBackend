package com.douwe.notes.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Data
@Entity
@XmlRootElement(name = "credit")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = "Credit.findCreditByAnnee", query = "SELECT c FROM Credit c WHERE c.academique.id = :idParam")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ANNEEACADEMIQUE_ID","PARCOURS_ID","COURS_ID"}))
public class Credit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Integer valeur;
    
    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @EqualsAndHashCode.Exclude
    private AnneeAcademique academique;

    @ManyToOne
    @JoinColumn(name = "PARCOURS_ID")
    @EqualsAndHashCode.Exclude
    private Parcours parcours;

    @ManyToOne    
    @JoinColumn(name = "COURS_ID")
    @EqualsAndHashCode.Exclude
    private Cours cours;
}
