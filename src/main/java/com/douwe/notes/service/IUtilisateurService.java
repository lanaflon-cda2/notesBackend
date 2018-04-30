package com.douwe.notes.service;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.entities.Utilisateur;
import javax.ejb.Local;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Local
public interface IUtilisateurService {
    
    public Utilisateur createOrUpdate(Utilisateur u) throws DataAccessException;
    
//    public AuthAccessElement findByLoginAndPassword(AuthLoginElement loginElement) throws ServiceException;
//    
//    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) throws ServiceException;
}
