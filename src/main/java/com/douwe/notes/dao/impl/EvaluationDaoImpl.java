package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IEvaluationDao;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Cours_;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.EvaluationDetails;
import com.douwe.notes.entities.EvaluationDetails_;
import com.douwe.notes.entities.Evaluation_;
import com.douwe.notes.entities.TypeCours;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class EvaluationDaoImpl extends GenericDao<Evaluation, Long> implements IEvaluationDao{

    @Override
    public List<Evaluation> evaluationForCourses(Cours cours) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Evaluation> cq = cb.createQuery(Evaluation.class);
        Root<EvaluationDetails> detailsRoot = cq.from(EvaluationDetails.class);
        Root<Cours> coursRoot = cq.from(Cours.class);
        Path<Evaluation> evaluationPath = detailsRoot.get(EvaluationDetails_.evaluation);
        Path<TypeCours> typePath = detailsRoot.get(EvaluationDetails_.typeCours);
        cq.where(cb.and(cb.equal(typePath, coursRoot.get(Cours_.typeCours)),
                cb.equal(coursRoot, cours)));
        cq.select(evaluationPath);
        cq.orderBy(cb.asc(evaluationPath.get(Evaluation_.isExam)));
        return getManager().createQuery(cq).getResultList();
    }

    @Override
    public Evaluation findByCode(String code) throws DataAccessException {
        try{
            CriteriaBuilder cb = getManager().getCriteriaBuilder();
            CriteriaQuery<Evaluation> cq = cb.createQuery(Evaluation.class);
            Root<Evaluation> evaluationRoot = cq.from(Evaluation.class);
            cq.where(cb.and(cb.like(evaluationRoot.get(Evaluation_.code), code),cb.equal(evaluationRoot.get(Evaluation_.active), 1)));
            cq.select(evaluationRoot);
            return getManager().createQuery(cq).getSingleResult();
        } catch (NoResultException ex){
            
        }
        return null;
    }

    @Override
    public Evaluation findExamen() throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Evaluation> cq = cb.createQuery(Evaluation.class);
        Root<Evaluation> evaluationRoot = cq.from(Evaluation.class);
        cq.where(cb.isTrue(evaluationRoot.get(Evaluation_.isExam)));
        cq.select(evaluationRoot);
        return getManager().createQuery(cq).getSingleResult();
    }
    
}
