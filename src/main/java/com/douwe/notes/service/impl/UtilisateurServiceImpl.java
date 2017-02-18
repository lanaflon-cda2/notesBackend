package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IUtilisateurDao;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.projection.AuthAccessElement;
import com.douwe.notes.projection.AuthLoginElement;
import com.douwe.notes.service.IUtilisateurService;
import com.douwe.notes.service.ServiceException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Stateless
public class UtilisateurServiceImpl implements IUtilisateurService{
    
    @Inject
    private IUtilisateurDao utilisateurDao;

    @Override
    public AuthAccessElement findByLoginAndPassword(AuthLoginElement loginElement) throws ServiceException {
        Utilisateur user;
        try {
            user = utilisateurDao.findUtilisateurByLoginAndPassword(loginElement.getUsername(), loginElement.getPassword());
            if (user != null) {
                String token = UUID.randomUUID().toString() ;
                user.setToken(token);
                utilisateurDao.update(user);
                return new AuthAccessElement(loginElement.getUsername(), token, user.getRole());
            }
               return null;           
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Utilisateur createOrUpdate(Utilisateur u) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) throws ServiceException{
        try {
            Utilisateur u = utilisateurDao.findByUsernameAndAuthToken(authId, authToken);
            if (u != null) {
                return rolesAllowed.contains(u.getRole());
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
