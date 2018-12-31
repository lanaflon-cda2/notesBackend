package com.douwe.notes.service;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.EtudiantCycle;
import com.douwe.notes.projection.EtudiantNiveau;
import com.douwe.notes.projection.EtudiantNotes;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.service.util.DeliberationItem;
import com.douwe.notes.service.util.ImportationResult;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Local
public interface INoteService {
    
    public Note saveOrUpdateNote(Note note) throws ServiceException;
    
    public void deleteNote(Long id) throws ServiceException;
    
    public Note findNoteById(long id) throws ServiceException;
    
    public List<Note> getAllNotes() throws ServiceException;
    
    public Note getNoteEtudiantByEvaluation(Long etudiantId, Long evaluationId, Long coursId,Long anneeId, int session) throws ServiceException;
    
    public List<EtudiantNotes> getAllNotesEtudiants(Niveau niveau, Option option, Cours cours, UniteEnseignement ue, AnneeAcademique academique, Session session) throws ServiceException;
    
    //public List<EtudiantNotes> getAllNotesEtudiants(Niveau niveau, Option option, Cours cours) throws ServiceException;
    
    public EtudiantNotes getNoteEtudiant(String matricule, long cycleId, long coursId, long anneeId) throws ServiceException;
    
    public ImportationResult importNotes(InputStream stream, Long coursId, Long evaluationId, Long anneeId, int session) throws ServiceException;
    
    MoyenneUniteEnseignement getMoyenneUEEtudiant(String matricule, long niveauId, long ueId, long anneeId, long aCourantId) throws ServiceException;

    @Deprecated
    Map<String, MoyenneUniteEnseignement> listeNoteUniteEnseignement(String matricule, long niveauId, long optionId, long semestreId, long anneeId) throws ServiceException;
    Map<String, MoyenneUniteEnseignement> listeNoteUniteEnseignement(String matricule,long niveauId, long anneeId, long anneeCourant, List<UniteEnseignement> ues) throws ServiceException;

    public List<DeliberationItem> listeDeliberation(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean infInclusive, double borneSup, boolean supInclusive, double finale, boolean strict)throws ServiceException;

    public int delibererCours(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean infInclusive, double borneSup, boolean supInclusive, double finale, boolean strict) throws ServiceException;

    public List<Note> listeNoteEtudiant(String matricule, long coursId, long anneeId) throws ServiceException;
    
    public Double calculerMoyenneCycle(String matricule, long cycleId, long anneeId) throws ServiceException;
    
    public int calculerNombreCreditsValides(String matricule, long cycleId, long anneeId) throws ServiceException;
    
    public EtudiantNiveau calculerPerformanceNiveau(String matricule, long niveauId, long anneeId) throws ServiceException;
    
    public EtudiantCycle calculerPerformanceCycle(String matricule, long cycleId, long anneeId) throws ServiceException;

}
