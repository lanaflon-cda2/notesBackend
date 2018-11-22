package com.douwe.notes.dao.impl;
 
import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Option;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class DepartementDaoImpl extends GenericDao<Departement, Long> implements IDepartementDao{

    @Override
    public List<Option> getAllOptions(Departement departement) throws DataAccessException {
      return  getManager().createNamedQuery("Departement.getAllOptions").setParameter("idParam", departement.getId()).getResultList();
    }

    @Override
    public void deleteActive(Departement departement) throws DataAccessException {
        getManager().createNamedQuery("Departement.deleteActive").setParameter("idParam", departement.getId());
    }

    @Override
    public List<Departement> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Departement.findAllActive").getResultList();
    }

    @Override
    public Departement findByCode(String code) throws DataAccessException {
        try{
            return (Departement) getManager().createNamedQuery("Departement.findByCode").setParameter("param", code).getSingleResult();
        } catch(NoResultException ex){
//            Logger.getLogger(SauvegardeDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
