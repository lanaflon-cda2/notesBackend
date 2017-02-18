package com.douwe.notes.dao.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.generic.dao.impl.GenericDao;
import com.douwe.notes.dao.IUtilisateurDao;
import com.douwe.notes.entities.Utilisateur;
import com.douwe.notes.entities.Utilisateur_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public class UtilisateurDaoImpl extends GenericDao<Utilisateur, Long> implements IUtilisateurDao{

    @Override
    public Utilisateur findUtilisateurByLoginAndPassword(String login, String password) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Utilisateur> cq = cb.createQuery(Utilisateur.class);
        Root<Utilisateur> utRoot = cq.from(Utilisateur.class);
        cq.where(cb.and(cb.like(utRoot.get(Utilisateur_.login), login), cb.like(utRoot.get(Utilisateur_.password), password)));
        return getManager().createQuery(cq).getSingleResult();
    }

    @Override
    public Utilisateur findByUsernameAndAuthToken(String username, String token) throws DataAccessException {
        CriteriaBuilder cb = getManager().getCriteriaBuilder();
        CriteriaQuery<Utilisateur> cq = cb.createQuery(Utilisateur.class);
        Root<Utilisateur> utRoot = cq.from(Utilisateur.class);
        cq.where(cb.and(cb.like(utRoot.get(Utilisateur_.login), username), cb.like(utRoot.get(Utilisateur_.token), token)));
        return getManager().createQuery(cq).getSingleResult();
    }
    
}
