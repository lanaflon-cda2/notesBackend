package com.douwe.notes.service;

import com.douwe.notes.entities.Credit;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public interface ICreditService {
    
    Credit saveOrUpdateCredit(Credit credit) throws ServiceException;
    
    void deleteCredit(long id) throws ServiceException;
    
    List<Credit> getAll() throws ServiceException;
    
    List<Credit> getAll(long anneeId, long niveauId, long optionId) throws ServiceException;
    
    Credit findById(long id) throws ServiceException;
    
    
}
