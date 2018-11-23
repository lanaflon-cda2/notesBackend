package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.Role;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.resource.IUtilisateurResource;
import com.douwe.notes.service.IUtilisateurService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Path;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/utilisateurs")
public class UtilisateurResource implements IUtilisateurResource{
    
    @Inject
    private IUtilisateurService utilisateurService;

    @Override
    public List<Utilisateur> find() {
        System.out.println("totototototototo");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = (auth == null)? null: (Utilisateur)auth.getPrincipal();
        if(utilisateur.getRole() == Role.ADMINISTRATEUR)
            return utilisateurService.findAll();
        else{
            List<Utilisateur> util = new ArrayList<>();
            util.add(utilisateurService.findOne(utilisateur.getId()));
            return util;
        }
    }

    @Override
    public Utilisateur getLoggedUser(Utilisateur user) {
        System.out.println("Bonjour d'autres problemes mon cher");
        System.out.println("L'utilisateur "+user);
        Utilisateur us = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return us;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur util = (auth == null)? null: (Utilisateur)auth.getPrincipal();
        if(util.getRole() == Role.ADMINISTRATEUR || util.getId() == utilisateur.getId())
            return utilisateurService.create(utilisateur);
        return null;
    }

    @Override
    public Utilisateur update(long id, Utilisateur utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur util = (auth == null)? null: (Utilisateur)auth.getPrincipal();
        if(util.getRole() == Role.ADMINISTRATEUR || util.getId() == id)
            return utilisateurService.update(id,utilisateur);
        return null;
    }

    @Override
    public void delete(long id) {
        utilisateurService.delete(id);
    }

    @Override
    public Map<String, Object> changePassword(long id, String oldPassword, String newPassword) {
        return utilisateurService.changePassword(id, oldPassword, newPassword);
    }

    @Override
    public void activate(long id) {
         utilisateurService.activate(id);
    }

    @Override
    public void reset(long id) {
        utilisateurService.reset(id);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
