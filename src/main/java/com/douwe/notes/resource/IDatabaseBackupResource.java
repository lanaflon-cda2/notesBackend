package com.douwe.notes.resource;

import com.douwe.notes.service.util.RestaurationError;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author guidona
 */
@Path("/")
public interface IDatabaseBackupResource {
    @GET
    @Path(value = "sauvegarde")
    @Produces("text/xml")
    Response produireSauvegarde();

    @GET
    @Path(value = "sauvegarde/{id: \\d+}")
    @Produces("text/xml")
    Response produireSauvegarde(@PathParam(value = "id") long anneeId);
    
    @POST
    @Path(value = "restauration")
    @Produces(value = "application/json")
    RestaurationError restauration(@FormDataParam("fichier") InputStream fichier);
}
