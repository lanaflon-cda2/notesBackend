
package com.douwe.notes.service;

import com.douwe.notes.entities.Evaluation;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IEvaluationService {
    
    public Evaluation saveOrUpdateEvaluation(Evaluation evaluation) throws ServiceException;
    
    public void deleteEvaluation(long id) throws ServiceException;
    
    public Evaluation findEvaluationById(long id) throws ServiceException;
    
    public List<Evaluation> getAllEvaluations() throws ServiceException;
    
    public List<Evaluation> getAllEvaluationByCours(Long id) throws ServiceException;
    
}
