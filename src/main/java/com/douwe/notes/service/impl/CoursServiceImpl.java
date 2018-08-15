package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.dao.IParcoursDao;
import com.douwe.notes.dao.IUniteEnseignementDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.service.ICoursService;
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
public class CoursServiceImpl implements ICoursService {

    @Inject
    private ICoursDao coursDao;
    
    @Inject
    private IAnneeAcademiqueDao academiqueDao;
    
    @Inject
    private IParcoursDao parcoursDao;
    
    @Inject
    private INiveauDao niveauDao;
    
    @Inject
    private IOptionDao optionDao;
    
    @Inject
    private IDepartementDao departementDao;
            
    
    @Inject
    private IUniteEnseignementDao enseignementDao;

    public ICoursDao getCoursDao() {
        return coursDao;
    }

    public void setCoursDao(ICoursDao coursDao) {
        this.coursDao = coursDao;
    }

    public IParcoursDao getParcoursDao() {
        return parcoursDao;
    }

    public void setParcoursDao(IParcoursDao parcoursDao) {
        this.parcoursDao = parcoursDao;
    }

    public IAnneeAcademiqueDao getAcademiqueDao() {
        return academiqueDao;
    }

    public void setAcademiqueDao(IAnneeAcademiqueDao academiqueDao) {
        this.academiqueDao = academiqueDao;
    }

    public IUniteEnseignementDao getEnseignementDao() {
        return enseignementDao;
    }

    public void setEnseignementDao(IUniteEnseignementDao enseignementDao) {
        this.enseignementDao = enseignementDao;
    }

    public INiveauDao getNiveauDao() {
        return niveauDao;
    }

    public void setNiveauDao(INiveauDao niveauDao) {
        this.niveauDao = niveauDao;
    }

    public IOptionDao getOptionDao() {
        return optionDao;
    }

    public void setOptionDao(IOptionDao optionDao) {
        this.optionDao = optionDao;
    }

    public IDepartementDao getDepartementDao() {
        return departementDao;
    }

    public void setDepartementDao(IDepartementDao departementDao) {
        this.departementDao = departementDao;
    }
    
    

    @Override
    public Cours saveOrUpdateCours(Cours cours) throws ServiceException {

        try {
            if (cours.getId() == null) {
                cours.setActive(1);
                return coursDao.create(cours);
            } else {
                return coursDao.update(cours);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public void deleteCours(Long id) throws ServiceException {
        try {
            Cours cours = coursDao.findById(id);
            if (cours != null) {
                cours.setActive(0);
                coursDao.update(cours);
            }
        } catch (DataAccessException dae) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, dae);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Cours findCoursById(long id) throws ServiceException {
        try {
            return coursDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Cours> getAllCours() throws ServiceException {
        try {
            return coursDao.findAllActive();
        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Cours findByIntituleAndDepartement(String intitule, Long departementId) throws ServiceException {
        try {
            Departement departement = departementDao.findById(departementId);
            Cours cours = coursDao.findByIntituleAndDepartement(intitule, departement);
            return cours;

        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Cours> findByParcoursAnnee(Long idParcours, Long idAnne) throws ServiceException {
        try {
            Parcours parcours = parcoursDao.findById(idParcours);
            
            if(parcours == null){
                throw  new ServiceException("Resource not found");
            }   
            AnneeAcademique academique = academiqueDao.findById(idAnne);
            if(academique == null){
                throw  new ServiceException("Resource not found");
            }
            
            return coursDao.findByParcoursAnnee(parcours, academique, null);
        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cours> findAllByUe(Long idUe) throws ServiceException {
        try {
            UniteEnseignement enseignement = enseignementDao.findById(idUe);
            if(enseignement == null){
                throw  new ServiceException("Ressource not found");
            }
            return coursDao.findByUe(enseignement);
        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cours> findByParcours(long niveauId, long optionId) throws ServiceException {
        try {
            Niveau niveau = niveauDao.findById(niveauId);
            Option option = optionDao.findById(optionId);
            Parcours parcours = parcoursDao.findByNiveauOption(niveau, option);
            return coursDao.findByParcours(parcours);
        } catch (DataAccessException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ServiceException(ex.getMessage(),ex);
        }
        
    }
}
