package com.douwe.notes.service;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface ISemestreService {
    
    public Semestre saveOrUpdateSemestre(Semestre semestre) throws ServiceException;
    
    public void deleteSemestre(Long id) throws ServiceException;
    
    public Semestre findSemestreById(long id) throws ServiceException;
    
    public List<Semestre> getAllSemestre() throws ServiceException;
    
    public Semestre findByAnneeUE(AnneeAcademique annee, UniteEnseignement ue) throws ServiceException;
}
