package com.douwe.notes.resource;

import com.douwe.notes.entities.Utilisateur;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/utilisateurs")
public interface IUtilisateurResource {
    
    @GET
    @Produces(value = "application/json")
    public List<Utilisateur> findAll();
}
