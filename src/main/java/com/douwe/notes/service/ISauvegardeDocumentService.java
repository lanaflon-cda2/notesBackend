/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service;

import java.io.OutputStream;

/**
 *
 * @author guidona
 */
public interface ISauvegardeDocumentService {
    
    public String produireSauvegardeDocument(OutputStream stream) throws ServiceException;
    
    public String produireSauvegardeDocument(OutputStream stream, long anneeId) throws ServiceException;
    
}
