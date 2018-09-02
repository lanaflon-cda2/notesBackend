package com.douwe.notes.service.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.dao.ICycleDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.dao.IEvaluationDao;
import com.douwe.notes.dao.IEvaluationDetailsDao;
import com.douwe.notes.dao.IInscriptionDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.dao.INoteDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.dao.IParcoursDao;
import com.douwe.notes.dao.ISemestreDao;
import com.douwe.notes.dao.IUniteEnseignementDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Cycle;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.EvaluationDetails;
import com.douwe.notes.entities.Inscription;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Note;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.CoursCredit;
import com.douwe.notes.projection.EtudiantCycle;
import com.douwe.notes.projection.EtudiantNiveau;
import com.douwe.notes.projection.EtudiantNotes;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.projection.UEnseignementCredit;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.impl.DocumentUtil;
import com.douwe.notes.service.util.DeliberationItem;
import com.douwe.notes.service.util.ImportationError;
import com.douwe.notes.service.util.ImportationResult;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Service
@Transactional
public class NoteServiceImpl implements INoteService {

    @Inject
    private IAnneeAcademiqueDao anneeAcademiqueDao;

    @Inject
    private INoteDao noteDao;

    @Inject
    private IEvaluationDao evaluationDao;

    @Inject
    private IEtudiantDao etudiantDao;

    @Inject
    private IEvaluationDetailsDao evaluationDetailsDao;

    @Inject
    private ICoursDao coursDao;

    @Inject
    private IAnneeAcademiqueDao academiqueDao;

    @Inject
    private IUniteEnseignementDao uniteEnseignementDao;

    @Inject
    private INiveauDao niveauDao;

    @Inject
    private ISemestreDao semestreDao;

    @Inject
    private IOptionDao optionDao;

    @Inject
    private ICycleDao cycleDao;

    @Inject
    private IDepartementDao departementDao;

    @Inject
    private IInscriptionDao inscriptionDao;

    @Inject
    private IParcoursDao parcoursDao;

    public IParcoursDao getIParcoursDao() {
        return parcoursDao;
    }

    public void setParcoursDao(IParcoursDao parcoursDao) {
        this.parcoursDao = parcoursDao;
    }

    public IInscriptionDao getIInscriptionDao() {
        return inscriptionDao;
    }

