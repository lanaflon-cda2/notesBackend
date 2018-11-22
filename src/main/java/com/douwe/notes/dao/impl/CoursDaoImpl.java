package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.CoursUEAnnee;
import com.douwe.notes.entities.CoursUEAnnee_;
import com.douwe.notes.entities.Cours_;
import com.douwe.notes.entities.Credit;
import com.douwe.notes.entities.Credit_;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Programme_;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.entities.UniteEnseignement_;
import com.douwe.notes.projection.CoursCredit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class CoursDaoImpl extends GenericDao<Cours, Long> implements ICoursDao {

    @Override
    public void deleteActive(Cours cours) throws DataAccessException {
        getManager().createNamedQuery("Cours.deleteActive").setParameter("idParam", cours.getId());
    }

    @Override
    public List<Cours> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Cours.findAllActive").getResultList();
    }

    @Override
    public Cours findByIntituleAndDepartement(String intitule, Departement departement) throws DataAccessException {
        //return (Cours) (getManager().createNamedQuery("Cours.findByIntitule").setParameter("param", intitule).getSingleResult());
        try{
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<Cours> cq = cb.createQuery(Cours.class);
            Root<Cours> coursRoot = cq.from(Cours.class);
            cq.where(cb.and(cb.like(coursRoot.get(Cours_.intitule), intitule), cb.equal(coursRoot.get(Cours_.departement), departement)));
            return getManager().createQuery(cq).getSingleResult();  
        } catch(NoResultException ex){
            
        }
        return null;
    }

    @Override
    public List<Cours> findByParcoursAnnee(Parcours parcours, AnneeAcademique academique, Semestre semestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Cours> cq = cb.createQuery(Cours.class);
        Root<CoursUEAnnee> coursUERoot = cq.from(CoursUEAnnee.class);
        Root<Cours> courRoot = cq.from(Cours.class);
        Root<Programme> programmeRoot = cq.from(Programme.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(coursUERoot.get(CoursUEAnnee_.anneeAcademique), academique));
        predicates.add(cb.equal(programmeRoot.get(Programme_.anneeAcademique), academique));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours), parcours));
        /**
         * This is where i am
         */
        predicates.add(cb.equal(courRoot.get(Cours_.departement), parcours.getOption().getDepartement()));
        predicates.add(cb.equal(programmeRoot.get(Programme_.uniteEnseignement), coursUERoot.get(CoursUEAnnee_.uniteEnseignement)));
        Path<Cours> coursPath = coursUERoot.get(CoursUEAnnee_.cours);
        if (semestre != null) {
            predicates.add(cb.equal(programmeRoot.get(Programme_.semestre), semestre));
        }
//        Root<Programme> programmeRoot = cq.from(Programme.class);
//        Root<Cours> coursRoot = cq.from(Cours.class);
//        Root<CoursUEAnnee> anneeRoot = cq.from(CoursUEAnnee.class);
//        Path<Parcours> parcoursPath = programmeRoot.get(Programme_.parcours);
//        Path<Semestre> semestrePath = programmeRoot.get(Programme_.semestre);
//        Path<AnneeAcademique> anneePath = programmeRoot.get(Programme_.anneeAcademique);
//        Expression<List<Cours>> listeCoursPath = anneeRoot.get(CoursUEAnnee_.cours);
//        Expression<List<UniteEnseignement>> listeUEPath = anneeRoot.get(CoursUEAnnee_.uniteEnseignements);
//        List<Predicate> predicates = new ArrayList<Predicate>();
//        predicates.add(cb.equal(anneePath, academique));
//        predicates.add(cb.equal(parcoursPath, parcours));
//        predicates.add(cb.equal(anneeRoot.get(CoursUEAnnee_.anneeAcademique), academique));
//        predicates.add(cb.isMember(coursRoot, listeCoursPath));
//        predicates.add(cb.isMember(coursRoot, listeCoursPath));
        
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.select(coursPath);
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Cours> findByUe(UniteEnseignement ue) throws DataAccessException {
        return getManager().createNamedQuery("Cours.findByUE").setParameter("idParam", ue.getId()).getResultList();
    }

    @Override
    public List<Cours> findByParcours(Parcours parcours) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Cours> cq = cb.createQuery(Cours.class);
        Root<Cours> coursRoot = cq.from(Cours.class);
        Root<UniteEnseignement> uniteRoot = cq.from(UniteEnseignement.class);
        ListJoin<UniteEnseignement, Cours> hello = uniteRoot.join(UniteEnseignement_.cours);
        Path<Parcours> parcoursPath = uniteRoot.get(UniteEnseignement_.parcours);
        cq.where(cb.and(cb.equal(parcoursPath, parcours), 
                cb.ge(uniteRoot.get(UniteEnseignement_.active), 1),
                cb.equal(coursRoot.get(Cours_.departement), parcours.getOption().getDepartement())));
        cq.select(hello);
        cq.distinct(true);
        cq.orderBy(cb.asc(hello.get(Cours_.intitule)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<CoursCredit> findCoursCreditByUe(UniteEnseignement ue, AnneeAcademique annee) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<CoursCredit> cq = cb.createQuery(CoursCredit.class);
        Root<UniteEnseignement> uniteRoot = cq.from(UniteEnseignement.class);
        Root<Credit> creditRoot = cq.from(Credit.class);
        Join<Credit, Cours> coursPath = creditRoot.join(Credit_.cours);
        
        cq.where(cb.and(cb.equal(uniteRoot, ue), 
                cb.equal(creditRoot.get(Credit_.academique), annee),
                cb.equal(creditRoot.get(Credit_.cours), coursPath),
                cb.equal(creditRoot.get(Credit_.parcours), ue.getParcours()),
                cb.isMember(coursPath, uniteRoot.get(UniteEnseignement_.cours))));
        cq.multiselect(coursPath,creditRoot.get(Credit_.valeur));  
        
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Cours> findByDepartement(Departement departement)  throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Cours> cq = cb.createQuery(Cours.class);
        Root<Cours> coursRoot = cq.from(Cours.class);
        cq.where(cb.and(cb.equal(coursRoot.get(Cours_.departement), departement), cb.equal(coursRoot.get(Cours_.active), 1)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public Cours findByIntitule(String intitule) throws DataAccessException {
        try {
            return (Cours) (getManager().createNamedQuery("Cours.findByIntitule").setParameter("param", intitule).getSingleResult());
        } catch (Exception e) {
        }
        return null;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
