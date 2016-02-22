package com.douwe.notes.service;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Local
public interface ISemestreService {
    
    public Semestre saveOrUpdateSemestre(Semestre semestre) throws ServiceException;
    
    public void deleteSemestre(Long id) throws ServiceException;
    
    public Semestre findSemestreById(long id) throws ServiceException;
    
    public List<Semestre> getAllSemestre() throws ServiceException;
    
    public Semestre findByAnneeUE(AnneeAcademique annee, UniteEnseignement ue) throws ServiceException;
}
