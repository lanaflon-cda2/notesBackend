package com.douwe.notes.entities;

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
@XmlRootElement(name = "etudiant")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
@NamedQuery(name = "Etudiant.deleteActive",query = "update Etudiant e set e.active = 0 where e.id = :idParam"),
@NamedQuery(name = "Etudiant.findAllActive",query = "select e from Etudiant e where e.active=1"),    
@NamedQuery(name = "Etudiant.findByMatricule",query = "SELECT e from Etudiant e WHERE e.matricule like :param")
})
public class Etudiant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column(unique = true)
    private String matricule;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date dateDeNaissance;
    
    @Column(columnDefinition = "boolean default true")
    private boolean validDate;
    
    @Column
    private String lieuDeNaissance;
    
    @Column
    private String email;
    
    @Column
    private String numeroTelephone;
    
    @Column
    private Genre genre;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Etudiant(){
        
    }
}
