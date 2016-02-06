package com.douwe.notes.service.document;

import java.io.OutputStream;
import javax.ejb.Local;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Local
public interface IRelevetDocument {
    
    public void produireRelevet(Long niveauId, Long optionId, Long anneeId, OutputStream stream);
    
}
