package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Option;
import com.douwe.notes.service.IDepartementService;
import com.douwe.notes.service.ServiceException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Service
public class DepartementServiceImpl implements IDepartementService{
    @Inject
    private IDepartementDao departementDao;
    
    @Inject
    private ICoursDao coursDao;
    
    @Override
    public Departement saveOrUpdateDepartement(Departement departement) throws ServiceException{
        try {
            if (departement.getId() == null) {
                departement.setActive(1);
                return departementDao.create(departement);
            } else {
                return departementDao.update(departement);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public void deleteDepartement(Long id) throws ServiceException{
        try {
            Departement departement = departementDao.findById(id);
            if (departement != null) {
                departement.setActive(0);
                departementDao.update(departement);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Departement> getAllDepartements() throws ServiceException{
        try {
            return departementDao.findAllActive();
        } catch (DataAccessException ex) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
    }

    public IDepartementDao getDepartementDao() {
        return departementDao;
    }

    public void setDepartementDao(IDepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public ICoursDao getCoursDao() {
        return coursDao;
    }

    public void setCoursDao(ICoursDao coursDao) {
        this.coursDao = coursDao;
    }

    @Override
    public Departement findDepartementById(long id) throws ServiceException{
        try {
            return departementDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
        
    }

    @Override
    public List<Option> getAllOptions(Departement departement) throws ServiceException{
        try {
            return departementDao.getAllOptions(departement);
        } catch (DataAccessException ex) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Departement findByCode(String code) throws ServiceException {
        try {
            return departementDao.findByCode(code);
        } catch (DataAccessException ex) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Cours> getAllCours(long id) throws ServiceException {
        try {
            Departement departement = departementDao.findById(id);
            if(departement != null){
                return coursDao.findByDepartement(departement);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(DepartementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
        return Collections.EMPTY_LIST;
    }
}
