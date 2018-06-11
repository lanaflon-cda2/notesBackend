package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Entity
@XmlRootElement(name = "enseignement")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Enseignement.deleteActive", query = "update Enseignement e set e.active = 0 where e.id = :idParam"),
    @NamedQuery(name = "Enseignement.findAllActive", query = "select e from Enseignement e where e.active=1")

})
public class Enseignement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @XmlTransient
    private int version;

    @ManyToOne(optional = false)
    private AnneeAcademique anneeAcademique;

    @ManyToMany()
    private List<Enseignant> enseignants;

    @ManyToOne(optional = false)
    private Cours cours;

    @ManyToOne(optional = false)
    private Parcours parcours;

    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;

    public Enseignement() {

    }
}