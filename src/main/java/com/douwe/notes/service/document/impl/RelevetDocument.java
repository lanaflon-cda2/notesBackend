package com.douwe.notes.service.document.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.config.MessageHelper;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.dao.ICoursDao;
import com.douwe.notes.dao.ICreditDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.dao.IEnseignantDao;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.dao.IEvaluationDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.dao.IParcoursDao;
import com.douwe.notes.dao.IProgrammeDao;
import com.douwe.notes.dao.ISemestreDao;
import com.douwe.notes.dao.IUniteEnseignementDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.projection.RelevetEtudiantNotesInfos;
import com.douwe.notes.projection.UEnseignementCredit;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.IRelevetDocument;
import com.douwe.notes.service.impl.DocumentServiceImpl;
import com.douwe.notes.service.util.RomanNumber;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Stateless
public class RelevetDocument implements IRelevetDocument {

    @EJB
    private INoteService noteService;

    @Inject
    private INiveauDao niveauDao;

    @Inject
    private IEnseignantDao enseignantDao;

    @Inject
    private IEtudiantDao etudiantDao;

    @Inject
    private IOptionDao optionDao;

    @Inject
    private ICoursDao coursDao;

    @Inject
    private IAnneeAcademiqueDao academiqueDao;

    @Inject
    private IDepartementDao departementDao;

    @Inject
    private ICreditDao creditDao;

    // TODO Royken I faut eviter les dependances vers les services
    @Inject
    private IEvaluationDao EvaluationDao;

    @Inject
    private IProgrammeDao programmeDao;

    @Inject
    private IParcoursDao parcoursDao;

    @Inject
    private ISemestreDao semestreDao;

    @Inject
    private IUniteEnseignementDao uniteEnsDao;

    @Inject
    private DocumentCommon common;

    private final DateFormat dateFormatter;

    private final DateFormat anneeFormatter;

    MessageHelper msgHelper = new MessageHelper();

    public RelevetDocument() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        anneeFormatter = new SimpleDateFormat("yyyy");
    }

    public IEtudiantDao getEtudiantDao() {
        return etudiantDao;
    }

    public void setEtudiantDao(IEtudiantDao etudiantDao) {
        this.etudiantDao = etudiantDao;
    }

