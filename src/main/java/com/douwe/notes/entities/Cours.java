package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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


/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */

@Data
@Entity
@XmlRootElement(name = "cours")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({

@NamedQuery(name = "Cours.deleteActive",query = "update Cours c set c.active = 0 where c.id = :idParam"),
@NamedQuery(name = "Cours.findAllActive",query = "select c from Cours c WHERE c.active=1"),
@NamedQuery(name = "Cours.findByIntitule",query = "SELECT c FROM Cours c WHERE c.intitule like :param"),
//@NamedQuery(name = "Cours.findByUE",query = "select co from Cours co, UniteEnseignement ue JOIN ue.courses c_e where ue.id = :idParam and c_e.id =co.id")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"DEPARTEMENT_ID","INTITULE"}))
public class Cours implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column(name = "INTITULE")
    private String intitule;
    
    @ManyToOne(optional = false)
    private TypeCours typeCours;
    
    @XmlTransient
    @ManyToMany(mappedBy = "cours")
    private List<UniteEnseignement> uniteEnseignements;
    
    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;

    @ManyToOne
    @JoinColumn(name = "DEPARTEMENT_ID")
    private Departement departement;
       
    public Cours(){
    }
}