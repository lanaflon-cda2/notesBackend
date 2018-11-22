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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
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
@XmlRootElement(name = "enseignant")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Enseignant.deleteActive", query = "update Enseignant e set e.active = 0 where e.id = :idParam"),
    @NamedQuery(name = "Enseignant.findAllActive", query = "select e from Enseignant e where e.active=1"),
    @NamedQuery(name = "Enseignant.findByName", query = "select e from Enseignant e where e.nom = :param")
})
public class Enseignant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @XmlTransient
    private int version;

    @Column
    private String nom;

    @ManyToMany(mappedBy = "enseignants")
    @XmlTransient
    @EqualsAndHashCode.Exclude
    private List<Enseignement> enseignements;

    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;

    public Enseignant() {

    }
}
