package com.douwe.notes.service;

import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Enseignant;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IEnseignantService {
    
    public Enseignant saveOrUpdateEnseignant(Enseignant enseignant) throws ServiceException;
    
    public void deleteEnseignant(Long id) throws ServiceException;
    
    public Enseignant findEnseignantById(long id) throws ServiceException;
    
    public List<Enseignant> getAllEnseignants() throws ServiceException;
    
    public List<Enseignant> findByCours(Cours cours) throws ServiceException;
    
}
