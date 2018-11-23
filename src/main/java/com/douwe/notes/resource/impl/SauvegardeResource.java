/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.resource.impl;

import com.douwe.notes.resource.ISauvegardeResource;
import com.douwe.notes.service.ISauvegardeDocumentService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author guidona
 */
public class SauvegardeResource implements ISauvegardeResource{
    
    @Inject
    private ISauvegardeDocumentService sauvegardeService;
    
    @Override
    public Response produireSauvegarde() {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    sauvegardeService.produireSauvegardeDocument(output);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        Date date = new Date();
        String filename = new String("SauvegardeDu"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/"));
        return Response.ok(stream).header("x-filename", filename + ".xml").build();
    }

    @Override
    public Response produireSauvegarde(long anneeId) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    sauvegardeService.produireSauvegardeDocument(output, anneeId);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        Date date = new Date();
        String filename = new String("SauvegardeDu"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/"));
        return Response.ok(stream).header("x-filename", filename + ".xml").build();
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
