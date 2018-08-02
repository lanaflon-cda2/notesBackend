package com.douwe.notes.dao;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.IDao;
import com.douwe.notes.entities.Utilisateur;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IUtilisateurDao extends IDao<Utilisateur, Long>{
    
    public Utilisateur findUtilisateurByLoginAndPassword(String login, String password) throws DataAccessException;
    
//    public Utilisateur findByUsernameAndAuthToken(String username, String token) throws DataAccessException;
//    
//    public Utilisateur findByAuthToken(String token) throws DataAccessException;

    public Utilisateur findByLogin(String username);
    
//    public List<Utilisateur> findAllActive() throws DataAccessException; 
}
