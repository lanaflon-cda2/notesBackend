package com.douwe.notes.service.document;

import java.io.OutputStream;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IRelevetDocument {
    
    public void produireRelevet(Long niveauId, Long optionId, Long anneeId, OutputStream stream, Long etudiantId);
    
}
