package com.douwe.notes.service;

import com.douwe.notes.entities.TypeCours;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface ITypeCoursService {
    
    public TypeCours saveOrUpdateTpyeCours(TypeCours typeCours) throws ServiceException;
    
    public void deleteTypeCours(Long id) throws ServiceException;
    
    public TypeCours findTypeCoursById(long id) throws ServiceException;
    
    public List<TypeCours> getAllTypeCours() throws ServiceException;
    
}
