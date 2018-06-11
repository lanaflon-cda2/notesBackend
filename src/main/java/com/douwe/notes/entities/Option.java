package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Entity(name = "options")
@XmlRootElement(name = "option")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "Option.findDepartement", query = "select d from Departement d where d.id = :idParam"),
        @NamedQuery(name = "Option.findByCode",query = "SELECT o from options o WHERE o.code like :param"),
        @NamedQuery(name = "Option.findAllActive",query = "SELECT o from options o WHERE o.active=1")
})
public class Option implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @XmlTransient
    private int version;

    @Column(unique = true)
    private String code;

    @Column
    private String description;
    
    @Column
    private String descriptionEnglish;

    @ManyToOne(optional = false)
    private Departement departement;

    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;

    public Option() {

    }
}
