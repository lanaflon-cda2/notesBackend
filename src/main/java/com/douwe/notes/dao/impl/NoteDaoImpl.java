package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.INoteDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.AnneeAcademique_;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Cours_;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Etudiant_;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Note_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public class NoteDaoImpl extends GenericDao<Note, Long> implements INoteDao{

    @Override
    public List<Note> listeNoteCours(Etudiant etudiant, Cours cours, AnneeAcademique academique) throws DataAccessException{
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Note> cq = cb.createQuery(Note.class);
        Root<Note> noteRoot = cq.from(Note.class);
        Path<Etudiant> etudiantPath = noteRoot.get(Note_.etudiant);
        Path<AnneeAcademique> anneePath = noteRoot.get(Note_.anneeAcademique);
        Path<Cours> coursPath = noteRoot.get(Note_.cours);
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(noteRoot.get(Note_.active), 1));
        if(etudiant != null){
            predicates.add(cb.equal(etudiantPath, etudiant));
            predicates.add(cb.equal(etudiantPath.get(Etudiant_.active), 1));
        }
        if(academique != null){
            predicates.add(cb.equal(anneePath, academique));
            predicates.add(cb.equal(anneePath.get(AnneeAcademique_.active), 1));
        }
        if(cours != null){
            predicates.add(cb.equal(coursPath, cours));
            predicates.add(cb.equal(coursPath.get(Cours_.active), 1));
        }
        cq.select(noteRoot);
        if (predicates.size() > 0) {
            cq.where((predicates.size() == 1) ? predicates.get(0) : cb.and(predicates.toArray(new Predicate[0])));
        }
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public Note getNoteCours(Etudiant etudiant, Evaluation evaluation, Cours cours, AnneeAcademique academique) throws DataAccessException {
        return (Note)(getManager().createNamedQuery("Note.findNoteEvaluationCours").setParameter("param1", etudiant.getId()).setParameter("param2",evaluation.getId()).setParameter("param3",cours.getId()).setParameter("param4",academique.getId()).getSingleResult());
    }
}