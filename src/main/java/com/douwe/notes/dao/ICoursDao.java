package com.douwe.notes.dao;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.IDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.CoursCredit;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public interface ICoursDao extends IDao<Cours, Long>{
    
    public void deleteActive(Cours cours) throws DataAccessException;
    
    public List<Cours> findAllActive() throws DataAccessException;
    
    public Cours findByIntituleAndDepartement(String intitule, com.douwe.notes.entities.Departement departement) throws DataAccessException;
    
    public List<Cours> findByParcoursAnnee(Parcours parcours, AnneeAcademique academique, Semestre semestre) throws DataAccessException;
    
    public List<Cours> findByUe(UniteEnseignement ue) throws DataAccessException;
    
    public List<CoursCredit> findCoursCreditByUe(UniteEnseignement ue, AnneeAcademique annee) throws DataAccessException;

    public List<Cours> findByParcours(Parcours parcours)  throws DataAccessException;

    public List<Cours> findByDepartement(Departement departement) throws DataAccessException;
    
    public Cours findByIntitule(String intitule) throws DataAccessException;
}
