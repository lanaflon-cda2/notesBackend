package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.entities.Cycle;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Niveau_;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class NiveauDaoImpl extends GenericDao<Niveau, Long> implements INiveauDao {

    @Override
    public Niveau findByCode(String code) throws DataAccessException {
        try{
            return (Niveau) (getManager().createNamedQuery("Niveau.findByCode").setParameter("param", code).getSingleResult());
        } catch(NoResultException ex){
//            Logger.getLogger(NiveauDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Niveau findNiveauTerminalParCycle(Cycle cycle) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Niveau> cq = cb.createQuery(Niveau.class);
        Root<Niveau> niveauRoot = cq.from(Niveau.class);
        Path<Cycle> pathCycle = niveauRoot.get(Niveau_.cycle);
        cq.where(cb.and(cb.equal(pathCycle, cycle), cb.equal(niveauRoot.get(Niveau_.active),true),
                cb.equal(niveauRoot.get(Niveau_.terminal), true)));
        return getManager().createQuery(cq).getSingleResult();
    }

}
