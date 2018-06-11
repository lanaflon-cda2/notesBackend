package com.douwe.notes.service;

import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.service.util.ImportationResult;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IEtudiantService {
    
    public Etudiant saveOrUpdateEtudiant(Etudiant etudiant) throws ServiceException;
    
    public void deleteEtudiant(Long id) throws ServiceException;
    
    public Etudiant findEtudiantById(long id) throws ServiceException;
    
    public List<Etudiant> getAllEtudiant() throws ServiceException;
    
    public List<Etudiant> findByCritiria(long departement, long annee, long niveau, long option) throws ServiceException;
    
    public List<Etudiant> listeEtudiantInscritCours(long annee, long niveau, long option, long cours) throws ServiceException;
    
    public Etudiant findByMatricule(String matricule) throws ServiceException;
    
    public ImportationResult importEtudiants(InputStream stream, Long idAnneeAcademique) throws ServiceException;
    
}
