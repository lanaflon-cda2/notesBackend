package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Inscription_;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Option_;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Parcours_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.NoResultException;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class OptionDaoImpl extends GenericDao<Option, Long> implements IOptionDao{

    @Override
    public Departement findDepartement(Option option) throws DataAccessException {
       return (Departement) getManager().createNamedQuery("Option.findDepartement").setParameter("idParam", option.getDepartement().getId()).getSingleResult();
    }

    @Override
    public Option findByCode(String code) throws DataAccessException {
        try{
            return (Option)(getManager().createNamedQuery("Option.findByCode").setParameter("param", code).getSingleResult());
        } catch(NoResultException ex){
//            Logger.getLogger(OptionDaoImpl.class.getName()).log(Logger.Level.FATAL, null, ex);
        }
        return null;
    }

    @Override
    public List<Option> findByDepartementNiveau(Departement dep, Niveau niv) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Option> cq = cb.createQuery(Option.class);
        Root<Parcours> parcoursRoot = cq.from(Parcours.class);
        Path<Niveau> niveauPath = parcoursRoot.get(Parcours_.niveau);
        Path<Option> optionPath = parcoursRoot.get(Parcours_.option);
        Path<Departement> departementPath = optionPath.get(Option_.departement);
        cq.where(cb.and(cb.equal(niveauPath, niv),cb.equal(optionPath.get(Option_.active), 1), cb.equal(departementPath, dep)));
        cq.select(optionPath);
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Option> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Option.findAllActive").getResultList();
    }

    @Override
    public Option findByEtudiantNiveau(Etudiant etudiant, Niveau n) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Option> cq = cb.createQuery(Option.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Path<Parcours> parcoursPath = inscriptionRoot.get(Inscription_.parcours);
        cq.where(cb.and(
                cb.equal(parcoursPath.get(Parcours_.niveau), n),
                cb.equal(inscriptionRoot.get(Inscription_.etudiant), etudiant)));
        cq.select(parcoursPath.get(Parcours_.option));
        cq.distinct(true);
        return getManager().createQuery(cq).setMaxResults(1).getSingleResult();
    }
    
}
