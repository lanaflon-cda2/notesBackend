package com.douwe.notes.resource;

import com.douwe.notes.projection.AuthAccessElement;
import com.douwe.notes.projection.AuthLoginElement;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/auth")
public interface IUtilisateurResource {
    
    @POST
    @Path("login")
    @PermitAll
    public AuthAccessElement login(@Context HttpServletRequest request, AuthLoginElement loginElement);
}
