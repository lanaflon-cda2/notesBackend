package com.douwe.notes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@XmlRootElement(name = "evaluationDetail")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({@NamedQuery(name = "EvaluationDetails.findAllActive",query = "SELECT ed FROM EvaluationDetails ed WHERE ed.active = 1"),
@NamedQuery(name = "EvaluationDetails.findByTypeCours",query = "SELECT ed from EvaluationDetails ed WHERE ed.typeCours = :idParam")})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"EVALUATION_ID","TYPECOURS_ID"}))
public class EvaluationDetails implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    @XmlTransient
    private int version;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "EVALUATION_ID")
    private Evaluation evaluation;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPECOURS_ID")
    @XmlTransient
    private TypeCours typeCours;
    
    @Column
    @Min(0)
    @Max(100)
    private Integer pourcentage;
    
     @XmlTransient
    @Column(columnDefinition = "int default 1")
    private int active;
    
    public EvaluationDetails(){
        
    }
}