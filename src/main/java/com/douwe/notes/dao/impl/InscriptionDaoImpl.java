package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.AnneeAcademique_;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Inscription_;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Repository
public class InscriptionDaoImpl extends GenericDao<Inscription, Long> implements IInscriptionDao {

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
        List<Inscription> inscription = getManager().createNamedQuery("Inscription.findByEtudiant").setParameter("param1", etudiant.getId()).setParameter("param2", academique.getId()).getResultList();
        return inscription.get(0);
    }

    @Override
    public List<Inscription> findInscriptionByEtudiantOrdered(Etudiant etudiant) throws DataAccessException {
        return getManager().createNamedQuery("Inscription.findByEtudiantOrdered").setParameter("idParam", etudiant.getId()).getResultList();
    }

    @Override
    public List<Inscription> findByAnnee(AnneeAcademique annee) throws DataAccessException {
        return getManager().createNamedQuery("Inscription.findByAnnee").setParameter("idParam", annee.getId()).getResultList();
    }

    public Inscription findLastInscription(Etudiant etudiant, Niveau niveau, Option option, AnneeAcademique annee) throws DataAccessException {
        try{CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Inscription> cq = cb.createQuery(Inscription.class);
        Root<Inscription> inscriptionRoot = cq.from(Inscription.class);
        Path<Etudiant> etudiantPath = inscriptionRoot.get(Inscription_.etudiant);
        Path<AnneeAcademique> annePath = inscriptionRoot.get(Inscription_.anneeAcademique);
        Path<Niveau> niveauPath = inscriptionRoot.get(Inscription_.parcours).get(Parcours_.niveau);
        Path<Option> optionPath = inscriptionRoot.get(Inscription_.parcours).get(Parcours_.option);
        cq.where(cb.and(cb.equal(etudiantPath, etudiant),
                        cb.equal(niveauPath, niveau),
                        cb.equal(optionPath, option),
                        cb.le(annePath.get(AnneeAcademique_.numeroDebut), annee.getNumeroDebut())));
        cq.orderBy(cb.desc(annePath.get(AnneeAcademique_.numeroDebut)));
        return getManager().createQuery(cq).setMaxResults(1).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

}
