package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.ISemestreDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.service.ISemestreService;
import com.douwe.notes.service.ServiceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Service
@Transactional
public class SemestreServiceImpl implements ISemestreService {

    @Inject
    private ISemestreDao semestreDao;

    public ISemestreDao getSemestreDao() {
        return semestreDao;
    }

    public void setSemestreDao(ISemestreDao semestreDao) {
        this.semestreDao = semestreDao;
    }

    @Override
    public Semestre saveOrUpdateSemestre(Semestre semestre) throws ServiceException {
        try {
            if (semestre.getId() == null) {
                semestre.setActive(1);
                return semestreDao.create(semestre);
            } else {
                return semestreDao.update(semestre);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(SemestreServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public void deleteSemestre(Long id) throws ServiceException {
        try {
            Semestre semestre = semestreDao.findById(id);
            if (semestre != null) {
                semestreDao.delete(semestre);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(SemestreServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Semestre findSemestreById(long id) throws ServiceException {
        try {
            return semestreDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(SemestreServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Semestre> getAllSemestre() throws ServiceException {
        try {
            return semestreDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(SemestreServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Semestre findByAnneeUE(AnneeAcademique annee, UniteEnseignement ue) throws ServiceException {
        try {
            return semestreDao.findByAnneeUE(annee, ue);
        } catch (DataAccessException ex) {
            Logger.getLogger(SemestreServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }
}
