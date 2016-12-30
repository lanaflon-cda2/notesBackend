package com.douwe.notes.service.document.impl;

import com.douwe.generic.dao.DataAccessException;
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
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.springframework.core.io.ClassPathResource;

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
            //PdfWriter.getInstance(doc, stream);

            writer.setPageEvent(new Watermark());
            doc.setPageSize(PageSize.A4);
            doc.setMargins(24, 24, 24, 24);
            doc.open();
            Niveau n = niveauDao.findById(niveauId);
            Option o = optionDao.findById(optionId);
            AnneeAcademique a = academiqueDao.findById(anneeId);
            List<Semestre> semestres = semestreDao.findByNiveau(n);

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

            doc.close();
        } catch (DataAccessException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetEtudiant(Document doc, Etudiant e, Niveau n, Option o, AnneeAcademique a, List<UEnseignementCredit> ues1, List<UEnseignementCredit> ues2, RelevetEtudiantNotesInfos infos) {
        try {
            common.produceDocumentHeader(doc, null, n, o, a, null, null, null, true);
            produceRelevetTitle(doc, a.getNumeroDebut().toString());
            // doc.add(new Chunk("\n"));
            produireRelevetHeader(e, n, o, doc);
            produceRelevetTable(doc, ues1, ues2, infos);
            //doc.add(new Chunk("\n"));
            produceRelevetFooter(doc);
        } catch (Exception ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetTitle(Document doc, String annee) {
        try {
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            doc.add(new Phrase("\n"));
            StringBuilder str = new StringBuilder();

            str.append("RELEVE DE NOTES / ACADEMIC TRANSCRIPT             ");
            str.append("N°");
            str.append("________/DISS/DAACR/");
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

            Phrase cyclef = new Phrase("Cycle: ", french2);
            String phrase1 = o.getDepartement().getFrenchDescription().replace("Département d'", "").replace("Département des", "").replace("Département de", "");
            String phrase2 = o.getDepartement().getEnglishDescription().replace("Department of ", "");
            Phrase valuecyclef = new Phrase(n.getCycle().getDiplomeFr() + " en " + phrase1 + "\n", french);
            Phrase cyclee = new Phrase("cycle: ", english2);
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

            Phrase niveauf = new Phrase("Niveau: ", french2);
            Phrase valueniveauf = new Phrase(n.getCode() + "\n", french);
            Phrase niveaue = new Phrase("Level: ", english2);
            Phrase niveau = new Phrase();
            niveau.add(niveauf);
            niveau.add(valueniveauf);
            niveau.add(niveaue);
            PdfPCell cell2 = new PdfPCell(niveau);
            cell2.setBorderColor(BaseColor.WHITE);
            table.addCell(cell2);

            Phrase spf = new Phrase(new Chunk("Option: ", french2));
            Phrase valuespf = new Phrase(new Chunk(o.getDescription() +"\n", french));
            Phrase spe = new Phrase(new Chunk("Option: ", english2));
            Phrase valuespe = new Phrase(new Chunk(o.getDepartement().getEnglishDescription(), english));

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

            Phrase nomf = new Phrase(new Chunk("Nom(s) et Prénom(s): ", french2));
            Phrase valuenomf = new Phrase(new Chunk(e.getNom() + "\n", french));
            Phrase nome = new Phrase(new Chunk("Name and surname: ", english2));
            Phrase nom = new Phrase();
            nom.add(nomf);
            nom.add(valuenomf);
            nom.add(nome);
            PdfPCell cell4 = new PdfPCell(nom);
            cell4.setBorderColor(BaseColor.WHITE);
            cell4.setColspan(2);
            table.addCell(cell4);

            Phrase matf = new Phrase(new Chunk("Matricule: ", french2));
            Phrase valuematf = new Phrase(new Chunk(e.getMatricule() + "\n", french));
            Phrase mate = new Phrase(new Chunk("Registration number: ", english2));
            Phrase matricule = new Phrase();
            matricule.add(matf);
            matricule.add(valuematf);
            matricule.add(mate);
            PdfPCell cell5 = new PdfPCell(matricule);
            cell5.setBorderColor(BaseColor.WHITE);
            table.addCell(cell5);

            Phrase datef = new Phrase(new Chunk("Né(e) le: ", french2));
            Phrase valuedatef;
            if (e.getDateDeNaissance() != null) {
                if (e.isValidDate()) {
                    valuedatef = new Phrase(new Chunk(dateFormatter.format(e.getDateDeNaissance()) + "\n", french));
                } else {
                    valuedatef = new Phrase(new Chunk("Vers " + anneeFormatter.format(e.getDateDeNaissance()) + "\n", french));
                }

            } else {
                valuedatef = new Phrase(new Chunk("Unknown" + "\n", french));
            }
            Phrase datee = new Phrase(new Chunk("Born on: ", english2));
            Phrase date = new Phrase();
            date.add(datef);
            date.add(valuedatef);
            date.add(datee);
            PdfPCell cell6 = new PdfPCell(date);
            cell6.setBorderColor(BaseColor.WHITE);
            table.addCell(cell6);

            Phrase lieuf = new Phrase(new Chunk("à: ", french2));
            Phrase valuelieuf;
            if (e.getLieuDeNaissance() != null) {
                valuelieuf = new Phrase(new Chunk(e.getLieuDeNaissance() + "\n", french));
            } else {
                valuelieuf = new Phrase(new Chunk("Unknown place" + "\n", french));
            }
            Phrase lieue = new Phrase(new Chunk("at: ", english2));
            Phrase lieu = new Phrase();
            lieu.add(lieuf);
            lieu.add(valuelieuf);
            lieu.add(lieue);
            PdfPCell cell7 = new PdfPCell(lieu);
            cell7.setBorderColor(BaseColor.WHITE);
            table.addCell(cell7);

            Phrase sexef = new Phrase(new Chunk("Sexe: ", french2));
            String ssexe = e.getGenre().toString();
            ssexe = ssexe.substring(0, 1).toUpperCase() + ssexe.substring(1);
            Phrase valuesexef = new Phrase(new Chunk(ssexe + "\n", french));
            Phrase sexee = new Phrase(new Chunk("Sex: ", english2));
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

    private void produceRelevetTable(Document doc, List<UEnseignementCredit> ues1, List<UEnseignementCredit> ues2, RelevetEtudiantNotesInfos infos) {
        try {

            Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font bf1 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            Font bf2 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);
            List<UEnseignementCredit> ues = new ArrayList<>();
            ues.addAll(ues1);
            ues.addAll(ues2);
            //int nombreCredit = 0;
            //int nombreCreditValide = 0;
            boolean aToutValide = true;

            float relativeWidths[] = {2.5F, 10, 2, 3, 3, 2, 3.5F, 2};
            PdfPTable table = new PdfPTable(relativeWidths);
            table.setSpacingBefore(5f);
            table.setWidthPercentage(100);
            table.addCell(CreerTitre("Code UE", "Credit Code", bf, bf2, true, true));
            table.addCell(CreerTitre("Intitulé de l'Unité d'Enseignement", "Course Title", bf, bf2, true, true));
            table.addCell(CreerTitre("Crédit", "Credit", bf, bf2, true, true));
            table.addCell(CreerTitre("Moyenne/20", "Average/20", bf, bf2, true, true));
            table.addCell(CreerTitre("Moy/Grade", "GPA", bf, bf2, true, true));
            table.addCell(CreerTitre("Grade", "Grade", bf, bf2, true, true));
            table.addCell(CreerTitre("Semestre", "Semester", bf, bf2, true, true));
            table.addCell(CreerTitre("Session", "Session", bf, bf2, true, true));
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
            float paddingTop = 6f, paddingBottom = 6f;
            for (UEnseignementCredit ue : ues) {
                if (ue.getCredit() != 0) {
                    Double value = notes.get(ue.getCodeUE());
                    if (value < 10) {
                        aToutValide = false;
                    }
                    //nombreCredit += ue.getCredit();
                    Session session = sessions.get(ue.getCodeUE());
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, false, false,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, false, false,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(value < 10 ? "0" : String.valueOf(ue.getCredit()), bf1, false, true,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, false, true,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.valueOf(mgp.get(ue.getCodeUE())), bf1, false, true,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.transformNoteGradeUE(value), bf1, false, true,paddingTop,paddingBottom));

                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(semtrs.get(ue.getCodeUE()), bf1, false, true,paddingTop,paddingBottom));
                    table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.sessionToString(session), bf1, false, true,paddingTop,paddingBottom));
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
            float widths[] = {1, 3, 2, 3, 2, 8};
            PdfPTable table2 = new PdfPTable(widths);
            table2.setSpacingBefore(5f);
            table2.setWidthPercentage(100);
            table2.addCell(CreerTitre("Rang", "Rank", bf, bf2, false, true));
            table2.addCell(CreerTitre("Crédits Capitalisés", "Total Credits", bf, bf2, false, true));
            table2.addCell(CreerTitre("Moy / Grad", "GPA", bf, bf2, false, true));
            table2.addCell(CreerTitre("Grade général", "General Grade", bf, bf2, false, true));
            table2.addCell(CreerTitre("Décision", "Decision", bf, bf2, false, true));
            table2.addCell(CreerTitre("Mention", "Rank", bf, bf2, false, true));
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
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.valueOf(infos.getRang()) : "", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(String.valueOf(infos.getNombreCreditValides()), bf, false, 1, 1));
            //table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.format("%.2f", infos.getMoyenne()) : "", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? String.valueOf(infos.getMoyenneMgp()) : "", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? DocumentUtil.transformMoyenneMgpToGradeRelevet(infos.getMoyenneMgp()) : "", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell((infos.getMoyenne() >= 10) ? "AD" : "RD", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(aToutValide ? DocumentUtil.transformMoyenneMgpToMentionRelevet(infos.getMoyenneMgp()) : "", bf, false, 2, 2));

            cell.addElement(table2);
            cell.setColspan(8);
            cell.setRowspan(6);
            table.addCell(cell);
            doc.add(table);

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
        cl.setHorizontalAlignment(estCentre?Element.ALIGN_CENTER:Element.ALIGN_LEFT);
        cl.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cl.setPaddingBottom(4f);
        cl.setPaddingTop(5f);
        if(avecBordure){
            cl.setBorderWidth(0.01f);
            cl.setBorderColor(BaseColor.BLACK);
        }else{
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
            hh.add(new Phrase("En foi de quoi ce relevé de notes lui est délivré pour servir et valoir ce que de droit.", font3));
            hh.add("\n");
            hh.add(new Phrase("This academic transcript is issued to serve where and when ever neccessary.", font4));
            PdfPCell cell1 = new PdfPCell(hh);
            
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell1.setVerticalAlignment(Element.ALIGN_LEFT);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setPaddingBottom(15f);
            date.addCell(cell1);
            date.addCell(removeBorder(new PdfPCell()));
            date.addCell(removeBorder(new PdfPCell()));

            PdfPCell cell2 = new PdfPCell();
            cell2.addElement(new Phrase("Maroua, le : ________________", font2));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setBorderColor(BaseColor.WHITE);
            date.addCell(cell2);
            doc.add(date);
            //doc.add(new Phrase("\n"));
            
            PdfPTable cachet = new PdfPTable(new float[]{3,4,6});
            cachet.setSpacingBefore(4f);
            cachet.setWidthPercentage(90);
            /*PdfPCell cell3 = new PdfPCell();
            cell3.addElement(new Phrase(, font1));
            cell3.addElement(new Phrase(, font2));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderColor(BaseColor.WHITE);*/
            cachet.addCell(CreerTitre("Le Chef de Département", "The Head of Department", font1, font2, false, true));
            /*PdfPCell cell4 = new PdfPCell();
            cell4.addElement(new Phrase(, font1));
            cell4.addElement(new Phrase(, font2));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderColor(BaseColor.WHITE);*/
            cachet.addCell(removeBorder(new PdfPCell()));
            cachet.addCell(CreerTitre("Le Directeur", "The Director", font1, font2, false, true));
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

    public class Watermark extends PdfPageEventHelper {

        protected Paragraph pied;
        protected Paragraph pied2;
        protected Phrase GPA = new Phrase("GPA = Grade Point Average (moyenne par grade). A+=[3,6 4] (Excellent / First Class Hons.), A- = [3,2 3,6[ (Très Bien / Second Class Hons. Upper Division), B = [2,8 3,2[ (Bien / Second Class Hons. Lower Division), B- = [2,4 2,8[ (Assez-Bien / Third Class), C = [2 2,4[ (Passable / Pass)", new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL, BaseColor.BLACK));
        protected Image anotherWatermark;

        public Watermark() {
            try {
                pied = new Paragraph("Ce relevé de notes, pour être valide, ne doit contenir ni surcharge, ni rature. Le titulaire devra faire certifier les copies conformes en cas de besoin.",new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL, BaseColor.BLACK));
                pied2 = new Paragraph("This academic transcript is only valid when it contains neither overload neither cross off. The holder will have to make certified true copies when necessary.",new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.ITALIC, BaseColor.BLACK));
                //pied = new Paragraph(builder.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL, BaseColor.BLACK));
                URL url = new ClassPathResource("watermark.png").getURL();
                anotherWatermark = Image.getInstance(url);

            } catch (IOException | BadElementException ex) {
                Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfContentByte canvas = writer.getDirectContentUnder();
                //ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
                //ColumnText.s
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, pied, 40, 50, 0);
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, pied2, 40, 46, 0);
                canvas.addImage(anotherWatermark, anotherWatermark.getWidth(), 0, 0, anotherWatermark.getHeight(), 110, 240);
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, GPA, 575, 690, 270);
            } catch (DocumentException ex) {
                Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Map<String, RelevetEtudiantNotesInfos> computeInfosOfAllStudents(List<Etudiant> etudiants, List<Semestre> semestres, List<UniteEnseignement> uniteEns1, List<UniteEnseignement> uniteEns2, List<UEnseignementCredit> ues1, List<UEnseignementCredit> ues2, AnneeAcademique a) {
        Map<String, RelevetEtudiantNotesInfos> resultTmp = new HashMap<>();
        for (Etudiant e : etudiants) {
            RelevetEtudiantNotesInfos infos = new RelevetEtudiantNotesInfos();
            double produit = 0;
            double produitMgp = 0;
            int nombreCredit = 0;
            int nombreCreditValide = 0;
            try {

                Map<String, MoyenneUniteEnseignement> notes1 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns1);
                Map<String, MoyenneUniteEnseignement> notes2 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), a.getId(), uniteEns2);
                Map<String, Double> notes = new HashMap<>();
                Map<String, Double> mgp = new HashMap<>();
                Map<String, Session> sessions = new HashMap<>();
                Map<String, String> semes = new HashMap<>();
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
                resultTmp.put(e.getMatricule(), infos);
                // Je ne sais toujours pas comment obtenir le rang
            } catch (ServiceException ex) {
                Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        resultTmp = sortByComparator(resultTmp);
        int rank = 1;
        Map<String, RelevetEtudiantNotesInfos> result = new HashMap<>();
        for (String key : resultTmp.keySet()) {
            RelevetEtudiantNotesInfos tmp = resultTmp.get(key);
            tmp.setRang(rank);
            result.put(key, tmp);
            ++rank;
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
