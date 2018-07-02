package com.douwe.notes.service;

import com.douwe.notes.entities.Utilisateur;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IUtilisateurService {
    
    public Utilisateur create(Utilisateur u);
    
    
    public Utilisateur update(long id, Utilisateur u);
    
    public void delete(long id);
    
//    public AuthAccessElement findByLoginAndPassword(AuthLoginElement loginElement) throws ServiceException;
//    
//    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) throws ServiceException;
    
    public List<Utilisateur> findAll();

    public Map<String, Object> changePassword(long id, String oldPassword, String newPassword);
}
