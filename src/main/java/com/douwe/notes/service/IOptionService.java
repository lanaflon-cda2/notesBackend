package com.douwe.notes.service;

import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Option;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IOptionService {
    
    public Option saveOrUpdateOption(Option option) throws ServiceException;
    
    public void deleteOption(Long id) throws ServiceException;
    
    public Option findOptionById(long id) throws ServiceException;
    
    public List<Option> getAllOptions() throws ServiceException;
    
    public Departement getDepartement(Option option) throws ServiceException;
    
     public Option findByCode(String code) throws ServiceException;

    public List<Option> findByDepartementNiveau(long departementId, long niveauId) throws ServiceException;
    
}
