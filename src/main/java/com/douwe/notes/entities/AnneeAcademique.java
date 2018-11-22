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
import lombok.Data;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Data
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

    @Override
    public String toString(){
        return String.format("%d -%d", numeroDebut, numeroDebut + 1);
    }
}
