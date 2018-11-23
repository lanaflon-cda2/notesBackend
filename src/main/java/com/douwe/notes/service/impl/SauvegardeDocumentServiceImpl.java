/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.notes.service.impl;

import com.douwe.notes.service.document.ISauvegardeDocument;
import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;
import javax.inject.Inject;
import com.douwe.notes.service.ISauvegardeDocumentService;
import java.time.Instant;
import java.util.Date;
import javax.inject.Named;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author guidona
 */
@Named
@Service
@Transactional
public class SauvegardeDocumentServiceImpl implements ISauvegardeDocumentService{
    
    @Inject
    private ISauvegardeDocument sauvegardeDocument;

    @Override
    public String produireSauvegardeDocument(OutputStream stream) throws ServiceException {
        sauvegardeDocument.sauvegardeBD(stream);
        Date date = new Date();
        String filename = new String("SauvegardeDu"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/"));
        return filename;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String produireSauvegardeDocument(OutputStream stream, long anneeId) throws ServiceException {
        sauvegardeDocument.sauvegardeBD(stream, anneeId);
        Date date = new Date();
        String filename = new String("SauvegardeDu"+date.toGMTString().replaceFirst(" GMT", "").replaceAll(" ", "/"));
        return filename;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
