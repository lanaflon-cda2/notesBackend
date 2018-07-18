package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IUtilisateurDao;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.entities.util.CustomUserDetails;
import com.douwe.notes.service.IUtilisateurService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Service
@Transactional
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
            System.out.println("Aucun utilisateur avec username "+username);
            throw new UsernameNotFoundException("User not found "+ username);
        }
        /*BCryptPasswordEncoder be = new BCryptPasswordEncoder();
        System.out.println("L'email de l'utilisateur "+ username + " est "+user.getRole().toString());
        System.out.println("Toto "+be.matches(user.getPassword(), be.encode("admin123")));
        System.out.println("Tata "+be.encode("admin123"));*/
        return new CustomUserDetails(user);
    }

    @Override
    public List<Utilisateur> findAll() {
        try {
            return utilisateurDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Utilisateur create(Utilisateur u) {
        try {
            u.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            return utilisateurDao.create(u);
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Utilisateur update(long id, Utilisateur u) {
        try {
            Utilisateur util = utilisateurDao.findById(id);
            if(util != null){
                util.setDepartements(u.getDepartements());
                util.setEmail(u.getEmail());
                util.setLogin(u.getLogin());
                util.setPrenom(u.getPrenom());
                util.setRole(u.getRole());
                util.setNom(u.getNom());
                return utilisateurDao.update(util);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        try {
            Utilisateur  u = utilisateurDao.findById(id);
            if(u != null)
                utilisateurDao.delete(u);
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Map<String, Object> changePassword(long id, String oldPassword, String newPassword) {
        Map<String, Object> result = new HashMap<>();
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        try {
            Utilisateur user = utilisateurDao.findById(id);
            if(user == null || !bcpe.matches(oldPassword, user.getPassword())){
                result.put("error","Mot de passe incorrect");
            }else{
                user.setPassword(bcpe.encode(newPassword));
                utilisateurDao.update(user);
                result.put("message", "Mot de passe mis à jour avec succès");
                        
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
