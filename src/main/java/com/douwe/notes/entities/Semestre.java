package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@XmlRootElement(name = "semestre")
@XmlAccessorType(XmlAccessType.FIELD)
public class Semestre implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column(unique = true)
    private String intitule;
    
    @ManyToOne(optional = false)
    private Niveau niveau;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Semestre(String intitule){
        this.intitule = intitule;
    } 
     
    public Semestre(){
        
    }
}
