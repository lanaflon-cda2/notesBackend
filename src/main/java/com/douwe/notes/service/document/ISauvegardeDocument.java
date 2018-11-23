/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.document;

import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;

/**
 *
 * @author guidona
 */
public interface ISauvegardeDocument {
    
    public void sauvegardeBD(OutputStream stream) throws ServiceException;
    
    public void sauvegardeBD(OutputStream stream, long anneId) throws ServiceException;
    
    
}
