package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import lombok.EqualsAndHashCode;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Data
@Entity
@XmlRootElement(name = "unites")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "UE.deleteActive", query = "update UniteEnseignement ue set ue.active = 0 where ue.id = :idParam"),
    @NamedQuery(name = "UE.findAllActive", query = "select ue from UniteEnseignement ue where ue.active=1"),
    @NamedQuery(name = "UE.findByNiveauOption", query = "SELECT ue from UniteEnseignement ue where ue.parcours.id = :idParam")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"CODE","PARCOURS_ID"}), name = "uniteenseignement")
public class UniteEnseignement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @XmlTransient
    private int version;

    @Column(name = "INTITULE")
    private String intitule;

    @Column(name = "CODE")
    private String code;

    @Column
    private boolean hasOptionalChoices;
  
    @ManyToOne(optional = false)
    @XmlTransient
    @JoinColumn(name = "PARCOURS_ID")
    @EqualsAndHashCode.Exclude
    private Parcours parcours;
    
    @ManyToMany
    @EqualsAndHashCode.Exclude
    private List<Cours> cours;

    @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;

    public UniteEnseignement() {

    }
}
