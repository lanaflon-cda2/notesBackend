package com.douwe.notes.resource;

import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.web.security.CurrentUser;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/utilisateurs")
public interface IUtilisateurResource {
    
    @GET
    @Produces(value = "application/json")
    public List<Utilisateur> findAll();
    
    @POST
    @Produces(value = "application/json")
    public Utilisateur save(Utilisateur utilisateur);
    
    @Path("{id}")
    @PUT
    @Produces(value = "application/json")
    public Utilisateur update(@PathParam("id")long id, Utilisateur utilisateur);
    
    @Path("{id}")
    @PUT
    @Produces(value = "application/json")
    public void delete(@PathParam("id")long id);
    
    
    @GET
    @Path("/logged")
    @Produces(value = "application/json")
    public Utilisateur getLoggedUser(@CurrentUser Utilisateur user);
    
    
    @POST
    @Path("{id}/changePassword")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = "application/json")
    public Map<String, Object> changePassword(@PathParam("id")long id, @FormDataParam("opassword")String oldPassword, @FormDataParam("npassword")String newPassword);
}
