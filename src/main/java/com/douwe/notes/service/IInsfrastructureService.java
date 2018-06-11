package com.douwe.notes.service;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cycle;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Niveau;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public interface IInsfrastructureService {

    public Departement saveOrUpdateDepartement(Departement departement) throws ServiceException;

    public void deleteDepartement(Long id) throws ServiceException;

    public Departement findDepartementById(long id) throws ServiceException;

    public List<Departement> getAllDepartements();

    // Les operations en relation avec les cycles
    public Cycle saveOrUpdateCycle(Cycle cycle);

    public void deleteCycle(long id);

    public Cycle findCycleById(long id);

    public List<Cycle> getAllCycles();

    // Les opérations en relation avec les niveaux
    public Niveau saveOrUpdateNiveau(Niveau niveau);

    public void deleteNiveau(long id);

    public Niveau findNiveauById(long id);

    public List<Niveau> getAllNiveaux();

    // Les opérations en relation avec les annees academique
    public AnneeAcademique saveOrUpdateAnneeAcademique(AnneeAcademique anneeAcademique);

    public void deleteAnneeAcademique(Long id);

    public List<AnneeAcademique> getAllAnneeAcademiques();

    public AnneeAcademique findAnneeAcademiqueById(long id);
}
