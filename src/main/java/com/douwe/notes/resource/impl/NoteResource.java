package com.douwe.notes.resource.impl;

import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Session;
import com.douwe.notes.projection.EtudiantNotes;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.projection.NoteTransfer;
import com.douwe.notes.resource.INoteResource;
import com.douwe.notes.service.IAnneeAcademiqueService;
import com.douwe.notes.service.ICoursService;
import com.douwe.notes.service.IDocumentFacadeService;
import com.douwe.notes.service.INiveauService;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.IOptionService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.util.DeliberationItem;
import com.douwe.notes.service.util.ImportationResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Path("/notes")
public class NoteResource implements INoteResource {

    @Inject
    private INoteService service;

    @Inject
    private IOptionService optionService;

    @Inject
    private IAnneeAcademiqueService anneeAcademiqueService;

    @Inject
    private ICoursService coursService;

    @Inject
    private INiveauService niveauService;

    @Inject
    private IDocumentFacadeService documentService;

    @Inject
    private INoteService noteService;

    public INoteService getService() {
        return service;
    }

    public void setService(INoteService service) {
        this.service = service;
    }

    public IOptionService getOptionService() {
        return optionService;
    }

    public void setOptionService(IOptionService optionService) {
        this.optionService = optionService;
    }

    public IAnneeAcademiqueService getAnneeAcademiqueService() {
        return anneeAcademiqueService;
    }

    public void setAnneeAcademiqueService(IAnneeAcademiqueService anneeAcademiqueService) {
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

    public ICoursService getCoursService() {
        return coursService;
    }

    public void setCoursService(ICoursService coursService) {
        this.coursService = coursService;
    }

    public INiveauService getNiveauService() {
        return niveauService;
    }

    public void setNiveauService(INiveauService niveauService) {
        this.niveauService = niveauService;
    }

    public IDocumentFacadeService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(IDocumentFacadeService documentService) {
        this.documentService = documentService;
    }

    public INoteService getNoteService() {
        return noteService;
    }

    public void setNoteService(INoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public Note createNote(Note note) {
        try {
            return service.saveOrUpdateNote(note);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Note> getAllNotes() {
        try {
            return service.getAllNotes();
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Note getNote(long id) {
        try {
            Note note = service.findNoteById(id);
            if (note == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            return note;
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Note updateNote(long id, Note note) {
        try {
            Note note1 = service.findNoteById(id);
            if (note1 != null) {
               // note1.setAnneeAcademique(note.getAnneeAcademique());
               // note1.setCours(note.getCours());
                //note1.setEtudiant(note.getEtudiant());
                //note1.setEvaluation(note.getEvaluation());
                //note1.setSession(note.getSession());
                note1.setValeur(note.getValeur());
                return service.saveOrUpdateNote(note1);
            }
            return null;
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deleteNote(long id) {
        try {
            service.deleteNote(id);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String afficher(long niveauid, long optionid, long coursid, long anneeid, int ses) {
        try {
            Niveau n = niveauService.findNiveauById(niveauid);
            Option o = optionService.findOptionById(optionid);
            Cours c = coursService.findCoursById(coursid);
            AnneeAcademique a = anneeAcademiqueService.findAnneeById(anneeid);
            Session s = Session.values()[ses];
            List<EtudiantNotes> ets = service.getAllNotesEtudiants(n, o, c, null, a, s);
            for (EtudiantNotes et : ets) {
                System.out.print(String.format("Matricule: %s \t Nom: %s\t", et.getMatricule(), et.getNom()));
                for (Map.Entry<String, Double> e : et.getNote().entrySet()) {
                    System.out.print(String.format("%s - %.2f\t", e.getKey(), e.getValue()));
                }
                System.out.println(String.format("La moyenne : %.2f", et.getMoyenne()));
            }
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Hello";
    }

    @Override
    public OutputStream produirePv() {
        try {
            // Set response headers
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Hello world"));
            document.close();
            return baos;
            //return os;
        } catch (DocumentException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public ImportationResult importNotes(InputStream fichier, FormDataContentDisposition fileDisposition, String Headers, Long coursId, Long niveauId, Long optionId, Long anneeId, String session, boolean importNow) {
        try {
            return noteService.importNotes(fichier, Headers, coursId, niveauId, optionId, anneeId, session, importNow);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public EtudiantNotes noteEtudiant(String matricule, long niveauId, long coursId, long anneeId) {
        try {
            return noteService.getNoteEtudiant(matricule, niveauId, coursId, anneeId);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public MoyenneUniteEnseignement moyenneEtudiant(String matricule, long niveauId, long ueId, long annee) {
        try {
            return noteService.getMoyenneUEEtudiant(matricule, niveauId, ueId, annee, annee);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<String, MoyenneUniteEnseignement> listeNoteUniteEnseignement(String matricule, long niveauId, long optionId, long semestreId, long anneeId) {
        try {
            return noteService.listeNoteUniteEnseignement(matricule, niveauId, optionId, semestreId, anneeId);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<DeliberationItem> listeDeliberation(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean isInfInclusive, double borneSup, boolean isSupInclusive, double finale) {
        try {
            return noteService.listeDeliberation(niveauId, optionId, coursId, anneeId, session, borneInf, isInfInclusive, borneSup, isSupInclusive, finale);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }
    }

    @Override
    public String deliberer(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean isInfInclusive, double borneSup, boolean isSupInclusive, double finale) {
        try {
            return String.valueOf(noteService.delibererCours(niveauId, optionId, coursId, anneeId, session, borneInf, isInfInclusive, borneSup, isSupInclusive, finale));
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }
    }

    @Override
    public List<NoteTransfer> getNoteEtudiantCours(String matricule, long coursId, long anneeId) {
        List<NoteTransfer> result = new ArrayList<NoteTransfer>();
        try {
            List<Note> notes =  noteService.listeNoteEtudiant(matricule, coursId, anneeId);
            for (Note note : notes) {
                result.add(new NoteTransfer(note.getId(),note.getEvaluation().getCode(),
                note.getSession().toString(), note.getValeur()));
            }
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }
        return result;
    }

    @Override
    public Note getNoteEtudiantByEvaluation(Long etudiantId, Long evaluationId, Long coursId, Long anneeId, int session) {
        try {
            return noteService.getNoteEtudiantByEvaluation(etudiantId, evaluationId, coursId, anneeId, session);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }
    }

    @Override
    public Cours getCour(long id) {
        try {
            return coursService.findCoursById(id);
        } catch (ServiceException ex) {
            Logger.getLogger(NoteResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
