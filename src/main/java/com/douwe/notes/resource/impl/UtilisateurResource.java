package com.douwe.notes.resource.impl;

import com.douwe.notes.projection.AuthAccessElement;
import com.douwe.notes.projection.AuthLoginElement;
import com.douwe.notes.resource.IUtilisateurResource;
import com.douwe.notes.service.IUtilisateurService;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/auth")
public class UtilisateurResource implements IUtilisateurResource{
    
    @EJB
    private IUtilisateurService utilisateurService;

    @Override
    public AuthAccessElement login(HttpServletRequest request, AuthLoginElement loginElement) {
        AuthAccessElement accessElement= null;
        //accessElement = utilisateurService.findByLoginAndPassword(loginElement);
        if (accessElement != null) {
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_ID, accessElement.getAuthId());
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_TOKEN, accessElement.getAuthToken());
        }
        return accessElement;
    }    
    
}
