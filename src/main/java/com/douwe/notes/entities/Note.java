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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@NamedQueries({
    @NamedQuery(name = "Note.findNoteEvaluationCours",query = "SELECT n from Note n WHERE n.etudiant = :param1 and n.evaluation = :param2 and n.cours = :param3 and n.anneeAcademique = :param4"),
    @NamedQuery(name = "Note.findNoteByAnnee", query = "SELECT n from Note n WHERE n.anneeAcademique.id = :idParam")
})
@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ETUDIANT_ID","EVALUATION_ID","COURS_ID","ANNEEACADEMIQUE_ID","SESSIONS"}))
public class Note implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column
    @Min(0)
    @Max(20)
    private double valeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ETUDIANT_ID")
    @EqualsAndHashCode.Exclude
    private Etudiant etudiant;
    
    //les trois derniers a mettre à l'interface importation
    @ManyToOne(optional = false)
    @JoinColumn(name = "EVALUATION_ID")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private Evaluation evaluation;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "COURS_ID")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private Cours cours;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private AnneeAcademique anneeAcademique;
    
    @Column(name = "SESSIONS", nullable = false)
    @EqualsAndHashCode.Exclude
    private Session session = Session.normale;
    
    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    
    public Note(){
        
    }
}
