/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.resource.impl;

import com.douwe.notes.service.util.ImportationResult;
import com.douwe.notes.service.util.RestaurationError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import com.douwe.notes.service.IDatabaseBackupService;
import com.douwe.notes.resource.IDatabaseBackupResource;
import java.time.Instant;
import org.jboss.logging.Logger;

/**
 *
 * @author guidona
 */
public class DatabaseBackupResource implements IDatabaseBackupResource{
    
    @Inject
    private IDatabaseBackupService databaseBackupService;
    
    @Override
    public Response produireSauvegarde() {
        
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    databaseBackupService.produireSauvegardeDocument(output);
                } catch (Exception e) {
//                    throw new WebApplicationException(e);
                    Logger.getLogger(DatabaseBackupResource.class.getName()).log(Logger.Level.FATAL, this, e);
                }
            }
        };
        Date date = new Date();
        String filename = "sauvegarde_du_"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/");
        return Response.ok(stream).header("x-filename", filename + ".xml").build();
    }

    @Override
    public Response produireSauvegarde(long anneeId) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    databaseBackupService.produireSauvegardeDocument(output, anneeId);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        Date date = new Date();
        String filename = "sauvegarde_du_"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/");
        return Response.ok(stream).header("x-filename", filename + ".xml").build();
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RestaurationError restauration(InputStream fichier) {
        System.out.println("We are in the resource pane");
        System.out.println(fichier);
        return databaseBackupService.restauration(fichier);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
