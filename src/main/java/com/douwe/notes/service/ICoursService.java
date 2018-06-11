package com.douwe.notes.service;

import com.douwe.notes.entities.Cours;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface ICoursService {
    
    public Cours saveOrUpdateCours(Cours cours) throws ServiceException;
    
    public void deleteCours(Long id) throws ServiceException;
    
    public Cours findCoursById(long id) throws ServiceException;
    
    public List<Cours> getAllCours() throws ServiceException;
    
    public Cours findByIntituleAndDepartement(String intitule, Long departementId) throws ServiceException;
    
    public List<Cours> findByParcoursAnnee(Long idParcours, Long idAnne) throws ServiceException;
    
    public List<Cours> findAllByUe(Long  idUe) throws ServiceException;

    public List<Cours> findByParcours(long niveauId, long optionId) throws ServiceException;
}
