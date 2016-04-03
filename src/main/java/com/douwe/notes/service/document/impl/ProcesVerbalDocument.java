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
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Credit;
import com.douwe.notes.entities.Evaluation;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Session;
import com.douwe.notes.projection.EtudiantNotes;
import com.douwe.notes.projection.StatistiquesNote;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.IProcesVerbalDocument;
import com.douwe.notes.service.impl.DocumentServiceImpl;
import com.itextpdf.text.BaseColor;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
public class ProcesVerbalDocument implements IProcesVerbalDocument{
    
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
    public String produirePv(Long niveauId, Long optionId, Long coursId, Long academiqueId, int session, OutputStream stream) throws ServiceException {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, stream);
            doc.setPageSize(PageSize.A4);
            doc.open();

            Niveau niveau = niveauDao.findById(niveauId);
            Option option = optionDao.findById(optionId);
            Cours cours = coursDao.findById(coursId);
            AnneeAcademique anne = academiqueDao.findById(academiqueId);
            Session s = Session.values()[session];
            Programme prog = programmeDao.findByCours(cours, niveau, option, anne);
            Credit credit = creditDao.findByCours(cours, niveau, option, anne);
            common.produceDocumentHeader(doc, cours, niveau, option, anne, s, prog, credit, false);
            StatistiquesNote stats = produceBody(doc, cours, niveau, option, anne, s, true);
            producePvFooter(doc, stats);
            doc.newPage();
            common.produceDocumentHeader(doc, cours, niveau, option, anne, s, prog, credit, false);
            produceBody(doc, cours, niveau, option, anne, s, false);
            doc.close();
        } catch (DataAccessException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
        // TODO I need to change something here because this method is called twice and every times it needs to load the data from the database
    // Can we load the data only once ?
    private StatistiquesNote produceBody(Document doc, Cours c, Niveau n, Option o, AnneeAcademique a, Session s, boolean avecNoms) throws Exception {
        StatistiquesNote result = new StatistiquesNote();
        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
        int sup15 = 0;
        int inf10 = 0;
        int entre1014 = 0;
        Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
        List<Evaluation> evals = EvaluationDao.evaluationForCourses(c);
        // List<Evaluation> evals = evaluationService.getAllEvaluationByCours(c.getId());
        int taille = evals.size();
        float relativeWidths[];
        if (avecNoms) {
            relativeWidths = new float[5 + taille];
            relativeWidths[0] = 1;
            relativeWidths[1] = 3;
            relativeWidths[2] = 10;
            for (int i = 0; i < taille + 2; i++) {
                relativeWidths[3 + i] = 2;
            }
        } else {
            relativeWidths = new float[4 + taille];
            relativeWidths[0] = 1;
            relativeWidths[1] = 3;
            for (int i = 0; i < taille + 2; i++) {
                relativeWidths[2 + i] = 2;
            }
        }
        PdfPTable table = new PdfPTable(relativeWidths);

        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.addCell(DocumentUtil.createDefaultHeaderCell("No", bf));
        table.addCell(DocumentUtil.createDefaultHeaderCell("Matricule", bf));
        if (avecNoms) {
            table.addCell(DocumentUtil.createDefaultHeaderCell("NOM(s) et PRENOM(s)", bf));
        }

        for (Evaluation eval : evals) {
            table.addCell(DocumentUtil.createDefaultHeaderCell(String.format("%s / 20 ", eval.getCode()), bf));
        }
        table.addCell(DocumentUtil.createDefaultHeaderCell("Moy / 20", bf));
        table.addCell(DocumentUtil.createDefaultHeaderCell("Grade", bf));
        table.setSpacingBefore(10f);
        List<EtudiantNotes> notes = noteService.getAllNotesEtudiants(n, o, c, null, a, s);
        if (avecNoms) {
            table.setWidthPercentage(98);
        } else {
            table.setWidthPercentage(60);
            // Maybe I need to do this on the database
            Collections.sort(notes, new Comparator<EtudiantNotes>() {

                @Override
                public int compare(EtudiantNotes t, EtudiantNotes t1) {
                    return t.getMatricule().compareToIgnoreCase(t1.getMatricule());
                }

            });
        }

        int i = 0;
        for (EtudiantNotes note : notes) {
            table.addCell(DocumentUtil.createDefaultBodyCell(String.valueOf(++i), bf12, false));
            table.addCell(DocumentUtil.createDefaultBodyCell(note.getMatricule(), bf12, false));
            if (avecNoms) {
                PdfPCell cell =DocumentUtil. createDefaultBodyCell(note.getNom().toUpperCase(), bf12, false);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }
            for (Evaluation eval : evals) {
                Double value = note.getNote().get(eval.getCode());
                table.addCell(DocumentUtil.createDefaultBodyCell((value == null) ? "" : String.format("%.2f", value), bf12, false));

            }
            double moyenne = note.getMoyenne();
            table.addCell(DocumentUtil.createDefaultBodyCell(String.format("%.2f", moyenne), bf12, false));
            table.addCell(DocumentUtil.createDefaultBodyCell(DocumentUtil.transformNoteGradeUE(moyenne), bf12, false));
            if (moyenne > max) {
                max = moyenne;
            }
            if (moyenne < min) {
                min = moyenne;
            }
            if (moyenne >= 15) {
                sup15++;
            } else if (moyenne >= 10) {
                entre1014++;
            } else {
                inf10++;
            }
        }
        doc.add(table);
        table = new PdfPTable(3);
        table.setWidthPercentage(95);
        PdfPCell cell = DocumentUtil.createDefaultHeaderCell("Président", bf);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.WHITE);
        table.addCell(cell);
        cell = DocumentUtil.createDefaultHeaderCell("Vice-Président", bf);
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = DocumentUtil.createDefaultHeaderCell("Membre(s)", bf);
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        table.setSpacingBefore(15f);
        table.setSpacingAfter(25f);

        doc.add(table);
        result.setEffectif(notes.size());
        result.setNombreMoyenneEntre10et15(entre1014);
        result.setNombreMoyenneInferieurA10(inf10);
        result.setNombreMoyenneSuperieureQuinze(sup15);
        result.setPlusGrandeMoyenne(max);
        result.setPlusPetiteMoyenne(min);
        return result;
    }
    
      private void producePvFooter(Document doc, StatistiquesNote stats) throws Exception {
        Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        float widths[] = {5, 3, 3};
        PdfPTable pourcentage = new PdfPTable(widths);
        pourcentage.getDefaultCell().setBorder(0);
        pourcentage.getDefaultCell().setPaddingBottom(5f);
        pourcentage.getDefaultCell().setPaddingTop(5f);
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase("Pourcentage(%)", bf));
        pourcentage.addCell(new Phrase("Moyenne Comprise entre 15 et 20", bf));
        pourcentage.addCell(new Phrase(String.valueOf(stats.getNombreMoyenneSuperieureQuinze()), bf));
        pourcentage.addCell(new Phrase(String.format("%.2f", 100 * ((stats.getNombreMoyenneSuperieureQuinze() * 1.0) / stats.getEffectif())), bf));
        pourcentage.addCell(new Phrase("Moyenne Comprise entre 14,99 et 10", bf));
        pourcentage.addCell(new Phrase(String.valueOf(stats.getNombreMoyenneEntre10et15()), bf));
        pourcentage.addCell(new Phrase(String.format("%.2f", 100 * ((stats.getNombreMoyenneEntre10et15() * 1.0) / stats.getEffectif())), bf));
        pourcentage.addCell(new Phrase("Moyenne < 10", bf));
        pourcentage.addCell(new Phrase(String.valueOf(stats.getNombreMoyenneInferieurA10()), bf));
        pourcentage.addCell(new Phrase(String.format("%.2f", 100 * ((stats.getNombreMoyenneInferieurA10() * 1.0) / stats.getEffectif())), bf));
        pourcentage.addCell(new Phrase("Effectif Total des Etudiants", bf));
        pourcentage.addCell(new Phrase(String.format(String.valueOf(stats.getEffectif())), bf));
        pourcentage.addCell("");
        pourcentage.addCell(new Phrase("Plus Grande MOY (Max)", bf));
        pourcentage.addCell(new Phrase(String.format("%.2f", stats.getPlusGrandeMoyenne()), bf));
        pourcentage.addCell("");
        pourcentage.addCell(new Phrase("Plus Petite MOY (Min)", bf));
        pourcentage.addCell(new Phrase(String.format("%.2f", stats.getPlusPetiteMoyenne()), bf));
        pourcentage.addCell("");
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase("Taux de Réussite >=10", bf));
        pourcentage.addCell(new Phrase());
        pourcentage.addCell(new Phrase(String.format("%.2f", 100 * ((stats.getNombreMoyenneEntre10et15() + stats.getNombreMoyenneSuperieureQuinze()) * 1.0 / stats.getEffectif())), bf));
        pourcentage.setSpacingBefore(15f);
        doc.add(pourcentage);
    }
   
}
