package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.ICycleDao;
import com.douwe.notes.entities.Cycle;
import java.util.List;
import javax.persistence.NoResultException;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class CycleDaoImpl extends GenericDao<Cycle, Long> implements ICycleDao{

    @Override
    public void deleteActive(Cycle cycle) throws DataAccessException {
        getManager().createNamedQuery("Cycle.deleteActive").setParameter("idParam", cycle.getId());
    }

    @Override
    public List<Cycle> getAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Cycle.findAllActive").getResultList();
    }

    @Override
    public Cycle findByNom(String nom) throws DataAccessException {
        try{
            return (Cycle) getManager().createNamedQuery("Cycle.findByNom").setParameter("nameParam", nom).getSingleResult();
        } catch(NoResultException ex){
//            Logger.getLogger(CycleDaoImpl.class.getName()).log(Logger.Level.FATAL, ex);
        }
        return null;
    }
    
    
    
}
