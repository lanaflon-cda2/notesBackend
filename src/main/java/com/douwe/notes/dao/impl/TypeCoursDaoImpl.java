package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.ITypeCoursDao;
import com.douwe.notes.entities.TypeCours;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class TypeCoursDaoImpl extends GenericDao<TypeCours, Long> implements ITypeCoursDao{

    @Override
    public List<TypeCours> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("TypeCours.findAllActive").getResultList();
    }

    @Override
    public TypeCours findByName(String name) throws DataAccessException {
        try {
            return (TypeCours) getManager().createNamedQuery("TypeCours.findBName").setParameter("param", name).getSingleResult();
        } catch (Exception e) {
        }
        return null;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
