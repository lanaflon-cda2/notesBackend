package com.douwe.notes.service;

import com.douwe.notes.entities.Parcours;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IParcoursService {

    public Parcours saveOrUpdateParcours(Parcours parcours) throws ServiceException;

    public void deleteParcours(Long id) throws ServiceException;

    public Parcours findParcoursById(long id) throws ServiceException;

    public List<Parcours> getAllParcours() throws ServiceException;
    
    public Parcours findByNiveauOption(long niveauId, long optionId) throws ServiceException;

}
