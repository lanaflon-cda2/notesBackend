package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.resource.IUtilisateurResource;
import com.douwe.notes.service.IUtilisateurService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Path;

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

    
    
}
