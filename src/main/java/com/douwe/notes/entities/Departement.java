package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonIgnore;
import java.io.Serializable;
import java.util.List;
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Entity
@XmlRootElement(name="departement")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Departement.deleteActive",query = "update Departement d set d.active = 0 where d.id = :idParam"),
    @NamedQuery(name = "Departement.findAllActive", query = "select d from Departement d where d.active = 1 ORDER BY d.code"),
    @NamedQuery(name = "Departement.getAllOptions", query = "select o from options o where o.departement.id = :idParam"),
    @NamedQuery(name = "Departement.findByCode",query = "SELECT d from Departement d WHERE d.code like :param")
})
public class Departement implements Serializable {
    
    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @XmlTransient
    @Version
    private int version;
    
    @XmlElement
    @Column(unique = true)
    private String code;
    
    @XmlElement
    @Column
    private String frenchDescription;
    
    @XmlElement
    @Column
    private String englishDescription;
    
    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    @XmlTransient
    @OneToMany(mappedBy = "departement")
    private List<Utilisateur> utilisateurs;
    
    public Departement(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    public int getVersion() {
        return version;
    }

    @JsonIgnore
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonIgnore
    public int getActive() {
        return active;
    }

    @JsonIgnore
    public void setActive(int active) {
        this.active = active;
    }

    public String getFrenchDescription() {
        return frenchDescription;
    }

    public void setFrenchDescription(String frenchDescription) {
        this.frenchDescription = frenchDescription;
    }

    public String getEnglishDescription() {
        return englishDescription;
    }

    public void setEnglishDescription(String englishDescription) {
        this.englishDescription = englishDescription;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
    
    

    @Override
    public String toString() {
        return "Departement{" + "id=" + id + ", version=" + version + ", code=" + code + ", frenchDescription=" + frenchDescription + ", englishDescription=" + englishDescription + ", active=" + active + '}';
    }
    
}