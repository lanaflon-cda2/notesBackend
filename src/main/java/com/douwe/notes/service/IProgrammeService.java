package com.douwe.notes.service;

import com.douwe.notes.entities.Programme;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IProgrammeService {
    
    public Programme saveOrUpdateProgramme(Programme programme) throws ServiceException;
    
    public void deleteProgramme(Long id) throws ServiceException;
    
    public Programme findProgrammeById(long id) throws ServiceException;
    
    public List<Programme> getAllProgrammes() throws ServiceException;
    
    Programme findByCours(long coursId, long niveauId, long optionId , long anneeId) throws ServiceException;

   public List<Programme> findProgrammeByParcours(Long niveauId, Long optionId, Long idAnnee,Long idSemestre) throws ServiceException;

    public List<Programme> findByOptionAnnee(long anneeId, long niveauId, long optionId, long semestreId) throws ServiceException;

    
}
