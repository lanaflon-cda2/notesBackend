package com.douwe.notes.service;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Inscription;
import java.util.List;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public interface IInscriptionService {
    
    public Inscription saveOrUpdateInscription(Inscription inscription) throws ServiceException;
    
    /*
    public Inscription saveEtudiant(Etudiant etudiant, AnneeAcademique academique, Parcours parcours) throws ServiceException;
    */
    public void deleteInscription(long id) throws ServiceException;
    
    public Inscription findInscriptionById(long id) throws ServiceException;
    
    public List<Inscription> getAllInscriptions() throws ServiceException;
    
    public Inscription inscrireEtudiant(Etudiant etudiant, String codeNiveau, String codeOption, Long anneeId) throws ServiceException;
    
    public Inscription findInscriptionByEtudiant(Etudiant etudiant, AnneeAcademique academique) throws ServiceException;
    
}
