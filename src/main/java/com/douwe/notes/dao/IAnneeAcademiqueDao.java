package com.douwe.notes.dao;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.IDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Semestre;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public interface IAnneeAcademiqueDao extends IDao<AnneeAcademique, Long>{
    
    public List<AnneeAcademique> findAllActive() throws DataAccessException;
    
    public AnneeAcademique findLastYearNote(Etudiant etudiant, Niveau n, Cours c, AnneeAcademique fin) throws DataAccessException;
    
    public List<AnneeAcademique> findAllYearWthNote(AnneeAcademique annee, Niveau niveau, Option option, Semestre semestre) throws DataAccessException;
    
    public AnneeAcademique findLastInscriptionYear(Etudiant etudiant, Niveau niveau, Option o) throws DataAccessException;
    
    public AnneeAcademique findLastYearNote(Etudiant etudiant, Niveau niveau, Option o) throws DataAccessException;
    
    public List<AnneeAcademique> findAllYearWthNote(Etudiant etudiant, Niveau niveau, Option o) throws DataAccessException;
    
    public AnneeAcademique findByNumeroDebut(Integer date) throws DataAccessException;
}
