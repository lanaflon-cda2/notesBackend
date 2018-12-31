package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.AnneeAcademique_;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Etudiant_;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Inscription_;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Niveau_;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Note_;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Parcours_;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Programme_;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public class AnneeAcademiqueDaoImpl extends GenericDao<AnneeAcademique, Long> implements IAnneeAcademiqueDao {

    @Override
    public List<AnneeAcademique> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Annee.findAllActive").getResultList();
    }

//    @Override
//    public AnneeAcademique findByString(String date) throws DataAccessException {
//        return (AnneeAcademique)(getManager().createNamedQuery("Annee.findByString").setParameter("param", date).getSingleResult());
//    }
    @Override
    public AnneeAcademique findLastYearNote(Etudiant etudiant, Niveau n, Cours c, AnneeAcademique fin) throws DataAccessException {
        try {
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<AnneeAcademique> cq = cb.createQuery(AnneeAcademique.class);
            Root<Note> noteRoot = cq.from(Note.class);
            Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
            Path<AnneeAcademique> anneePath = noteRoot.get(Note_.anneeAcademique);
            Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
            
            Path<Cours> coursPath = noteRoot.get(Note_.cours);
            cq.where(cb.and(cb.equal(etudiantPath, etudiant), cb.equal(coursPath, c), 
                    cb.equal(inscriptionRoot.get(Inscription_.etudiant), etudiantPath),
                    cb.lessThanOrEqualTo(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.debut), anneePath.get(AnneeAcademique_.debut)),
                    cb.equal(inscriptionRoot.get(Inscription_.parcours).get(Parcours_.niveau).get(Niveau_.cycle), n.getCycle()),
                    cb.lessThanOrEqualTo(anneePath.get(AnneeAcademique_.debut), fin.getDebut())));
            cq.select(anneePath);
            cq.orderBy(cb.desc(anneePath.get(AnneeAcademique_.debut)));
            return getManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<AnneeAcademique> findAllYearWthNote(AnneeAcademique annee, Niveau niveau, Option option, Semestre semestre) throws DataAccessException {
        List<AnneeAcademique> resultat = new ArrayList<>();
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Note> noteRoot = cq.from(Note.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Inscription> inscriptionRoot1 = cq.from(Inscription.class);
        Root<Programme> programmeRoot = cq.from(Programme.class);
        Path<AnneeAcademique> anneePath = noteRoot.get(Note_.anneeAcademique);
        Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        Path<Cours> coursPath = noteRoot.get(Note_.cours);
        Path<Parcours> parcoursPath = inscriptionRoot.get(Inscription_.parcours);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(parcoursPath.get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(parcoursPath.get(Parcours_.option), option));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.option), option));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(inscriptionRoot.get(Inscription_.etudiant), etudiantPath));        
        predicates.add(cb.isMember(coursPath, programmeRoot.get(Programme_.uniteEnseignement).get(UniteEnseignement_.cours)));
        predicates.add(cb.equal(anneePath, annee));
        // J'écris vraiment des requêtes compliquées
        //predicates.add(cb.equal(parcoursPath.get(Parcours_.niveau).get(Niveau_.cycle), niveau.getCycle()));
        predicates.add(cb.equal(inscriptionRoot1.get(Inscription_.anneeAcademique), annee));
        predicates.add(cb.equal(inscriptionRoot1.get(Inscription_.etudiant), etudiantPath));
        predicates.add(cb.equal(inscriptionRoot1.get(Inscription_.parcours).get(Parcours_.niveau).get(Niveau_.cycle), niveau.getCycle()));
        predicates.add(cb.equal(inscriptionRoot1.get(Inscription_.anneeAcademique), annee));
        if (semestre != null) {
            predicates.add(cb.equal(programmeRoot.get(Programme_.semestre), semestre));
        }
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }

        //cq.select(inscriptionRoot.get(Inscription_.anneeAcademique));
        cq.multiselect(inscriptionRoot.get(Inscription_.anneeAcademique), cb.max(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut)));
        cq.distinct(true);
        cq.orderBy(cb.desc(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut)));
        //cq.groupBy(etudiantPath.get(Etudiant_.id));
        cq.groupBy(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.id));
        cq.having(cb.equal(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut), cb.max(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut))));
        // TODO un moyen vraiment complique de contourner une limitation dans eclipselink
        List<Object[]> result = getManager().createQuery(cq).getResultList();
        for (Object[] result1 : result) {
            resultat.add((AnneeAcademique) result1[0]);
        }
        return resultat;
    }

    @Override
    public AnneeAcademique findLastInscriptionYear(Etudiant etudiant, Niveau niveau, Option option) throws DataAccessException {
        try {
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<AnneeAcademique> cq = cb.createQuery(AnneeAcademique.class);
            Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
            Path<Etudiant> etudiantPath = inscriptionRoot.get(Inscription_.etudiant);
            Path<AnneeAcademique> anneePath = inscriptionRoot.get(Inscription_.anneeAcademique);
            Path<Parcours> parcoursPath = inscriptionRoot.get(Inscription_.parcours);
            cq.where(cb.and(
                    cb.equal(etudiantPath, etudiant), 
                    cb.equal(parcoursPath.get(Parcours_.niveau), niveau),
                    cb.equal(parcoursPath.get(Parcours_.option), option)
                    
                    ));
            cq.select(anneePath);
            cq.orderBy(cb.desc(anneePath.get(AnneeAcademique_.numeroDebut)));
            return getManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public AnneeAcademique findLastYearNote(Etudiant etudiant, Niveau niveau, Option option) throws DataAccessException {
        try {
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<AnneeAcademique> cq = cb.createQuery(AnneeAcademique.class);
            Root<Note> noteRoot = cq.from(Note.class);
            Root<Programme> programmeRoot = cq.from(Programme.class);
            Path<AnneeAcademique> anneePath = noteRoot.get(Note_.anneeAcademique);
            Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
            Path<Cours> coursPath = noteRoot.get(Note_.cours);
            Path<Parcours> parcoursPath = programmeRoot.get(Programme_.parcours);
            cq.where(cb.and(
                    cb.equal(etudiantPath, etudiant), 
                    cb.equal(parcoursPath.get(Parcours_.niveau), niveau),
                    cb.equal(parcoursPath.get(Parcours_.option), option),
                    cb.isMember(coursPath, programmeRoot.get(Programme_.uniteEnseignement).get(UniteEnseignement_.cours))));
            cq.select(anneePath);
            cq.orderBy(cb.desc(anneePath.get(AnneeAcademique_.debut)));
            return getManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<AnneeAcademique> findAllYearWthNote(Etudiant etudiant, Niveau niveau, Option option) throws DataAccessException {
        try {
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<AnneeAcademique> cq = cb.createQuery(AnneeAcademique.class);
            Root<Note> noteRoot = cq.from(Note.class);
            Root<Programme> programmeRoot = cq.from(Programme.class);
            Path<AnneeAcademique> anneePath = noteRoot.get(Note_.anneeAcademique);
            Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
            Path<Cours> coursPath = noteRoot.get(Note_.cours);
            Path<Parcours> parcoursPath = programmeRoot.get(Programme_.parcours);
            cq.where(cb.and(
                    cb.equal(etudiantPath, etudiant), 
                    cb.equal(parcoursPath.get(Parcours_.niveau), niveau),
                    cb.equal(parcoursPath.get(Parcours_.option), option),
                    cb.isMember(coursPath, programmeRoot.get(Programme_.uniteEnseignement).get(UniteEnseignement_.cours))));
            cq.select(anneePath);
            cq.orderBy(cb.desc(anneePath.get(AnneeAcademique_.debut)));
            return getManager().createQuery(cq).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public AnneeAcademique findByNumeroDebut(Integer date) throws DataAccessException {
        try{
            return (AnneeAcademique)(getManager().createNamedQuery("Annee.findByNumeroAnnee").setParameter("param", date).getSingleResult());
        } catch (NoResultException ex){
            
        }
        return null;
    }
}
