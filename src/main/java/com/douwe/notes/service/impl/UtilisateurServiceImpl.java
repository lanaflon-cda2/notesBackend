package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.dao.IUtilisateurDao;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Role;
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
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Transactional
public class UtilisateurServiceImpl implements IUtilisateurService, UserDetailsService{
    
    @Inject
    private IUtilisateurDao utilisateurDao;
    
    @Inject
    private IInscriptionDao inscriptionDao;

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
        if(user == null || user.getActive() == 0){
            System.out.println("Aucun utilisateur avec username "+username);
            throw new UsernameNotFoundException("User not found "+ username);
        }
//        BCryptPasswordEncoder be = new BCryptPasswordEncoder();
//        System.out.println("L'email de l'utilisateur "+ username + " est "+user.getRole().toString());
//        System.out.println("Toto "+be.matches(user.getPassword(), be.encode("admin123")));
//        System.out.println("Tata "+be.encode("admin123"));
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
            System.out.println("L'utilisateur est null? "+ u == null);
            if(u.getRole() == Role.DEPARTEMENT && (u.getDepartements() == null || u.getDepartements().size() > 1)){
                System.out.println("Cannot have a HOD with more than one department");
                return null;
            }
            u.setActive(1);
            u.setPassword(new BCryptPasswordEncoder().encode("password"));
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
            if(util.getRole() == Role.DEPARTEMENT && u.getDepartements().size() > 1){
                System.out.println("Cannot have HOD with more than one department");
                return null;
            }
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
            Utilisateur u = utilisateurDao.findById(id);
            if(u != null){
                u.setActive(0);
                utilisateurDao.update(u);
            }
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

    @Override
    public void activate(long id) {
        try {
            Utilisateur utilisateur = utilisateurDao.findById(id);
            if(utilisateur != null){
                utilisateur.setActive(1);
                utilisateurDao.update(utilisateur);
            }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void reset(long id) {
        try {
            Utilisateur utilisateur = utilisateurDao.findById(id);
            if(utilisateur != null){
                utilisateur.setPassword(new BCryptPasswordEncoder().encode("password"));
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Utilisateur findOne(long id) {
        try {
            return utilisateurDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(UtilisateurServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
