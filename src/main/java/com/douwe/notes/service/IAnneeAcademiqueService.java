package com.douwe.notes.service;

import com.douwe.notes.entities.AnneeAcademique;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IAnneeAcademiqueService {
    public AnneeAcademique saveOrUpdateAnnee(AnneeAcademique anneeAcademique) throws ServiceException;
    
    public void deleteAnnee(Long id) throws ServiceException;;
    
    public AnneeAcademique findAnneeById(long id) throws ServiceException;;
    
    public List<AnneeAcademique> getAllAnnee() throws ServiceException;;
    
//    public AnneeAcademique findAnneeByString(String annee) throws ServiceException;
}
