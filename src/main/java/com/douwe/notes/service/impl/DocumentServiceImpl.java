package com.douwe.notes.service.impl;

import com.douwe.notes.service.IDocumentFacadeService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.IProcesVerbalDocument;
import com.douwe.notes.service.document.IRelevetDocument;
import com.douwe.notes.service.document.ISyntheseDocument;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Stateless
public class DocumentServiceImpl implements IDocumentFacadeService {

    
    @EJB
    private IProcesVerbalDocument  procesVerbalDocument;
    
    @EJB
    private ISyntheseDocument syntheseDocument;
    
    @EJB
    private IRelevetDocument relevetDocument;

    

    public IProcesVerbalDocument getProcesVerbalDocument() {
        return procesVerbalDocument;
    }

    public void setProcesVerbalDocument(IProcesVerbalDocument procesVerbalDocument) {
        this.procesVerbalDocument = procesVerbalDocument;
    }

    public ISyntheseDocument getSyntheseDocument() {
        return syntheseDocument;
    }

    public void setSyntheseDocument(ISyntheseDocument syntheseDocument) {
        this.syntheseDocument = syntheseDocument;
    }

    public IRelevetDocument getRelevetDocument() {
        return relevetDocument;
    }

    public void setRelevetDocument(IRelevetDocument relevetDocument) {
        this.relevetDocument = relevetDocument;
    }
    
    @Override
    public String produirePv(Long niveauId, Long optionId, Long coursId, Long academiqueId, int session, OutputStream stream) throws ServiceException {
       return   procesVerbalDocument.produirePv(niveauId, optionId, coursId, academiqueId, session, stream);

    }
    
    @Override
    public String produireSynthese(Long niveauId, Long optionId, Long academiqueId, Long semestreId, OutputStream stream) throws ServiceException {
        return syntheseDocument.produireSynthese(niveauId, optionId, academiqueId, semestreId, stream);
    }

    @Override
    public void produireRelevet(Long niveauId, Long optionId, Long anneeId, OutputStream stream, Long etudinatid) {
        relevetDocument.produireRelevet(niveauId, optionId, anneeId, stream, null);
    }

}
