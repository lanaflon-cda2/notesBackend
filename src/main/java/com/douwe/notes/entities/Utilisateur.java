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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Data
@Entity
@XmlRootElement(name = "utilisateur")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findAllActive", query = "select u from Utilisateur u where u.active = 1 ORDER BY u.nom")
})
    
public class Utilisateur implements Serializable{

    public Utilisateur() {
    }
    
    public Utilisateur(Utilisateur user) {
        super();
        this.id = user.id;
        this.email = user.email;
        this.login = user.login;
        this.nom = user.nom;
        this.password = user.password;
        this.picture = user.picture;
        this.prenom = user.prenom;
        this.departements = user.departements;
        this.role = user.role;
        
    }
    
    @Version
    @JsonIgnore
    private int version;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(columnDefinition = "int default 1")
    private int active;
    
    @Column (nullable = false)
    private String nom;
    
    @Column
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String login;
    
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    
    @Column(name = "UROLE")
    private Role role;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private byte[] picture;
    
    @OneToMany
    @EqualsAndHashCode.Exclude
    private List<Departement> departements;
}
