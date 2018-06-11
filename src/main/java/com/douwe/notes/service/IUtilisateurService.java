package com.douwe.notes.service;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.entities.Utilisateur;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IUtilisateurService {
    
    public Utilisateur createOrUpdate(Utilisateur u) throws DataAccessException;
    
//    public AuthAccessElement findByLoginAndPassword(AuthLoginElement loginElement) throws ServiceException;
//    
//    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) throws ServiceException;
    
    public List<Utilisateur> findAll();
}
