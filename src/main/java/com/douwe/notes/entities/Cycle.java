package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@XmlRootElement(name = "cycle")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name="cycles")
@NamedQueries({
    @NamedQuery(name = "Cycle.deleteActive",query ="update cycles c set c.active = 0 where c.id = :idParam"),
    @NamedQuery(name = "Cycle.findAllActive",query = "select c from cycles c where c.active = 1"),
    @NamedQuery(name = "Cycle.findByNom", query="select c from cycles c where c.nom = :nameParam")
})
public class Cycle implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @Column (unique = true)
    private String nom;
    
    @Column(unique = true)
    private String diplomeFr;
    
    @Column(unique = true)
    private String diplomeEn;
    
    @OneToMany(mappedBy = "cycle")
    @XmlTransient
    private List<Niveau> niveaux;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public Cycle(){
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.nom);
        hash = 53 * hash + Objects.hashCode(this.diplomeFr);
        hash = 53 * hash + Objects.hashCode(this.diplomeEn);
        hash = 53 * hash + this.active;
        return hash;
    }    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cycle other = (Cycle) obj;
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.diplomeFr, other.diplomeFr)) {
            return false;
        }
        if (!Objects.equals(this.diplomeEn, other.diplomeEn)) {
            return false;
        }
        return true;
    }
    
}