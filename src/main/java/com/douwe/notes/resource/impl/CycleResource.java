package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.Cycle;
import com.douwe.notes.resource.ICycleResource;
import com.douwe.notes.service.ICycleService;
import com.douwe.notes.service.IInsfrastructureService;
import com.douwe.notes.service.ServiceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Path("/cycles")
public class CycleResource implements ICycleResource{

   @EJB
    private ICycleService cycleService;
    
    @Override
    public Cycle createCycle(Cycle cycle) {
        try {
            return cycleService.saveOrUpdateCycle(cycle);
        } catch (ServiceException ex) {
            Logger.getLogger(CycleResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cycle> getAllCycle() {
        try {
            return cycleService.getAllCycles();
        } catch (ServiceException ex) {
            Logger.getLogger(CycleResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Cycle getCycle(long id) {
        try {
            return cycleService.findCycleById(id);
        } catch (ServiceException ex) {
            Logger.getLogger(CycleResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Cycle updateCycle(long id, Cycle cycle) {
        try {
            Cycle c = cycleService.findCycleById(id);
            if(c != null){
                c.setNom(cycle.getNom());
                c.setDiplomeEn(cycle.getDiplomeEn());
                c.setDiplomeFr(cycle.getDiplomeFr());
                return cycleService.saveOrUpdateCycle(c);
            }
            return null;
        } catch (ServiceException ex) {
            Logger.getLogger(CycleResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deleteCycle(long id) {
        try {
            cycleService.deleteCycle(id);
        } catch (ServiceException ex) {
            Logger.getLogger(CycleResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ICycleService getCycleService() {
        return cycleService;
    }

    public void setCycleService(ICycleService cycleService) {
        this.cycleService = cycleService;
    }
    
    
    
}