    public INoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(INoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public IEvaluationDao getEvaluationDao() {
        return evaluationDao;
    }

    public void setEvaluationDao(IEvaluationDao evaluationDao) {
        this.evaluationDao = evaluationDao;
    }

    public IEtudiantDao getEtudiantDao() {
        return etudiantDao;
    }

    public void setEtudiantDao(IEtudiantDao etudiantDao) {
        this.etudiantDao = etudiantDao;
    }

    public IEvaluationDetailsDao getEvaluationDetailsDao() {
        return evaluationDetailsDao;
    }

    public void setEvaluationDetailsDao(IEvaluationDetailsDao evaluationDetailsDao) {
        this.evaluationDetailsDao = evaluationDetailsDao;
    }

    public ICoursDao getCoursDao() {
        return coursDao;
    }

    public void setCoursDao(ICoursDao coursDao) {
        this.coursDao = coursDao;
    }

    public IAnneeAcademiqueDao getAcademiqueDao() {
        return academiqueDao;
    }

    public void setAcademiqueDao(IAnneeAcademiqueDao academiqueDao) {
        this.academiqueDao = academiqueDao;
    }

    public IUniteEnseignementDao getUniteEnseignementDao() {
        return uniteEnseignementDao;
    }

    public void setUniteEnseignementDao(IUniteEnseignementDao uniteEnseignementDao) {
        this.uniteEnseignementDao = uniteEnseignementDao;
    }

    public INiveauDao getNiveauDao() {
        return niveauDao;
    }

    public void setNiveauDao(INiveauDao niveauDao) {
        this.niveauDao = niveauDao;
    }

    public ISemestreDao getSemestreDao() {
        return semestreDao;
    }

    public void setSemestreDao(ISemestreDao semestreDao) {
        this.semestreDao = semestreDao;
    }

    public IOptionDao getOptionDao() {
        return optionDao;
    }

    public void setOptionDao(IOptionDao optionDao) {
        this.optionDao = optionDao;
    }

    public IDepartementDao getDepartementDao() {
        return departementDao;
    }

    public void setDepartementDao(IDepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public ICycleDao getCycleDao() {
        return cycleDao;
    }

    public void setCycleDao(ICycleDao cycleDao) {
        this.cycleDao = cycleDao;
    }

    public void setInscriptionDao(IInscriptionDao inscriptionDao) {
        this.inscriptionDao = inscriptionDao;
    }

    @Override
    public Note saveOrUpdateNote(Note note) throws ServiceException {
        try {
            if (note.getId() == null) {
                note.setActive(1);
                return noteDao.create(note);
            } else {
                return noteDao.update(note);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }

    }

    @Override
    public void deleteNote(Long id) throws ServiceException {
        try {
            Note note = noteDao.findById(id);
            if (note != null) {
                noteDao.delete(note);
            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public Note findNoteById(long id) throws ServiceException {
        try {
            return noteDao.findById(id);
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<Note> getAllNotes() throws ServiceException {
        try {
            return noteDao.findAll();
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("La ressource demandée est introuvable");
        }
    }

    @Override
    public List<EtudiantNotes> getAllNotesEtudiants(Niveau niveau, Option option, Cours cours, UniteEnseignement ue, AnneeAcademique academique, Session session) throws ServiceException {

        try {
            return listeNoteEtudiant(cours, academique, niveau, option, session);
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }

    }

    private List<EtudiantNotes> listeNoteEtudiant(Cours cours, AnneeAcademique academique, Niveau niveau, Option option, Session session) throws DataAccessException {
        List<EtudiantNotes> result = new ArrayList<>();
        Map<String, Integer> calc = getEvaluationDetails(cours);

        // recuperer les listes des  étudiants du parcours
        //List<Etudiant> etudiants = etudiantDao.listeEtudiantParDepartementEtNiveau(null, academique, niveau, option);
        List<Etudiant> etudiants = etudiantDao.listeEtudiantAvecNotes(academique, niveau, option, cours, session);
        //System.out.println("Bravo j'ai trouve " + etudiants.size() + " etudiants");
        for (Etudiant etudiant : etudiants) {
            EtudiantNotes et = rechercherNoteEtudiant(etudiant, cours, academique, session, calc);
            result.add(et);
        }
        return result;
    }

    private EtudiantNotes rechercherNoteEtudiant(Etudiant etudiant, Cours cours, AnneeAcademique academique, Session session, Map<String, Integer> calc) throws DataAccessException {
        EtudiantNotes et = new EtudiantNotes();
        et.setMatricule(etudiant.getMatricule());
        et.setNom(etudiant.getNom());
        Map<String, Double> notes = new HashMap<>();
        List<Note> nn = noteDao.listeNoteCours(etudiant, cours, academique, session);
        nn.forEach((Note nn1) -> {
            Evaluation e = nn1.getEvaluation();
            if (e.isExam() && nn1.getSession() == Session.normale) {
                if (!notes.containsKey(e.getCode())) {
                    notes.put(nn1.getEvaluation().getCode(), nn1.getValeur());
                }
            } else {
                notes.put(nn1.getEvaluation().getCode(), nn1.getValeur());
            }
        });
        et.setNote(notes);
        // TODO I need to review this
        et.setAnnee(academique);
        et.setDetails(calc);
        return et;
    }

    /*
    private Optional<Double> rechercherNoteEtudiant(Etudiant etudiant, Cours cours, Session session) throws DataAccessException {
        Map<String, Integer> calc = getEvaluationDetails(cours);
        List<Inscription> inscription = inscriptionDao.findInscriptionByEtudiantOrdered(etudiant);
        if (inscription.size() > 1) {
            for (Inscription ins : inscription) {
                if (rechercherNoteEtudiant(etudiant, cours, ins.getAnneeAcademique(), session, calc).getMoyenne().isPresent()) {
                    return rechercherNoteEtudiant(etudiant, cours, ins.getAnneeAcademique(), session, calc).getMoyenne();
                }
            }
        }
        return Optional.empty();
    }

     */
    private Optional<Double> rechercherNoteEtudiant(Etudiant etudiant, Cours cours, AnneeAcademique annee) throws DataAccessException {
        Map<String, Integer> calc = getEvaluationDetails(cours);
        //List<Inscription> inscription = inscriptionDao.findInscriptionByEtudiantOrdered(etudiant);
        return rechercherNoteEtudiant(etudiant, cours, annee, null, calc).getMoyenne();
    }

    private boolean etudiantADejaValider(Etudiant etudiant, Niveau niveau, Option option, Cours cours, AnneeAcademique annee) throws DataAccessException {
        AnneeAcademique an = anneeAcademiqueDao.findLastYearNote(etudiant, niveau, cours, annee);
        Optional<Double> moyenne = rechercherNoteEtudiant(etudiant, cours, an);
        return moyenne.isPresent() && moyenne.get() >= 10;

        /*
        This implementation is not correct
        Optional<Double> moyRattrapageAnneeDernier = rechercherNoteEtudiant(etudiant, cours, Session.rattrapage);
        Optional<Double> moyNormaleAnneeDernier = rechercherNoteEtudiant(etudiant, cours, Session.normale);
        Optional<Double> moyNormaleAnneeEnCours = rechercherNoteEtudiant(etudiant, cours, annee, Session.normale);
        Optional<Double> moyRattrapageAnneeEnCours = rechercherNoteEtudiant(etudiant, cours, annee, Session.normale);
        if (moyNormaleAnneeDernier.isPresent() && moyNormaleAnneeDernier.get() >= 10.0) {
            return true;
        }
        if (moyRattrapageAnneeDernier.isPresent() && moyRattrapageAnneeDernier.get() >= 10.0) {
            return true;
        }
        if (moyRattrapageAnneeEnCours.isPresent() && moyRattrapageAnneeEnCours.get() >= 10.0) {
            return true;
        }
        return moyNormaleAnneeEnCours.isPresent() && moyNormaleAnneeEnCours.get() >= 10.0;
         */
        
    }

    private boolean etudiantPeutFaireCeCours(Etudiant etudiant, Cours cours, Niveau n, Option o, AnneeAcademique academique) throws DataAccessException {
        /*
        This implementation was not correct since someone can go an upper level and still pass the course
        Inscription inscription = inscriptionDao.findInscriptionByEtudiant(etudiant, academique);
        Parcours parcours = parcoursDao.findByNiveauOption(n, o);
        return inscription.getParcours() == parcours;
         */
        return inscriptionDao.findLastInscription(etudiant, n, o, academique) != null;
    }

    private Map<String, Integer> getEvaluationDetails(Cours cours) throws DataAccessException {
        Map<String, Integer> calc = new HashMap<>();
        List<EvaluationDetails> details = evaluationDetailsDao.findByTypeCours(cours.getTypeCours());
        details.forEach((detail) -> {
            calc.put(detail.getEvaluation().getCode(), detail.getPourcentage());
        });
        return calc;
    }

    // TODO This method is very long. I need to reduce the size somehow
    @Override
    public ImportationResult importNotes(InputStream stream, String Headers, Long coursId, Long niveauId, Long optionId, Long anneeId, String session, boolean importNow) throws ServiceException {
        ImportationResult result = new ImportationResult();
        List<ImportationError> erreurs = new ArrayList<>();
        JSONObject header = new JSONObject(Headers);
        JSONObject sessionObj = new JSONObject(session);
        try {
            Cours cours = coursDao.findById(coursId);
            Option option = optionDao.findById(optionId);
            Niveau niveau = niveauDao.findById(niveauId);
//            Session s = Session.values()[session];
            List<Evaluation> evaluations = evaluationDao.evaluationForCourses(cours);
            AnneeAcademique academique = academiqueDao.findById(anneeId);
            /*XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);*/
            Workbook workbook = WorkbookFactory.create(stream);
            Map<String, Evaluation> mapEvaluations = new HashMap<>();
            evaluations.forEach(e -> mapEvaluations.put(e.getCode(), e));
            final Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean hdr = true;
            Row row;
            String matricule = null;
            Etudiant etudiant;
            int count = 0, index = 1;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                // I don't understand why. The number of columns might not be 5 all the time
                if (/*row.getPhysicalNumberOfCells() == 5 &&*/hdr) {
                    row = rowIterator.next();
                    hdr = false;
                }
                Cell cell = row.getCell(header.getInt("Matricule"));
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    matricule = cell.getStringCellValue();
                    if (!matricule.matches("[0-9]{2}[A-Z][0-9]{3}[S,P]")) {
                        System.out.println("Le matricule est " + cell.getStringCellValue());
                        erreurs.add(new ImportationError(index, String.format("Le matricule %s est incorrect", matricule)));
                        continue;
                    }
                }
                // I need to find a better way to deal with this.
                etudiant = etudiantDao.findByMatricule(matricule);

                if (etudiant == null) {
                    System.out.println("The student does not exist");
                    erreurs.add(new ImportationError(index, String.format("Le matricule %s est incorrect", matricule)));
                    continue;
                }
                // The student might be in the list but does not have any mark
                // I need to read all the marks available
                Map<String, Double> map = new HashMap<>();
                for (String key : header.keySet()) {
                    System.out.println("This is a key " + key);
                    if (!"Matricule".equalsIgnoreCase(key)) {
                        try {
                            int intIndex = Integer.valueOf(header.getString(key));
                            Cell c = row.getCell(intIndex);
                            if ((c == null) || (c.getCellType() == Cell.CELL_TYPE_BLANK)) {
                                System.out.println("Test null or blank");
                                continue;
                            }
                            if (c.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                                System.out.println("Test is not numeric");
                                erreurs.add(new ImportationError(index, String.format("La note de %s de %s  est invalide", key, etudiant.getNom())));
                                continue;
                            }
                            Double note = c.getNumericCellValue();
                            System.out.println("La note est " + note);
                            map.put(key, note);
                        } catch (NumberFormatException nfe) {

                        }
                    }
                }
                if (map.isEmpty()) {
                    System.out.println("The map is empty");
                    continue;
                }
                // L'étudiant est il inscrit dans le parcours?
                if (!etudiantPeutFaireCeCours(etudiant, cours, niveau, option, academique)) {
                    erreurs.add(new ImportationError(index, String.format("Etudiant  %s n'est pas du parcours", etudiant.getNom())));
                    continue;
                }
                // L'étudiant a t il deja validé le cours par le passé?
                if (etudiantADejaValider(etudiant, niveau, option, cours, academique)) {
                    erreurs.add(new ImportationError(index, String.format("Etudiant %s a valide", etudiant.getNom())));
                    continue;
                }
                // A ce moment il n'ya aucun souci. L'on peut proceder a l'importation des notes 
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    String codeEvaluation = entry.getKey();
                    double valeurNote = entry.getValue();
                    System.out.println(String.format("Traitement de la note %s avec la valeur %.2f", codeEvaluation, valeurNote));
                    if ((valeurNote < 0) || (valeurNote > 20)) {
                        erreurs.add(new ImportationError(index, String.format("La valeur %.2f de l'evaluation %s est invalide", valeurNote, codeEvaluation)));
                        continue;
                    }
                    if (importNow) {
                        Evaluation eval = mapEvaluations.get(codeEvaluation);
                        Note note = new Note();
                        note.setValeur(valeurNote);
                        note.setActive(1);
                        note.setAnneeAcademique(academique);
                        note.setCours(cours);
                        note.setEtudiant(etudiant);
                        note.setEvaluation(eval);
                        if (eval.isExam()) {
                            note.setSession(Session.values()[sessionObj.getInt(eval.getCode())]);
                        }
                        try {
                            noteDao.create(note);
                        } catch (DataAccessException ex) {
                            erreurs.add(new ImportationError(index, ex.getMessage()));
                        }
                    }
                }
                count++;
                index++;
            }
            result.setNombreImporte(count);
            result.setErreurs(erreurs);
        } catch (DataAccessException | IOException | InvalidFormatException ex) {
            Logger.getLogger(NoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private Note insertNote(String etudiantMatricule, String nomEtudiant, String codeEvaluation, String coursIntitule, Long anneeId, double valeur, int session, Long departementId) {

        try {
            Etudiant etudiant;
            if (etudiantMatricule != null) {
                etudiant = etudiantDao.findByMatricule(etudiantMatricule);
            } else {
                etudiant = etudiantDao.findByName(nomEtudiant);
            }

            Evaluation eval = evaluationDao.findByCode(codeEvaluation);

            Departement departement = departementDao.findById(departementId);

            Cours cours = coursDao.findByIntituleAndDepartement(coursIntitule, departement);

            AnneeAcademique academique = academiqueDao.findById(anneeId);

            Note note = new Note();
            note.setActive(1);
            note.setAnneeAcademique(academique);
            note.setCours(cours);
            note.setEtudiant(etudiant);
            note.setEvaluation(eval);
            Session s = Session.values()[session];
            note.setSession(s);
            note.setValeur(valeur);
            return noteDao.create(note);

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public EtudiantNotes getNoteEtudiant(String matricule, long niveauId, long coursId, long anneeId) throws ServiceException {
        EtudiantNotes result = null;
        try {
            Etudiant etudiant = etudiantDao.findByMatricule(matricule);
            Cours c = coursDao.findById(coursId);
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            //System.out.println("L'année donne "+annee);
            Niveau niveau = niveauDao.findById(niveauId);
            try {
                annee = academiqueDao.findLastYearNote(etudiant, niveau, c, annee);

            } catch (NoResultException nre) {
                annee = null;
            }
            if (annee != null) {
                //System.out.println(String.format("Pour l'étudiant %s l'annee donne %s et le cours est %s",etudiant.getNom(), annee.toString(), c.getIntitule()));
                result = new EtudiantNotes();
                result.setAnnee(annee);
                List<Note> notes = noteDao.getNoteCours(etudiant, c, annee);
                result.setDetails(getEvaluationDetails(c));
                result.setNom(etudiant.getNom());
                result.setMatricule(etudiant.getMatricule());
                Session s = Session.normale;
                boolean status = false;
                Map<String, Double> map = new HashMap<>();
                for (Note note : notes) {
                    if (note.getEvaluation().isExam()) {
                        if (!status) {
                            status = (note.getSession() == Session.rattrapage);
                            map.put(note.getEvaluation().getCode(), note.getValeur());
                            s = note.getSession();
                        }
                    } else {
                        map.put(note.getEvaluation().getCode(), note.getValeur());
                    }
                }
                result.setNote(map);
                result.setSession(s);

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public MoyenneUniteEnseignement getMoyenneUEEtudiant(String matricule, long niveauId, long ueId, long anneeId, long aCourantId) throws ServiceException {
        MoyenneUniteEnseignement result = null;
        try {
            UniteEnseignement ue = uniteEnseignementDao.findById(ueId);
            // TODO I need to come back here and figure out something
            AnneeAcademique annee = null;
            if (aCourantId > 0) {
                annee = academiqueDao.findById(aCourantId);
            }
            result = new MoyenneUniteEnseignement(ue.isHasOptionalChoices());
            // TODO I need to find out a way not to issue this query for every student
            List<CoursCredit> liste = coursDao.findCoursCreditByUe(ue, annee);
            for (CoursCredit cours : liste) {
                EtudiantNotes n = getNoteEtudiant(matricule, niveauId, cours.getCours().getId(), anneeId);
                if (n != null) {
                    result.getCredits().put(cours.getCours().getIntitule(), cours.getCredit());
                    result.getSessions().add(n.getSession());
                    // TODO I need to review something here
                    if (n.getMoyenne().isPresent()) {
                        result.getNotes().put(cours.getCours().getIntitule(), n.getMoyenne().get());
                        result.getAnnees().add(n.getAnnee());

                    }
                }

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public Map<String, MoyenneUniteEnseignement> listeNoteUniteEnseignement(String matricule, long niveauId, long optionId, long semestreId, long anneeId) throws ServiceException {
        try {
            Map<String, MoyenneUniteEnseignement> result = new HashMap<>();
            Niveau niveau = niveauDao.findById(niveauId);
            Option option = optionDao.findById(optionId);
            Semestre semestre = semestreDao.findById(semestreId);
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            List<UniteEnseignement> liste = uniteEnseignementDao.findByUniteNiveauOptionSemestre(niveau, option, semestre, annee);
            for (UniteEnseignement liste1 : liste) {
                MoyenneUniteEnseignement res = getMoyenneUEEtudiant(matricule, niveauId, liste1.getId(), anneeId, anneeId);
                result.put(liste1.getCode(), res);
            }
            return result;

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<String, MoyenneUniteEnseignement> listeNoteUniteEnseignement(String matricule, long niveauId, long anneeId, long aCourantId, List<UniteEnseignement> ues) throws ServiceException {
        Map<String, MoyenneUniteEnseignement> result = new HashMap<>();

        for (UniteEnseignement liste1 : ues) {
            MoyenneUniteEnseignement res = getMoyenneUEEtudiant(matricule, niveauId, liste1.getId(), anneeId, aCourantId);
            result.put(liste1.getCode(), res);
        }
        return result;
    }

    @Override
    public List<DeliberationItem> listeDeliberation(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean infInclusive, double borneSup, boolean supInclusive, double finale) throws ServiceException {

        try {
            Cours c = coursDao.findById(coursId);
            Niveau n = niveauDao.findById(niveauId);
            Option p = optionDao.findById(optionId);
            AnneeAcademique a = academiqueDao.findById(anneeId);
            Session s = Session.values()[session];
            return lesDeliberation(c, n, p, a, s, infInclusive, borneInf, supInclusive, borneSup, finale);

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(400);
        }

    }

    private List<DeliberationItem> lesDeliberation(Cours c, Niveau n, Option o, AnneeAcademique a, Session s, boolean infInclusive, double borneInf, boolean supInclusive, double borneSup, double finale) throws DataAccessException {
        List<DeliberationItem> result = new ArrayList<>();

        List<EtudiantNotes> toto = listeNoteEtudiant(c, a, n, o, s);
        for (EtudiantNotes toto1 : toto) {
            // I need to keep only those with values that belongs to the right interval
            // I need to review something here
            if (!toto1.getMoyenne().isPresent()) {
                continue;
            }
            double moyenne = toto1.getMoyenne().get();
            boolean test1 = infInclusive ? moyenne >= borneInf : moyenne > borneInf;
            boolean test2 = supInclusive ? moyenne <= borneSup : moyenne < borneSup;
            if (test1 && test2) {
                // I add the corresponding student to the list
                DeliberationItem item = new DeliberationItem();
                item.setMatricule(toto1.getMatricule());
                item.setNom(toto1.getNom());
                item.setMoyenneAvant(moyenne);
                // TODO Il fqut chercher un moyen pour recuperer le code de l'examen de la base de données
                double noteAvant = toto1.getNote().get("EE");
                double noteApres = ((finale - moyenne) * 100 / toto1.getDetails().get("EE")) + noteAvant;
                noteApres = Math.ceil(noteApres * 4) / 4.0;
                // TODO je dois surement eviter de faire ce genre de chose
                toto1.getNote().put("EE", noteApres);
                // TODO I need to review this code
                item.setMoyenneApres(toto1.getMoyenne().get());
                item.setNoteAvant(noteAvant);
                item.setNoteApres(noteApres);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public int delibererCours(long niveauId, long optionId, long coursId, long anneeId, int session, double borneInf, boolean infInclusive, double borneSup, boolean supInclusive, double finale) throws ServiceException {
        int res = 0;
        try {
            Cours c = coursDao.findById(coursId);
            Niveau n = niveauDao.findById(niveauId);
            Option p = optionDao.findById(optionId);
            AnneeAcademique a = academiqueDao.findById(anneeId);
            Session s = Session.values()[session];
            List<DeliberationItem> result = lesDeliberation(c, n, p, a, s, infInclusive, borneInf, supInclusive, borneSup, finale);
            Evaluation evaluation = evaluationDao.findExamen();
            for (DeliberationItem result1 : result) {
                Etudiant e = etudiantDao.findByMatricule(result1.getMatricule());
                Note note = noteDao.getNoteCours(e, evaluation, c, a, s);
                note.setValeur(result1.getNoteApres());
                noteDao.update(note);
                res++;

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return res;

    }

    @Override
    public List<Note> listeNoteEtudiant(String matricule, long coursId, long anneeId) throws ServiceException {
        try {
            Etudiant etudiant = etudiantDao.findByMatricule(matricule);
            Cours cours = coursDao.findById(coursId);
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            return noteDao.getNoteCours(etudiant, cours, annee);

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException(ex);
        }
    }

    @Override
    public Note getNoteEtudiantByEvaluation(Long etudiantId, Long evaluationId, Long coursId, Long anneeId, int session) throws ServiceException {
        try {
            Etudiant etudiant = etudiantDao.findById(etudiantId);
            if (etudiant == null) {
                throw new ServiceException("L'etudiant demandé est introuvable");
            }
            Evaluation evaluation = evaluationDao.findById(evaluationId);
            if (evaluation == null) {
                throw new ServiceException("L'evaluation demandée est introuvable");
            }
            //System.out.println("\n =====  L'id du cours est :" + coursId + "======\n");
            Cours cours = coursDao.findById(coursId);
            if (cours == null) {
                throw new ServiceException("Le cours demandé est introuvable");
            }
            AnneeAcademique academique = academiqueDao.findById(anneeId);
            if (academique == null) {
                throw new ServiceException("L'année demandée est introuvable");
            }

            Session s = Session.values()[session];
            return noteDao.getNoteCours(etudiant, evaluation, cours, academique, s);

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("Impossible d'effectuer le traitement");
        }
    }

    @Override
    public Double calculerMoyenneCycle(String matricule, long cycleId, long anneeId) throws ServiceException {
        try {

            Cycle cycle = cycleDao.findById(cycleId);
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            Etudiant etudiant = etudiantDao.findByMatricule(matricule);
            List<Niveau> niveaux = cycle.getNiveaux();
            double produitMgp = 0;
            int nombreCredit = 0;
            for (Niveau niveau : niveaux) {
                // I compute the MGP
                Option option;
                try {
                    option = optionDao.findByEtudiantNiveau(etudiant, niveau);
                } catch (NoResultException nre) {
                    continue;
                }
                AnneeAcademique an;
                if (niveau.isTerminal()) {
                    an = academiqueDao.findLastYearNote(etudiant, niveau, option);
                } else {
                    an = academiqueDao.findLastInscriptionYear(etudiant, niveau, option);
                }
                //System.out.println("Annee " + an.toString() + " etudiant " + etudiant.getNom() + " Niveau " + niveau.getCode());
                List<Semestre> semestres = semestreDao.findByNiveau(niveau);
                for (Semestre semestre : semestres) {

                    List<UniteEnseignement> uniteEns = uniteEnseignementDao.findByUniteNiveauOptionSemestre(niveau, option, semestre, an);
                    List<UEnseignementCredit> ues1 = uniteEnseignementDao.findByNiveauOptionSemestre(niveau, option, semestre, an);
                    Map<String, MoyenneUniteEnseignement> note = listeNoteUniteEnseignement(etudiant.getMatricule(), niveau.getId(), annee.getId(), an.getId(), uniteEns);
                    for (UEnseignementCredit ue : ues1) {
                        if (ue.getCredit() != 0) {
                            MoyenneUniteEnseignement mue = note.get(ue.getCodeUE());
                            Double value = mue.getMoyenne();
                            //System.out.println("UE " + ue.getIntituleUE() + " et le MGP " + value);
                            Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                            int credit = ue.getCredit();
                            if (value < 10) {
                                return null;
                            } else {
                                nombreCredit += credit;
                                produitMgp += noteMgp * credit;
                            }
                        }
                    }
                }
                //System.out.println("Le nombre de credit " + nombreCredit + " MGP " + Math.floor(produitMgp / nombreCredit * 100) / 100);
            }
            return Math.floor(produitMgp / nombreCredit * 100) / 100;

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public int calculerNombreCreditsValides(String matricule, long cycleId, long anneeId) throws ServiceException {
        // TODO I need to figure out why I did this stupidity
        return 120;
    }

    @Override
    public EtudiantNiveau calculerPerformanceNiveau(String matricule, long niveauId, long anneeId) throws ServiceException {
        EtudiantNiveau result = new EtudiantNiveau();
        try {
            Etudiant etudiant = etudiantDao.findByMatricule(matricule);
            Niveau niveau = niveauDao.findById(niveauId);
            double produitMgp = 0;
            int nombreCredit = 0;
            Option option;
            try {
                option = optionDao.findByEtudiantNiveau(etudiant, niveau);
            } catch (NoResultException nre) {
                return null;
            }
            AnneeAcademique an;
            if (niveau.isTerminal()) {
                an = academiqueDao.findLastYearNote(etudiant, niveau, option);
            } else {
                an = academiqueDao.findLastInscriptionYear(etudiant, niveau, option);
            }
            //List<Semestre> semestres = semestreDao.findByNiveau(niveau);
            //for (Semestre semestre : semestres) {
            List<UniteEnseignement> uniteEns = uniteEnseignementDao.findByUniteNiveauOptionSemestre(niveau, option, null, an);
            List<UEnseignementCredit> ues1 = uniteEnseignementDao.findByNiveauOptionSemestre(niveau, option, null, an);
            //System.out.println("*********** est null "+(an == null)+" et l'étudiant est "+etudiant.getNom());
            Map<String, MoyenneUniteEnseignement> note = listeNoteUniteEnseignement(etudiant.getMatricule(), niveauId, anneeId, (an == null) ? anneeId : an.getId(), uniteEns);
            for (UEnseignementCredit ue : ues1) {
                if (ue.getCredit() != 0) {
                    MoyenneUniteEnseignement mue = note.get(ue.getCodeUE());
                    Double value = mue.getMoyenne();
                    Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                    int credit = ue.getCredit();
                    if (value >= 10) {
                        nombreCredit += credit;
                    }
                    produitMgp += noteMgp * credit;

                }
            }
            result.setMgp(Math.floor(produitMgp / nombreCredit * 100) / 100);
            result.setNiveau(niveau.getCode());
            result.setMatricule(matricule);
            result.setCredit(nombreCredit);
            return result;

        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public EtudiantCycle calculerPerformanceCycle(String matricule, long cycleId, long anneeId) throws ServiceException {
        EtudiantCycle result = new EtudiantCycle();
        try {

            Cycle cycle = cycleDao.findById(cycleId);
            Etudiant etudiant = etudiantDao.findByMatricule(matricule);
            result.setMatricule(matricule);
            result.setNom(etudiant.getNom());
            List<Niveau> niveaux = cycle.getNiveaux();
            double produitMgp = 0;
            int nombreCredit = 0;
            boolean validCycle = true;
            for (Niveau niveau : niveaux) {
                int creditCapitalise = 0;
                int creditNiveau = 0;
                double produitMgpNiveau = 0;
                boolean validNiveau = true;
                Option option;
                try {
                    option = optionDao.findByEtudiantNiveau(etudiant, niveau);
                } catch (NoResultException nre) {
                    continue;
                }
                AnneeAcademique an;
                if (niveau.isTerminal()) {
                    an = academiqueDao.findLastYearNote(etudiant, niveau, option);
                } else {
                    an = academiqueDao.findLastInscriptionYear(etudiant, niveau, option);
                }
                List<Semestre> semestres = semestreDao.findByNiveau(niveau);
                for (Semestre semestre : semestres) {

                    List<UniteEnseignement> uniteEns = uniteEnseignementDao.findByUniteNiveauOptionSemestre(niveau, option, semestre, an);
                    List<UEnseignementCredit> ues1 = uniteEnseignementDao.findByNiveauOptionSemestre(niveau, option, semestre, an);
                    Map<String, MoyenneUniteEnseignement> note = listeNoteUniteEnseignement(etudiant.getMatricule(), niveau.getId(), anneeId, (an == null) ? anneeId : an.getId(), uniteEns);
                    for (UEnseignementCredit ue : ues1) {
                        if (ue.getCredit() != 0) {
                            MoyenneUniteEnseignement mue = note.get(ue.getCodeUE());
                            Double value = mue.getMoyenne();
                            Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                            int credit = ue.getCredit();
                            if (value < 10) {
                                validNiveau = false;
                                validCycle = false;
                            } else {
                                creditCapitalise += credit;
                                produitMgpNiveau += noteMgp * credit;
                            }
                            creditNiveau += credit;
                        }
                    }
                }
                nombreCredit += creditNiveau;
                produitMgp += produitMgpNiveau;
                result.addCreditNiveau(niveau.getCode(), creditCapitalise);
                if (validNiveau) {
                    result.addMGPNiveau(niveau.getCode(), Math.floor(produitMgpNiveau / creditNiveau * 100) / 100);
                }
            }
            if (validCycle) {
                result.setMgp(Math.floor(produitMgp / nombreCredit * 100) / 100);

            }
        } catch (DataAccessException ex) {
            Logger.getLogger(NoteServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