//    @Inject
//    private IUniteEnseignementDao uniteDao;
//    
//    @Inject
//    private ISemestreDao semestreDao;
    public INoteService getNoteService() {
        return noteService;
    }

    public void setNoteService(INoteService noteService) {
        this.noteService = noteService;
    }

    public INiveauDao getNiveauDao() {
        return niveauDao;
    }

    public void setNiveauDao(INiveauDao niveauDao) {
        this.niveauDao = niveauDao;
    }

    public IOptionDao getOptionDao() {
        return optionDao;
    }

    public void setOptionDao(IOptionDao optionDao) {
        this.optionDao = optionDao;
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

    public IDepartementDao getDepartementDao() {
        return departementDao;
    }

    public void setDepartementDao(IDepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public IEnseignantDao getEnseignantDao() {
        return enseignantDao;
    }

    public void setEnseignantDao(IEnseignantDao enseignantDao) {
        this.enseignantDao = enseignantDao;
    }

    public IProgrammeDao getProgrammeDao() {
        return programmeDao;
    }

    public void setProgrammeDao(IProgrammeDao programmeDao) {
        this.programmeDao = programmeDao;
    }

    public IParcoursDao getParcoursDao() {
        return parcoursDao;
    }

    public void setParcoursDao(IParcoursDao parcoursDao) {
        this.parcoursDao = parcoursDao;
    }

    public ISemestreDao getSemestreDao() {
        return semestreDao;
    }

    public void setSemestreDao(ISemestreDao semestreDao) {
        this.semestreDao = semestreDao;
    }

    public IEvaluationDao getEvaluationDao() {
        return EvaluationDao;
    }

    public void setEvaluationDao(IEvaluationDao EvaluationDao) {
        this.EvaluationDao = EvaluationDao;
    }

    public IUniteEnseignementDao getUniteEnsDao() {
        return uniteEnsDao;
    }

    public void setUniteEnsDao(IUniteEnseignementDao uniteEnsDao) {
        this.uniteEnsDao = uniteEnsDao;
    }

    public ICreditDao getCreditDao() {
        return creditDao;
    }

    public void setCreditDao(ICreditDao creditDao) {
        this.creditDao = creditDao;
    }

    public DocumentCommon getCommon() {
        return common;
    }

    public void setCommon(DocumentCommon common) {
        this.common = common;
    }

    @Override
    public void produireRelevet(Long niveauId, Long optionId, Long anneeId, OutputStream stream, Long etudiantId) {

        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, stream);
            writer.setPageEvent(new ReleveDecorator());
            doc.setPageSize(PageSize.A4);
            doc.setMargins(24, 24, 20, 20);
            //doc.setma
            doc.open();
            AnneeAcademique annee = academiqueDao.findById(anneeId);
            Option option = optionDao.findById(optionId);
            Niveau niveau = niveauDao.findById(niveauId);
            // Nouvel algorithm
            List<Etudiant> etudiants;
            if (niveau.isTerminal()) {
                etudiants = etudiantDao.listeEtudiantParDepartementEtNiveau(option.getDepartement(), annee, niveau, option);
            } else {
                etudiants = etudiantDao.listeEtudiantsAnnee(niveau, option, annee);
            }
            Map<String, RelevetEtudiantNotesInfos> infos = computeInfosOfAllStudents(etudiants, annee, niveau, option);
            // Il faut vérifier si l'id de l"tudiant est null
            if (etudiantId != null) {
                Etudiant etudiant = etudiantDao.findById(etudiantId);
                if (etudiant != null) {
                    RelevetEtudiantNotesInfos inf = infos.get(etudiant.getMatricule());
                    if (/*inf.isaToutValide() &&*/ (!niveau.isTerminal() || inf.getMgpCycle() != null)) {
                        produceRelevetEtudiant(doc, etudiant, niveau, option, annee, inf);
                        //doc.newPage();
                    }
                }
            } else {
                for (Etudiant etudiant : etudiants) {
                    RelevetEtudiantNotesInfos inf = infos.get(etudiant.getMatricule());
                    if (/*inf.isaToutValide() && */(!niveau.isTerminal() || inf.getMgpCycle() != null)) {
                        doc.newPage();
                        produceRelevetEtudiant(doc, etudiant, niveau, option, annee, inf);

                    }
                }
            }

            // Ancien algorithm
            /*
            //List<AnneeAcademique> annees = academiqueDao.findAllYearWthNote(a, n, o, s);

            //for (AnneeAcademique annee : annees) {
                //Liste des ues du semestre 1
                List<UniteEnseignement> uniteEns1 = uniteEnsDao.findByUniteNiveauOptionSemestre(n, o, semestres.get(0), a);

                List<UniteEnseignement> uniteEns2 = uniteEnsDao.findByUniteNiveauOptionSemestre(n, o, semestres.get(1), a);
                List<UEnseignementCredit> ues1 = uniteEnsDao.findByNiveauOptionSemestre(n, o, semestres.get(0), a);
                List<UEnseignementCredit> ues2 = uniteEnsDao.findByNiveauOptionSemestre(n, o, semestres.get(1), a);
                List<Etudiant> etudiants = etudiantDao.listeEtudiantParDepartementEtNiveau(o.getDepartement(), a, n, o);
                Map<String, RelevetEtudiantNotesInfos> infos = computeInfosOfAllStudents(etudiants, semestres, uniteEns1, uniteEns2, ues1, ues2, a);
                if (etudiantId != null) {
                    Etudiant etudiant = etudiantDao.findById(etudiantId);
                    RelevetEtudiantNotesInfos inf = infos.get(etudiant.getMatricule());
                    produceRelevetEtudiant(doc, etudiant, n, o, a, ues1, ues2, inf);

                } else {
                    for (Etudiant etudiant : etudiants) {
                        RelevetEtudiantNotesInfos inf = infos.get(etudiant.getMatricule());
                        produceRelevetEtudiant(doc, etudiant, n, o, a, ues1, ues2, inf);
                        doc.newPage();
                    }

                }
             */
            doc.close();
        } catch (DocumentException | DataAccessException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetEtudiant(Document doc, Etudiant e, Niveau n, Option o, AnneeAcademique a, RelevetEtudiantNotesInfos infos) {
        try {
            AnneeAcademique ac = infos.getAnneeAcademique();
            common.produceDocumentHeader(doc, null, n, o, a, null, null, null, true);
            produceRelevetTitle(doc, a.getNumeroDebut() + 1);
            // doc.add(new Chunk("\n"));
            produireRelevetHeader(e, n, o, doc);
            List<UEnseignementCredit> ues = new ArrayList<>();
            List<Semestre> semestres = semestreDao.findByNiveau(n);
            for (Semestre semestre : semestres) {
                ues.addAll(uniteEnsDao.findByNiveauOptionSemestre(n, o, semestre, ac));
            }
            produceRelevetTable(doc, ues, infos, n.isTerminal());
            //doc.add(new Chunk("\n"));
            produceRelevetFooter(doc);
        } catch (Exception ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetTitle(Document doc, int annee) {
        try {
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            doc.add(new Phrase("\n", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL)));
            StringBuilder str = new StringBuilder();

            str.append(msgHelper.getProperty("releve.releveTitle.titre"));
            str.append(msgHelper.getProperty("releve.releveTitle.no"));
            if (annee <= 2016) {
                str.append(msgHelper.getProperty("releve.releveTitle.daacr"));
            } else {
                str.append(msgHelper.getProperty("releve.releveTitle.daacr2"));
            }
            str.append(annee);
            Phrase phrase = new Phrase(str.toString(), font);
            doc.add(phrase);
        } catch (DocumentException ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produireRelevetHeader(Etudiant e, Niveau n, Option o, Document doc) {

        try {

            Font french = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font english = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLDITALIC);
            Font french2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
            Font english2 = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.ITALIC);

            float relativeWidths[];
            relativeWidths = new float[3];
            relativeWidths[0] = 3;
            relativeWidths[1] = 6;
            relativeWidths[2] = 3;
            PdfPTable table = new PdfPTable(relativeWidths);
            table.setWidthPercentage(100);

            Phrase cyclef = new Phrase(msgHelper.getProperty("releve.releveHeader.cycle"), french2);
            String phrase1 = o.getDepartement().getFrenchDescription().replace("Département d'", "").replace("Département des", "").replace("Département de", "");
            String phrase2 = o.getDepartement().getEnglishDescription().replace("Department of ", "");
            Phrase valuecyclef = new Phrase(n.getCycle().getDiplomeFr() + " en " + phrase1 + "\n", french);
            Phrase cyclee = new Phrase(msgHelper.getProperty("releve.releveHeader.cycleEng"), english2);
            Phrase valuecyclee = new Phrase(n.getCycle().getDiplomeEn() + " in " + phrase2 + "\n", english);
            Phrase cycle = new Phrase();
            cycle.add(cyclef);
            cycle.add(valuecyclef);
            cycle.add(cyclee);
            cycle.add(valuecyclee);
            PdfPCell cell1 = new PdfPCell(cycle);
            cell1.setColspan(2);
            cell1.setBorderColor(BaseColor.WHITE);
            table.addCell(cell1);

            Phrase niveauf = new Phrase(msgHelper.getProperty("releve.releveHeader.niveau"), french2);
            Phrase valueniveauf = new Phrase(n.getCode() + "\n", french);
            Phrase niveaue = new Phrase(msgHelper.getProperty("releve.releveHeader.level"), english2);
            Phrase niveau = new Phrase();
            niveau.add(niveauf);
            niveau.add(valueniveauf);
            niveau.add(niveaue);
            PdfPCell cell2 = new PdfPCell(niveau);
            cell2.setBorderColor(BaseColor.WHITE);
            table.addCell(cell2);

            Phrase spf = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.option"), french2));
            Phrase valuespf = new Phrase(new Chunk(o.getDescription() + "\n", french));
            Phrase spe = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.option"), english2));
            Phrase valuespe = new Phrase(new Chunk(o.getDescriptionEnglish(), english));

            /*         cell3.addElement(new Chunk("Spécialité : ", french2));
             cell3.addElement(new Chunk("Informatique et Télécommunications" + "\n", french));
             cell3.addElement(new Chunk("Speciality", english2));
             cell3.addElement(new Chunk("Computer Science and Telecommunications", english));
             */
            Phrase specialite = new Phrase();
            specialite.add(spf);
            specialite.add(valuespf);
            specialite.add(spe);
            specialite.add(valuespe);
            PdfPCell cell3 = new PdfPCell(specialite);
            cell3.setColspan(3);
            cell3.setBorderColor(BaseColor.WHITE);
            table.addCell(cell3);

            Phrase nomf = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.nom"), french2));
            Phrase valuenomf = new Phrase(new Chunk(e.getNom() + "\n", french));
            Phrase nome = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.name"), english2));
            Phrase nom = new Phrase();
            nom.add(nomf);
            nom.add(valuenomf);
            nom.add(nome);
            PdfPCell cell4 = new PdfPCell(nom);
            cell4.setBorderColor(BaseColor.WHITE);
            cell4.setColspan(2);
            table.addCell(cell4);

            Phrase matf = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.matricule"), french2));
            Phrase valuematf = new Phrase(new Chunk(e.getMatricule() + "\n", french));
            Phrase mate = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.registration"), english2));
            Phrase matricule = new Phrase();
            matricule.add(matf);
            matricule.add(valuematf);
            matricule.add(mate);
            PdfPCell cell5 = new PdfPCell(matricule);
            cell5.setBorderColor(BaseColor.WHITE);
            table.addCell(cell5);

            Phrase datef = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.neLe"), french2));
            Phrase valuedatef;
            if (e.getDateDeNaissance() != null) {
                if (e.isValidDate()) {
                    valuedatef = new Phrase(new Chunk(dateFormatter.format(e.getDateDeNaissance()) + "\n", french));
                } else {
                    valuedatef = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.vers") + anneeFormatter.format(e.getDateDeNaissance()) + "\n", french));
                }

            } else {
                valuedatef = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.unknownBirth") + "\n", french));
            }
            Phrase datee = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.birthDate"), english2));
            Phrase date = new Phrase();
            date.add(datef);
            date.add(valuedatef);
            date.add(datee);
            PdfPCell cell6 = new PdfPCell(date);
            cell6.setBorderColor(BaseColor.WHITE);
            table.addCell(cell6);

            Phrase lieuf = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.a"), french2));
            Phrase valuelieuf;
            if (e.getLieuDeNaissance() != null) {
                valuelieuf = new Phrase(new Chunk(e.getLieuDeNaissance().toUpperCase() + "\n", french));
            } else {
                valuelieuf = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.unknownPlace") + "\n", french));
            }
            Phrase lieue = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.at"), english2));
            Phrase lieu = new Phrase();
            lieu.add(lieuf);
            lieu.add(valuelieuf);
            lieu.add(lieue);
            PdfPCell cell7 = new PdfPCell(lieu);
            cell7.setBorderColor(BaseColor.WHITE);
            table.addCell(cell7);

            Phrase sexef = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.sexe"), french2));
            String ssexe = e.getGenre().toString();
            ssexe = ssexe.substring(0, 1).toUpperCase() + ssexe.substring(1);
            Phrase valuesexef = new Phrase(new Chunk(ssexe + "\n", french));
            Phrase sexee = new Phrase(new Chunk(msgHelper.getProperty("releve.releveHeader.sex"), english2));
            Phrase sexe = new Phrase();
            sexe.add(sexef);
            sexe.add(valuesexef);
            sexe.add(sexee);
            PdfPCell cell8 = new PdfPCell(sexe);
            cell8.setBorderColor(BaseColor.WHITE);
            table.addCell(cell8);

            doc.add(table);

            /*
             PdfPCell cell = new PdfPCell();
             cell.addElement(new Phrase("Created Date : ", grayFont));
             cell.addElement(new Phrase(theDate, blackFont));
             */
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetTable(Document doc, List<UEnseignementCredit> ues, RelevetEtudiantNotesInfos infos, boolean estTerminale) {
        try {

            Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font bf1 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            Font bf2 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);

            //int nombreCredit = 0;
            //int nombreCreditValide = 0;
            boolean aToutValide = true;

            float relativeWidths[] = {2.5F, 10, 2, 3, 3, 2, 3.5F, 2};
            PdfPTable table = new PdfPTable(relativeWidths);
            table.setSpacingBefore(5f);
            table.setWidthPercentage(100);
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.codeUE"), msgHelper.getProperty("releve.releveTable.creditCode"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.intituleUe"), msgHelper.getProperty("releve.releveTable.courseTitle"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.credit"), msgHelper.getProperty("releve.releveTable.creditEng"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.moyenne20"), msgHelper.getProperty("releve.releveTable.average20"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.moyenneGrade"), msgHelper.getProperty("releve.releveTable.gpa"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.grade"), msgHelper.getProperty("releve.releveTable.gradeEng"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.semestre"), msgHelper.getProperty("releve.releveTable.semester"), bf, bf2, true, true));
            table.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.session"), msgHelper.getProperty("releve.releveTable.sessionEng"), bf, bf2, true, true));
            /*
            table.addCell(DocumentUtil.createDefaultBodyCell("Credit Code", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Course Title", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Credit", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Average / 20", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("GPA", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Grade", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Semester", bf2, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Session", bf2, false));
             */
            Map<String, Double> notes = infos.getNotes();
            Map<String, Session> sessions = infos.getSessions();
            Map<String, Double> mgp = infos.getMgp();
            Map<String, String> semtrs = infos.getSemestres();
            int nombreCours = ues.size();
            float padding = 6f * 16 / nombreCours;
            if(nombreCours > 20)
                padding = 5f * 16 / nombreCours;

            if (estTerminale) {
                // padding = 5.8f * 16 / nombreCours;
                padding = 4.0f * 16 / nombreCours;
            }
            for (UEnseignementCredit ue : ues) {
                if (ue.getCredit() != 0) {
                    Double value = notes.get(ue.getCodeUE());
                    if ((value == null) ||(value < 10)) {
                        aToutValide = false;
                    }
                    //nombreCredit += ue.getCredit();
                    Session session = sessions.get(ue.getCodeUE());
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, false, false, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, false, false, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(value < 10 ? "0" : String.valueOf(ue.getCredit()), bf1, false, true, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, false, true, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.valueOf(mgp.get(ue.getCodeUE())), bf1, false, true, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.transformNoteGradeUE(Optional.of(value)), bf1, false, true, padding, padding));

                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(semtrs.get(ue.getCodeUE()), bf1, false, true, padding, padding));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.sessionToString(session), bf1, false, true, padding, padding));
                }

            }
            /*for (UEnseignementCredit ue : ues2) {
                if (ue.getCredit() != 0) {
                    Double value = notes.get(ue.getCodeUE());
                    if (value < 10) {
                        aToutValide = false;
                    }
                    //nombreCredit += ue.getCredit();
                    Session session = sessions.get(ue.getCodeUE());
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, false, true));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, false, false));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(value < 10 ? "0" : String.valueOf(ue.getCredit()), bf1, false, true));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, false, true));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.valueOf(mgp.get(ue.getCodeUE())), bf1, false, true));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.transformNoteGradeUE(value), bf1, false, true));

                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(semtrs.get(ue.getCodeUE()), bf1, false, true));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.sessionToString(session), bf1, false, true));
                }
            }*/
            PdfPCell cell = new PdfPCell();
//                 float relativeWidths2[];
//            relativeWidths2 = new float[8];
//            relativeWidths2[0] = 3;
//            relativeWidths[1] = 10;
//            relativeWidths[2] = 3;
//            for (int i = 0; i < 8; i++) {
//                relativeWidths2[3 + i] = 3;
//            }
            //float widths[] =;
            PdfPTable table2 = new PdfPTable(new float[]{1, 3.2f, 2, 3, 2, 8});
            table2.setSpacingBefore(5f);
            table2.setSpacingAfter(5f);
            table2.setWidthPercentage(96);
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.rang"), msgHelper.getProperty("releve.releveTable.rank"), bf, bf2, false, true));
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.creditCapitalise"), msgHelper.getProperty("releve.releveTable.totalCredit"), bf, bf2, false, true));
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.moyenneGrade"), msgHelper.getProperty("releve.releveTable.gpa"), bf, bf2, false, true));
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.gradeGeneral"), msgHelper.getProperty("releve.releveTable.generalGrade"), bf, bf2, false, true));
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.decision"), msgHelper.getProperty("releve.releveTable.decisonEng"), bf, bf2, false, true));
            table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.mention"), "", bf, bf2, false, true));
            /*table2.addCell(createRelevetFootBodyCell("Crédits Capitalisés", bf, false, 1, 1));
            //table2.addCell(createRelevetFootBodyCell("Moy /20", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Moy / Grad", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Grade général", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Décision", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Mention", bf, false, 1, 2));

            table2.addCell(createRelevetFootBodyCell("Rank", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Total Credit", bf1, false, 1, 1));
            //table2.addCell(createRelevetFootBodyCell("Average /20", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("GPA", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("General Grade", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Decision", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("", bf1, false, 1, 2));*/
 /*Les valeurs   */

            // Sais pas comment obtenir le rang
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.valueOf(infos.getRang()) : "    ", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(String.valueOf(infos.getNombreCreditValides()), bf, false, 1, 1));
            //table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.format("%.2f", infos.getMoyenne()) : "", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.format("%.2f", infos.getMoyenneMgp()) : "    ", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? DocumentUtil.transformMoyenneMgpToGradeRelevet(infos.getMoyenneMgp()) : "        ", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell((infos.getMoyenne() >= 10) ? "AD" : "RD", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? DocumentUtil.transformMoyenneMgpToMentionRelevet(infos.getMoyenneMgp()) : "               ", bf, false, 2, 2));
            cell.setBorderWidth(0.01f);
            cell.setBorderColor(BaseColor.BLACK);
            /*Chunk toto = new Chunk("Très Bien / Second Class Hons. Upper Division", bf);
            toto.setGenericTag("shadow");
            PdfPCell ccc = new PdfPCell(new Phrase(toto));
            ccc.setBorder(0);
            ccc.setBorderColor(BaseColor.WHITE);
            ccc.setHorizontalAlignment(Element.ALIGN_CENTER);
            ccc.setPaddingTop(3f);
            ccc.setPaddingBottom(3f);
            ccc.setTop(2f);
            table2.addCell(ccc);*/
            cell.addElement(table2);
            cell.setColspan(8);
            cell.setRowspan(6);
            if (estTerminale) {
                cell.setBorderWidthBottom(0);
                cell.setBorderColorBottom(BaseColor.WHITE);
            }
            table.addCell(cell);
            if (estTerminale) {
                table2 = new PdfPTable(new float[]{3, 3, 6});
                table2.setSpacingAfter(5f);
                table2.setWidthPercentage(92);
                table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.moyenneGradeCycle"), msgHelper.getProperty("releve.releveTable.moyenneGradeCycleEng"), bf, bf2, false, true));
                table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.gradeGeneralCycle"), "", bf, bf2, false, true));
                table2.addCell(CreerTitre(msgHelper.getProperty("releve.releveTable.mention"), "", bf, bf2, false, true));
                Double res = infos.getMgpCycle();
                table2.addCell(DocumentUtil.createRelevetFootBodyCell(String.format("%.2f", res), bf, false, 1, 1));
                table2.addCell(DocumentUtil.createRelevetFootBodyCell((res == null) ? "" : DocumentUtil.transformMoyenneMgpToGradeRelevet(infos.getMgpCycle()), bf, false, 1, 1));
                table2.addCell(DocumentUtil.createRelevetFootBodyCell((res == null) ? "" : DocumentUtil.transformMoyenneMgpToMentionRelevet(infos.getMgpCycle()), bf, false, 1, 1));
                cell = new PdfPCell();
                cell.setColspan(8);
                cell.setBorderWidth(0.01f);
                cell.setBorderColor(BaseColor.BLACK);
                cell.setBorderColorTop(BaseColor.WHITE);
                cell.setBorderWidthTop(0);
                //cell.setBorderColor(BaseColor.WHITE);
                cell.addElement(table2);
                table.addCell(cell);
            }
            doc.add(table);
            //doc.add(table2);

        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PdfPCell CreerTitre(String francais, String anglais, Font bf, Font bf2, boolean avecBordure, boolean estCentre) {
        Phrase ph = new Phrase();
        ph.add(new Chunk(francais, bf));
        ph.add(new Chunk("\n"));
        ph.add(new Chunk(anglais, bf2));
        PdfPCell cl = new PdfPCell(ph);
        cl.setHorizontalAlignment(estCentre ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
        cl.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cl.setPaddingBottom(4f);
        cl.setPaddingTop(5f);
        if (avecBordure) {
            cl.setBorderWidth(0.01f);
            cl.setBorderColor(BaseColor.BLACK);
        } else {
            cl.setBorderWidth(0.00f);
            cl.setBorderColor(BaseColor.WHITE);
        }
        return cl;
    }

    private void produceRelevetFooter(Document doc) {
        try {
            // Font frenchFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.ITALIC);
            Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font font4 = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.ITALIC);

            float relativewidhts[] = new float[2];
            relativewidhts[0] = 6;
            relativewidhts[1] = 4;
            PdfPTable date = new PdfPTable(relativewidhts);
            date.setSpacingBefore(5f);
            date.setWidthPercentage(100);

            Phrase hh = new Phrase();
            hh.add(new Phrase(msgHelper.getProperty("releve.releveFooter.foi"), font3));
            hh.add("\n");
            hh.add(new Phrase(msgHelper.getProperty("releve.releveFooter.faith"), font4));
            PdfPCell cell1 = new PdfPCell(hh);

            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell1.setVerticalAlignment(Element.ALIGN_LEFT);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setPaddingBottom(5f);
            date.addCell(cell1);
            date.addCell(removeBorder(new PdfPCell()));
            date.addCell(removeBorder(new PdfPCell()));

            PdfPCell cell2 = new PdfPCell();
            
            cell2.addElement(new Phrase(msgHelper.getProperty("releve.releveFooter.date"), font2));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setBorderColor(BaseColor.WHITE);
            date.addCell(cell2);
            doc.add(date);
            //doc.add(new Phrase("\n"));

            PdfPTable cachet = new PdfPTable(new float[]{3, 4, 6});
            cachet.setSpacingBefore(4f);
            cachet.setWidthPercentage(90);
            /*PdfPCell cell3 = new PdfPCell();
            cell3.addElement(new Phrase(, font1));
            cell3.addElement(new Phrase(, font2));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderColor(BaseColor.WHITE);*/
            cachet.addCell(CreerTitre(msgHelper.getProperty("releve.releveFooter.chefDepartement"), msgHelper.getProperty("releve.releveFooter.headDepartement"), font1, font2, false, true));
            /*PdfPCell cell4 = new PdfPCell();
            cell4.addElement(new Phrase(, font1));
            cell4.addElement(new Phrase(, font2));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderColor(BaseColor.WHITE);*/
            cachet.addCell(removeBorder(new PdfPCell()));
            cachet.addCell(CreerTitre(msgHelper.getProperty("releve.releveFooter.directeur"), msgHelper.getProperty("releve.releveFooter.director"), font1, font2, false, true));
            doc.add(cachet);

        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*private PdfPCell createRelevetFootBodyCell(String message, Font bf, boolean border, int rowspan, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(message, bf));
        if (border) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        }
        cell.setRowspan(rowspan);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(4f);
        cell.setPaddingTop(5f);
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }*/
    private PdfPCell removeBorder(PdfPCell pdfPCell) {
        pdfPCell.setBorderWidth(0);
        pdfPCell.setBorderColor(BaseColor.WHITE);
        return pdfPCell;
    }

    private RelevetEtudiantNotesInfos computeInfosStudent(Etudiant etudiant, AnneeAcademique annee, Niveau niveau, Option option) {

        RelevetEtudiantNotesInfos infos = new RelevetEtudiantNotesInfos();
        try {
            AnneeAcademique aa;
            if (niveau.isTerminal()) {
                aa = academiqueDao.findLastYearNote(etudiant, niveau, option);
            } else {
                aa = academiqueDao.findLastInscriptionYear(etudiant, niveau, option);
            }

            System.out.println("Etudiant " + etudiant.getNom() + " first is null " + (aa == null) + " second is null " + (annee == null) + " le niveau " + niveau.getCode());
            if (aa == null) {
                return infos;
            }
            if (aa.getNumeroDebut() > annee.getNumeroDebut()) {
                aa = annee;
            }
            infos.setAnneeAcademique(aa);
            double produit = 0;
            double produitMgp = 0;
            int nombreCredit = 0;
            int nombreCreditValide = 0;

            // Je recupère la liste des unités d'enseignement avec les crédits correspondants
            // TODO je vais éviter d'aller vers la base de données tout le temps en implémentant un mécanisme de cache
            //List<UniteEnseignement> unites = new ArrayList<>();
            //List<UEnseignementCredit> credits = new ArrayList<>();
            Map<String, Double> notes = new HashMap<>();
            Map<String, Double> mgp = new HashMap<>();
            Map<String, Session> sessions = new HashMap<>();
            Map<String, String> semes = new HashMap<>();
            List<Semestre> semestres = semestreDao.findByNiveau(niveau);
            for (Semestre semestre : semestres) {
                List<UniteEnseignement> uniteEns = uniteEnsDao.findByUniteNiveauOptionSemestre(niveau, option, semestre, aa);
                List<UEnseignementCredit> ues1 = uniteEnsDao.findByNiveauOptionSemestre(niveau, option, semestre, aa);
                Map<String, MoyenneUniteEnseignement> note = noteService.listeNoteUniteEnseignement(etudiant.getMatricule(), niveau.getId(), annee.getId(), aa.getId(), uniteEns);
                //unites.addAll(uniteEns);
                //credits.addAll(ues1);
                for (UEnseignementCredit ue : ues1) {
                    MoyenneUniteEnseignement mue = note.get(ue.getCodeUE());
                    Double value = mue.getMoyenne().orElseGet(() -> null);
                    if (value != null) {
                        Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                        Session session = mue.getSession();
                        mgp.put(ue.getCodeUE(), noteMgp);
                        String sem = "";
                        if (mue.getAnneeAcademique() != null) {
                            sem = semestreToRoman(semestre) + "(" + mue.getAnneeAcademique().toString() + ")";
                        }
                        semes.put(ue.getCodeUE(), sem);
                        notes.put(ue.getCodeUE(), value);
                        sessions.put(ue.getCodeUE(), session);
                        produit += value * ue.getCredit();
                        produitMgp += noteMgp * ue.getCredit();
                        nombreCredit += ue.getCredit();
                        if (value >= 10) {
                            nombreCreditValide += ue.getCredit();
                        }
                    }
                }
            }
            infos.setaToutValide(nombreCreditValide == nombreCredit);
            infos.setNotes(notes);
            infos.setMgp(mgp);
            infos.setSessions(sessions);
            infos.setSemestres(semes);
            infos.setNombreCreditValides(nombreCreditValide);
            infos.setMoyenneMgp((Math.floor(((produitMgp * 1.0) / nombreCredit) * 100.0) / 100.0));
            infos.setMoyenne((Math.round(((produit * 1.0) / nombreCredit) * 100.0) / 100.0));
            if (niveau.isTerminal()) {
                infos.setMgpCycle(noteService.calculerMoyenneCycle(etudiant.getMatricule(), niveau.getCycle().getId(), annee.getId()));
            }
        } catch (DataAccessException | ServiceException ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*  try {
        Map<String, MoyenneUniteEnseignement> notes1 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns1);
        Map<String, MoyenneUniteEnseignement> notes2 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns2);
        for (UEnseignementCredit ue : ues1) {
        MoyenneUniteEnseignement mue = notes1.get(ue.getCodeUE());
        // System.out.println(mue);
        Double value = mue.getMoyenne();
        Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
        Session session = mue.getSession();
        mgp.put(ue.getCodeUE(), noteMgp);
        Semestre semestre = semestres.get(0);
        String sem = semestreToRoman(semestre) + "(" + a.toString() + ")";
        semes.put(ue.getCodeUE(), sem);
        notes.put(ue.getCodeUE(), value);
        sessions.put(ue.getCodeUE(), session);
        produit += value * ue.getCredit();
        produitMgp += noteMgp * ue.getCredit();
        nombreCredit += ue.getCredit();
        if (value >= 10) {
        nombreCreditValide += ue.getCredit();
        }
        }
        for (UEnseignementCredit ue : ues2) {
        //MoyenneTrashData mue = notes.get(ue.getCodeUE());
        MoyenneUniteEnseignement mue = notes2.get(ue.getCodeUE());
        Double value = mue.getMoyenne();
        Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
        Session session = mue.getSession();
        nombreCredit += ue.getCredit();
        notes.put(ue.getCodeUE(), value);
        mgp.put(ue.getCodeUE(), noteMgp);
        Semestre semestre = semestres.get(1);
        String sem = semestreToRoman(semestre) + "(" + a + ")";
        semes.put(ue.getCodeUE(), sem);
        sessions.put(ue.getCodeUE(), session);
        produit += value * ue.getCredit();
        produitMgp += noteMgp * ue.getCredit();
        if (value >= 10) {
        nombreCreditValide += ue.getCredit();
        }
        }
        infos.setNotes(notes);
        infos.setMgp(mgp);
        infos.setSessions(sessions);
        infos.setSemestres(semes);
        infos.setNombreCreditValides(nombreCreditValide);
        infos.setMoyenneMgp((Math.round(((produitMgp * 1.0) / nombreCredit) * 100.0) / 100.0));
        infos.setMoyenne((Math.round(((produit * 1.0) / nombreCredit) * 100.0) / 100.0));
        } catch (ServiceException ex) {
        Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }*/
 /*  try {

            Map<String, MoyenneUniteEnseignement> notes1 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns1);
            Map<String, MoyenneUniteEnseignement> notes2 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns2);
            
            for (UEnseignementCredit ue : ues1) {
                MoyenneUniteEnseignement mue = notes1.get(ue.getCodeUE());
                // System.out.println(mue);
                Double value = mue.getMoyenne();
                Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                Session session = mue.getSession();
                mgp.put(ue.getCodeUE(), noteMgp);
                Semestre semestre = semestres.get(0);
                String sem = semestreToRoman(semestre) + "(" + a.toString() + ")";
                semes.put(ue.getCodeUE(), sem);
                notes.put(ue.getCodeUE(), value);
                sessions.put(ue.getCodeUE(), session);
                produit += value * ue.getCredit();
                produitMgp += noteMgp * ue.getCredit();
                nombreCredit += ue.getCredit();
                if (value >= 10) {
                    nombreCreditValide += ue.getCredit();
                }
            }

            for (UEnseignementCredit ue : ues2) {
                //MoyenneTrashData mue = notes.get(ue.getCodeUE());
                MoyenneUniteEnseignement mue = notes2.get(ue.getCodeUE());
                Double value = mue.getMoyenne();
                Double noteMgp = DocumentUtil.transformNoteMgpUE(value);
                Session session = mue.getSession();
                nombreCredit += ue.getCredit();
                notes.put(ue.getCodeUE(), value);
                mgp.put(ue.getCodeUE(), noteMgp);
                Semestre semestre = semestres.get(1);
                String sem = semestreToRoman(semestre) + "(" + a + ")";
                semes.put(ue.getCodeUE(), sem);

                sessions.put(ue.getCodeUE(), session);
                produit += value * ue.getCredit();
                produitMgp += noteMgp * ue.getCredit();
                if (value >= 10) {
                    nombreCreditValide += ue.getCredit();
                }
            }
            infos.setNotes(notes);
            infos.setMgp(mgp);
            infos.setSessions(sessions);
            infos.setSemestres(semes);
            infos.setNombreCreditValides(nombreCreditValide);
            infos.setMoyenneMgp((Math.round(((produitMgp * 1.0) / nombreCredit) * 100.0) / 100.0));
            infos.setMoyenne((Math.round(((produit * 1.0) / nombreCredit) * 100.0) / 100.0));

        } catch (ServiceException ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        return infos;
    }

    private Map<String, RelevetEtudiantNotesInfos> computeInfosOfAllStudents(List<Etudiant> etudiants, AnneeAcademique annee, Niveau niveau, Option option) throws DataAccessException {

        Map<String, RelevetEtudiantNotesInfos> resultTmp = new HashMap<>();
        for (Etudiant e : etudiants) {
            resultTmp.put(e.getMatricule(), computeInfosStudent(e, annee, niveau, option));
        }

        resultTmp = sortByComparator(resultTmp);
        int rank = 1;
        Map<String, RelevetEtudiantNotesInfos> result = new HashMap<>();
        for (String key : resultTmp.keySet()) {
            RelevetEtudiantNotesInfos tmp = resultTmp.get(key);
            if (tmp.isaToutValide()) {
                tmp.setRang(rank);
                ++rank;
            }
            result.put(key, tmp);

        }
        return result;
    }

    private static Map<String, RelevetEtudiantNotesInfos> sortByComparator(Map<String, RelevetEtudiantNotesInfos> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, RelevetEtudiantNotesInfos>> list
                = new LinkedList<>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, RelevetEtudiantNotesInfos>>() {
            @Override
            public int compare(Map.Entry<String, RelevetEtudiantNotesInfos> o1,
                    Map.Entry<String, RelevetEtudiantNotesInfos> o2) {
                //return (o1.getValue()).compareTo(o2.getValue());
                return Double.compare(o2.getValue().getMoyenneMgp(), o1.getValue().getMoyenneMgp());
            }
        });

        // Convert sorted map back to a Map
        Map<String, RelevetEtudiantNotesInfos> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, RelevetEtudiantNotesInfos> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private String semestreToRoman(Semestre semestre) {
        String intitule = semestre.getIntitule();
        String root = intitule.substring(8, intitule.length()).trim();
        return RomanNumber.toRoman(Integer.parseInt(root));
    }

}
