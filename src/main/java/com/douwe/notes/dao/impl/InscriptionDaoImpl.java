package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import java.util.List;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public class InscriptionDaoImpl extends GenericDao<Inscription, Long> implements IInscriptionDao{

    @Override
    public void deleteActive(Inscription inscription) throws DataAccessException {
        getManager().createNamedQuery("Inscription.deleteActive").setParameter("idPram", inscription.getId());
    }

    @Override
    public List<Inscription> findAllActive() throws DataAccessException {
        return getManager().createNamedQuery("Inscription.findAllActive").getResultList();
    }

    @Override
    public Inscription findInscriptionByEtudiant(Etudiant etudiant, AnneeAcademique academique) throws DataAccessException {
        return ((Inscription)getManager().createNamedQuery("Inscription.findByEtudiant").setParameter("param1", etudiant.getId()).setParameter("param2", academique.getId()).getSingleResult());
    }

    @Override
    public List<Inscription> findInscriptionByEtudiantOrdered(Etudiant etudiant) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Inscription findLastInscription(Etudiant etudiant, Niveau niveau, Option otion, AnneeAcademique annee) throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Inscription> findByAnnee(AnneeAcademique annee) throws DataAccessException {
        return getManager().createNamedQuery("Inscription.findByAnnee").setParameter("idParam", annee.getId()).getResultList();
    }
    
}
