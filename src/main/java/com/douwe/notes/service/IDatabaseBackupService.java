/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service;

import com.douwe.notes.service.util.RestaurationError;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author guidona
 */
public interface IDatabaseBackupService {
    
    public String produireSauvegardeDocument(OutputStream stream) throws ServiceException;
    
    public String produireSauvegardeDocument(OutputStream stream, long anneeId) throws ServiceException;
    
    public RestaurationError restauration(InputStream stream) throws SecurityException;
    
}
