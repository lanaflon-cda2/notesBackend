/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author guidona
 */
@Path("/sauvegarde")
public interface ISauvegardeResource {
    @GET
    @Produces("text/xml")
    Response produireSauvegarde();

    @GET
    @Path(value = "{id: \\d+}")
    @Produces("text/xml")
    Response produireSauvegarde(@PathParam(value = "id") long anneeId);
}
