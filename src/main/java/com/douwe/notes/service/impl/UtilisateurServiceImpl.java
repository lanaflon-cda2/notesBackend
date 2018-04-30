package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IUtilisateurDao;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.entities.util.CustomUserDetails;
import com.douwe.notes.service.IUtilisateurService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Stateless
@Named
public class UtilisateurServiceImpl implements IUtilisateurService, UserDetailsService{
    
    @Inject
    private IUtilisateurDao utilisateurDao;

//    @Override
//    public AuthAccessElement findByLoginAndPassword(AuthLoginElement loginElement) throws ServiceException {
//        Utilisateur user;
//        try {
//            user = utilisateurDao.findUtilisateurByLoginAndPassword(loginElement.getUsername(), loginElement.getPassword());
//            if (user != null) {
//                String token = UUID.randomUUID().toString() ;
//                user.setToken(token);
//                utilisateurDao.update(user);
//                return new AuthAccessElement(loginElement.getUsername(), token, user.getRole());
//            }
//               return null;           
//        } catch (DataAccessException ex) {
//            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }

    @Override
    public Utilisateur createOrUpdate(Utilisateur u) throws DataAccessException {
        return utilisateurDao.create(u);
    }
    
//    @Override
//    public boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed) throws ServiceException{
//        try {
//            Utilisateur u = utilisateurDao.findByUsernameAndAuthToken(authId, authToken);
//            if (u != null) {
//                return rolesAllowed.contains(u.getRole());
//            } else {
//                return false;
//            }
//        } catch (DataAccessException ex) {
//            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = utilisateurDao.findByLogin(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found "+ username);
        }
        
        return new CustomUserDetails(user);
    }
}
