package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.AnneeAcademique_;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Departement_;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Etudiant_;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.Evaluation_;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Inscription_;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Niveau_;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Note_;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Option_;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Parcours_;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Programme_;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.entities.UniteEnseignement_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class EtudiantDaoImpl extends GenericDao<Etudiant, Long> implements IEtudiantDao {

    @Override
    public List<Etudiant> listeEtudiantParDepartement(Departement departement, AnneeAcademique academique) throws DataAccessException {
        return etudiantsByCriteria(departement, academique, null, null, null);
    }

    @Override
    public List<Etudiant> listeEtudiantParDepartementEtOption(Departement departement, AnneeAcademique academique, Option option) throws DataAccessException {
        return etudiantsByCriteria(departement, academique, null, null, option);
    }

    @Override
    public List<Etudiant> listeEtudiantParDepartementEtNiveau(Departement departement, AnneeAcademique academique, Niveau niveau, Option option) throws DataAccessException {
        return etudiantsByCriteria(departement, academique, null, niveau, option);
    }

    @Override
    public List<Etudiant> listeEtudiantParDepartementEtParcours(Departement departement, AnneeAcademique academique, Parcours parcours) throws DataAccessException {
        return etudiantsByCriteria(departement, academique, parcours, null, null);
    }

    private List<Etudiant> etudiantsByCriteria(Departement departement, AnneeAcademique annee, Parcours parcours, Niveau niveau, Option option) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Path<AnneeAcademique> anneePath = inscriptionRoot.get(Inscription_.anneeAcademique);
        Path<Parcours> parcoursPath = inscriptionRoot.get(Inscription_.parcours);
        Path<Niveau> niveauPath = parcoursPath.get(Parcours_.niveau);
        Path<Option> optionPath = parcoursPath.get(Parcours_.option);
        Path<Departement> departPath = optionPath.get(Option_.departement);
        Path<Etudiant> etudiantPath = inscriptionRoot.get(Inscription_.etudiant);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(etudiantPath.get(Etudiant_.active), 1));
        if (departement != null) {
            predicates.add(cb.equal(departPath, departement));
            predicates.add(cb.equal(departPath.get(Departement_.active), 1));
        }
        if (annee != null) {
            predicates.add(cb.equal(anneePath, annee));
            predicates.add(cb.equal(anneePath.get(AnneeAcademique_.active), 1));
        }
        if (parcours != null) {
            predicates.add(cb.equal(parcoursPath, parcours));
            predicates.add(cb.equal(parcoursPath.get(Parcours_.active), 1));
        }
        if (niveau != null) {
            predicates.add(cb.equal(niveauPath, niveau));
            predicates.add(cb.equal(niveauPath.get(Niveau_.active), 1));
        }
        if (option != null) {
            predicates.add(cb.equal(optionPath, option));
            predicates.add(cb.equal(optionPath.get(Option_.active), 1));
        }
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.select(etudiantPath);
        cq.orderBy(cb.asc(etudiantPath.get(Etudiant_.nom)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public void deleteActive(Etudiant etudiant) throws DataAccessException {
        getManager().createNamedQuery("Etudiant.deleteActive").setParameter("idParam", etudiant.getId());
    }

    @Override
    public List<Etudiant> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Etudiant.findAllActive").getResultList();
    }

    @Override
    public Etudiant findByMatricule(String matricule) throws DataAccessException {
        try {
            return (Etudiant) (getManager().createNamedQuery("Etudiant.findByMatricule").setParameter("param", matricule).getSingleResult());
        } catch (NoResultException nre) {

        }
        return null;
    }

    @Override
    public Etudiant findByName(String name) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Etudiant> etudiantRoot = cq.from(Etudiant.class);
        cq.where(cb.and(cb.like(etudiantRoot.get(Etudiant_.nom), name), cb.equal(etudiantRoot.get(Etudiant_.active), 1)));
        cq.select(etudiantRoot);
        return getManager().createQuery(cq).getSingleResult();

    }

    @Override
    public List<Etudiant> listeEtudiantInscritParcours(AnneeAcademique academique, Parcours parcours) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Inscription> inscriptionRoot2 = cq.from(Inscription.class);
        Path<Etudiant> etudiantPath = inscriptionRoot.get(Inscription_.etudiant);
        Path<AnneeAcademique> anneePath = inscriptionRoot.get(Inscription_.anneeAcademique);
        Path<Parcours> parcoursPath = inscriptionRoot2.get(Inscription_.parcours);
        // L'etudiant est inscrit en ce moment
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.le(anneePath.get(AnneeAcademique_.numeroDebut), academique.getNumeroDebut()));
        predicates.add(cb.equal(parcoursPath, parcours));
        predicates.add(cb.equal(etudiantPath, inscriptionRoot2.get(Inscription_.etudiant)));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.anneeAcademique), academique));
        cq.select(etudiantPath);
        cq.distinct(true);
        cq.orderBy(cb.asc(etudiantPath.get(Etudiant_.nom)));
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        return getManager().createQuery(cq).getResultList();

    }

    @Override
    public List<Etudiant> listeEtudiantAvecNotes(AnneeAcademique academique, Niveau niveau, Option option, Cours cours, Session session) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Note> noteRoot = cq.from(Note.class);
        Path<Evaluation> evaluationPath = noteRoot.get(Note_.evaluation);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Inscription> inscriptionRoot2 = cq.from(Inscription.class);
        Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(etudiantPath, inscriptionRoot.get(Inscription_.etudiant)));
        predicates.add(cb.equal(etudiantPath, inscriptionRoot2.get(Inscription_.etudiant)));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(inscriptionRoot.get(Inscription_.parcours).get(Parcours_.niveau).get(Niveau_.cycle), niveau.getCycle()));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.option), option));
        predicates.add(cb.le(inscriptionRoot2.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut), academique.getNumeroDebut()));
        predicates.add(cb.equal(inscriptionRoot.get(Inscription_.anneeAcademique), academique));
        predicates.add(cb.equal(noteRoot.get(Note_.cours), cours));
        predicates.add(cb.equal(noteRoot.get(Note_.anneeAcademique), academique));
        if (session == Session.rattrapage) {
            predicates.add(cb.isTrue(evaluationPath.get(Evaluation_.isExam)));
            predicates.add(cb.equal(noteRoot.get(Note_.session), session));
        }
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.distinct(true);
        cq.orderBy(cb.asc(etudiantPath.get(Etudiant_.nom)));
        cq.select(etudiantPath);
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Etudiant> listeEtudiantAvecNotes(AnneeAcademique academique, Niveau niveau, Option option, Semestre semestre) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Note> noteRoot = cq.from(Note.class);
        Root<Cours> coursRoot = cq.from(Cours.class);
        //Root<CoursUEAnnee> coursUeRoot = cq.from(CoursUEAnnee.class);        
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Inscription> inscriptionRoot2 = cq.from(Inscription.class);
        Root<Programme> programmeRoot = cq.from(Programme.class);
        Path<Evaluation> evaluationPath = noteRoot.get(Note_.evaluation);
        Path<UniteEnseignement> unitePath = programmeRoot.get(Programme_.uniteEnseignement);
        Expression<List<Cours>> coursPath = unitePath.get(UniteEnseignement_.cours);
        //Expression<List<Cours>> totoPath = programmeRoot.get(Programme_.uniteEnseignement).get(UniteEnseignement_.courses);
        Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        List<Predicate> predicates = new ArrayList<>();
        // Les étudiants inscrits dans le parcours ou inscript par le passe et inscrit l'annee en cours et ainsi obtenu une note 
        // a un cours du parcours et semestre l'annee en cours
        //predicates.add(cb.equal(coursUeRoot.get(CoursUEAnnee_.anneeAcademique), academique));
        //predicates.add(cb.equal(coursUeRoot.get(CoursUEAnnee_.uniteEnseignement), programmeRoot.get(Programme_.uniteEnseignement)));
        predicates.add(cb.equal(etudiantPath, inscriptionRoot.get(Inscription_.etudiant)));
        predicates.add(cb.equal(etudiantPath, inscriptionRoot2.get(Inscription_.etudiant)));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.option), option));
        predicates.add(cb.le(inscriptionRoot2.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut), academique.getNumeroDebut()));
        predicates.add(cb.equal(inscriptionRoot.get(Inscription_.anneeAcademique), academique));
        predicates.add(cb.equal(programmeRoot.get(Programme_.anneeAcademique), academique));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.option), option));
        predicates.add(cb.isMember(coursRoot, coursPath));
        //predicates.add(cb.equal(coursRoot, coursUeRoot.get(CoursUEAnnee_.cours)));
        //predicates.add(cb.equal(noteRoot.get(Note_.cours), coursRoot));
        predicates.add(cb.equal(noteRoot.get(Note_.evaluation), evaluationPath));
        if (semestre != null) {
            predicates.add(cb.equal(programmeRoot.get(Programme_.semestre), semestre));
        }
        predicates.add(cb.equal(noteRoot.get(Note_.anneeAcademique), academique));
        predicates.add(cb.equal(noteRoot.get(Note_.cours), coursRoot));
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.distinct(true);
        cq.orderBy(cb.asc(etudiantPath.get(Etudiant_.nom)));
        cq.select(etudiantPath);
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Etudiant> listeEtudiantAvecNotes(AnneeAcademique debutInscription, AnneeAcademique encours, Niveau niveau, Option option, Semestre semestre) throws DataAccessException {
        List<Etudiant> resultat = new ArrayList<>();
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        boolean toto = false;
        //CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Note> noteRoot = cq.from(Note.class);
        Root<Cours> coursRoot = cq.from(Cours.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Programme> programmeRoot = cq.from(Programme.class);
        Join<Inscription, Etudiant> etudiantInscrit = inscriptionRoot.join(Inscription_.etudiant);
        //Path<Evaluation> evaluationPath = noteRoot.get(Note_.evaluation);
        Path<UniteEnseignement> unitePath = programmeRoot.get(Programme_.uniteEnseignement);
        Expression<List<Cours>> coursPath = unitePath.get(UniteEnseignement_.cours);
        //Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(inscriptionRoot.get(Inscription_.anneeAcademique), encours));
        predicates.add(cb.equal(noteRoot.get(Note_.etudiant), etudiantInscrit));
        if (encours.equals(debutInscription)) {
            // Inscrit au parcours pendant l'année academique encours
            predicates.add(cb.equal(inscriptionRoot.get(Inscription_.parcours).get(Parcours_.niveau), niveau));
            predicates.add(cb.equal(inscriptionRoot.get(Inscription_.parcours).get(Parcours_.option), option));
            cq.multiselect(etudiantInscrit, cb.max(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut)));
        } else {
            toto = true;
            predicates.add(cb.notEqual(inscriptionRoot.get(Inscription_.parcours).get(Parcours_.niveau), niveau));
            // j'ai eté inscrits au parcours concerné une année auparavent
            Root<Inscription> inscriptionRoot2 = cq.from(Inscription.class);
            Path<Integer> debutPath = inscriptionRoot2.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut);
            predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.niveau), niveau));
            predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.parcours).get(Parcours_.option), option));
            predicates.add(cb.lessThan(debutPath, encours.getNumeroDebut()));
            predicates.add(cb.equal(etudiantInscrit, inscriptionRoot2.get(Inscription_.etudiant)));
            cq.multiselect(inscriptionRoot2.get(Inscription_.etudiant), cb.max(debutPath));
            //cq.having(cb.equal(cb.max(debutPath), debutInscription.getNumeroDebut()));
        }
        // inscrit ailleurs pendant l'annee academique encours et la derniere inscription au parcours vaut debutInscription
        predicates.add(cb.equal(programmeRoot.get(Programme_.anneeAcademique), encours));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.niveau), niveau));
        predicates.add(cb.equal(programmeRoot.get(Programme_.parcours).get(Parcours_.option), option));
        predicates.add(cb.isMember(coursRoot, coursPath));
        //predicates.add(cb.equal(noteRoot.get(Note_.evaluation), evaluationPath));
        if (semestre != null) {
            predicates.add(cb.equal(programmeRoot.get(Programme_.semestre), semestre));
        }
        predicates.add(cb.equal(noteRoot.get(Note_.anneeAcademique), encours));
        predicates.add(cb.equal(noteRoot.get(Note_.cours), coursRoot));
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.distinct(true);
        cq.orderBy(cb.asc(etudiantInscrit.get(Etudiant_.nom)));
        cq.groupBy(etudiantInscrit);
        //cq.having(cb.equal(cb.max(inscriptionRoot.get(Inscription_.anneeAcademique).get(AnneeAcademique_.numeroDebut)), debutInscription.getNumeroDebut()));
        //cq.select(etudiantPath);

        List<Object[]> res = getManager().createQuery(cq).getResultList();
        for (Object[] re : res) {
            if (!toto || ((Integer) re[1]).equals(debutInscription.getNumeroDebut())) {
                resultat.add((Etudiant) re[0]);
            }
        }
        return resultat;
    }

    @Override
    public List<Etudiant> listeEtudiantsAnnee(Niveau n, Option o, AnneeAcademique a) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Root<Inscription> inscriptionRoot2 = cq.from(Inscription.class);
        Root<Note> noteRoot = cq.from(Note.class);
        Root<Programme> programmeRoot = cq.from(Programme.class);
        Root<Etudiant> etudiantRoot = cq.from(Etudiant.class);
        //Path<Etudiant> etudiantPath = inscriptionRoot.get(Inscription_.etudiant);
        Path<AnneeAcademique> anneePath = inscriptionRoot.get(Inscription_.anneeAcademique);
        Path<AnneeAcademique> anneePath2 = inscriptionRoot2.get(Inscription_.anneeAcademique);
        Path<Parcours> parcoursPath = inscriptionRoot.get(Inscription_.parcours);
        Path<Niveau> niveauPath = parcoursPath.get(Parcours_.niveau);
        Path<Option> optionPath = parcoursPath.get(Parcours_.option);
        //
        
        //Root<Cours> coursRoot = cq.from(Cours.class);
        Path<Cours> coursRoot = noteRoot.get(Note_.cours);
        
        Path<UniteEnseignement> unitePath = programmeRoot.get(Programme_.uniteEnseignement);
        Expression<List<Cours>> coursPath = unitePath.get(UniteEnseignement_.cours);
        //Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        //
        // L'etudiant est inscrit en ce moment
        //List<Predicate> predicates = new ArrayList<>();
        // Le code est pour un étudiant inscrit l'année en question
        //cq.where(cb.and(cb.equal(anneePath, a),cb.equal(niveauPath, n), cb.equal(optionPath, o)));
        // L'étudiant à eu une note cette année
        cq.where(cb.and(
                // L'étudiant était inscrit à été inscrit au parcours en question
                cb.equal(inscriptionRoot.get(Inscription_.etudiant), etudiantRoot),
                cb.equal(niveauPath, n),
                cb.equal(optionPath, o),
                cb.le(anneePath.get(AnneeAcademique_.numeroDebut), a.getNumeroDebut()),
                
                // L'étudiant est inscrit cette année
                cb.equal(inscriptionRoot2.get(Inscription_.etudiant), etudiantRoot),
                cb.equal(anneePath2, a),
                
                // L'étudiant a une note à un cours du parcours cette année
                cb.isMember(coursRoot, coursPath),
                cb.equal(noteRoot.get(Note_.etudiant), etudiantRoot),
                cb.equal(noteRoot.get(Note_.anneeAcademique), a),
                cb.equal(programmeRoot.get(Programme_.anneeAcademique), a),
                cb.equal(programmeRoot.get(Programme_.parcours), parcoursPath)
                
                ));
        
        /*predicates.add(cb.equal(etudiantPath, inscriptionRoot2.get(Inscription_.etudiant)));
        predicates.add(cb.equal(inscriptionRoot2.get(Inscription_.anneeAcademique), a));*/
        cq.select(etudiantRoot);
        cq.distinct(true);
        cq.orderBy(cb.asc(etudiantRoot.get(Etudiant_.nom)));
        
        return getManager().createQuery(cq).getResultList();
    }

}
