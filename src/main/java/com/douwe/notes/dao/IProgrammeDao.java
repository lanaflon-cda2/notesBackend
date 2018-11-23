package com.douwe.notes.dao;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.IDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Semestre;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public interface IProgrammeDao extends IDao<Programme, Long>{
    
    public void deleteActiv(Programme programme)throws DataAccessException;
    
    public List<Programme> findAllActive() throws DataAccessException;
    

    public List<Programme> findByNiveauOption(Niveau n, Option o, AnneeAcademique academique, Semestre semestre) throws DataAccessException;
    
    Programme findByCours(Cours c, Niveau n, Option o, AnneeAcademique a) throws DataAccessException;

    public List<Programme> findByAnnee(AnneeAcademique annee) throws DataAccessException;
    
}
