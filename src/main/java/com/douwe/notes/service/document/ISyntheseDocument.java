package com.douwe.notes.service.document;

import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface ISyntheseDocument {
    
    public String produireSynthese(Long niveauId, Long optionId,Long academiqueId,Long semestreId, OutputStream stream) throws ServiceException;
}
