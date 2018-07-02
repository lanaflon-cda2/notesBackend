package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.resource.IUtilisateurResource;
import com.douwe.notes.service.IUtilisateurService;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Path;
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
    public List<Utilisateur> findAll() {
        return utilisateurService.findAll();
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
        return utilisateurService.create(utilisateur);
    }

    @Override
    public Utilisateur update(long id, Utilisateur utilisateur) {
        return utilisateurService.update(id,utilisateur);
    }

    @Override
    public void delete(long id) {
        utilisateurService.delete(id);
    }

    @Override
    public Map<String, Object> changePassword(long id, String oldPassword, String newPassword) {
        return utilisateurService.changePassword(id,oldPassword, newPassword);
    }

    
    
}
