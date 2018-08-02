package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.resource.ICoursResource;
import com.douwe.notes.service.ICoursService;
import com.douwe.notes.service.IEvaluationService;
import com.douwe.notes.service.ServiceException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/cours")
public class CoursResource implements ICoursResource {

    @Inject
    private ICoursService coursService;

    @Inject
    private IEvaluationService evaluationService;

    public ICoursService getCoursService() {
        return coursService;
    }

    public void setCoursService(ICoursService coursService) {
        this.coursService = coursService;
    }

    public IEvaluationService getEvaluationService() {
        return evaluationService;
    }

    public void setEvaluationService(IEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public Cours createCours(Cours cours) {
        try {
            System.out.println("J'insere le cours \n**************** \n " + cours);
            return coursService.saveOrUpdateCours(cours);
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cours> getAllCours(Principal user) {
        try {
            return coursService.getAllCours();
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Cours getCours(long id) {
        try {
            Cours cours = coursService.findCoursById(id);
            if (cours == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return cours;
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Cours updateCours(long id, Cours cours) {
        try {
            Cours cours1 = coursService.findCoursById(id);
            if (cours1 != null) {
                cours1.setIntitule(cours.getIntitule());
                cours1.setTypeCours(cours.getTypeCours());
                return coursService.saveOrUpdateCours(cours1);
            }
            return null;
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deleteCours(long id) {
        try {
            coursService.deleteCours(id);
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Cours findByIntitule(String intitule) {
        try {
            Cours cours = coursService.findByIntituleAndDepartement(intitule, null);
            if (cours == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return cours;
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Evaluation> getAllEvaluationsByCours(long id) {
        try {
            return evaluationService.getAllEvaluationByCours(id);
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cours> getByParcours(long niveauId, long optionId) {
        try {
            return coursService.findByParcours(niveauId, optionId);
        } catch (ServiceException ex) {
            Logger.getLogger(CoursResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

}
