package com.douwe.notes.service.document;

import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IProcesVerbalDocument {
    
    /**
     * Produit le Procès verbal d'un cours donné pour un parcours durant une année académique
     * @param niveauId l'identifiant du niveau
     * @param optionId l'identifiant de l'option
     * @param coursId l'identifiant du cours
     * @param academiqueId l'identifiant de l'année académique
     * @param session la session
     * @param stream
     * @return
     * @throws ServiceException 
     */
    public String produirePv(Long niveauId, Long optionId, Long coursId, Long academiqueId, int session,OutputStream stream)throws ServiceException;
}
