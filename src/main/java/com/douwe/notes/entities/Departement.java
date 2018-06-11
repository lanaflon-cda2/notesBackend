package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Data
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

    public Departement(){
        
    }
}