package com.douwe.notes.service.document;

import com.douwe.notes.service.ServiceException;
import java.io.OutputStream;
import javax.ejb.Local;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Local
public interface IProcesVerbalDocument {
    
    public String produirePv(Long niveauId, Long optionId, Long coursId, Long academiqueId, int session,OutputStream stream)throws ServiceException;
}
