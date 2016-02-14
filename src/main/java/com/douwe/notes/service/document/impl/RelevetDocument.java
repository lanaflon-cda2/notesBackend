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
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.MoyenneTrashData;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.projection.UEnseignementCredit;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.IRelevetDocument;
import com.douwe.notes.service.impl.DocumentServiceImpl;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            doc.open();
            Niveau n = niveauDao.findById(niveauId);
            Option o = optionDao.findById(optionId);
            AnneeAcademique a = academiqueDao.findById(anneeId);
            List<Semestre> semestres = semestreDao.findByNiveau(n);

            //Liste des ues du semestre 1
            List<UniteEnseignement> uniteEns1 = uniteEnsDao.findByUniteNiveauOptionSemestre(n, o, semestres.get(0), a);
//            System.out.println("J'ai pris ======= " + uniteEns1.size());
//            for (UniteEnseignement uniteEns11 : uniteEns1) {
//                System.out.println(uniteEns11);
//            }
            List<UniteEnseignement> uniteEns2 = uniteEnsDao.findByUniteNiveauOptionSemestre(n, o, semestres.get(1), a);
             List<UEnseignementCredit> ues1 = uniteEnsDao.findByNiveauOptionSemestre(n, o, semestres.get(0), a);
            List<UEnseignementCredit> ues2 = uniteEnsDao.findByNiveauOptionSemestre(n, o, semestres.get(1), a);
            List<Etudiant> etudiants = etudiantDao.listeEtudiantParDepartementEtNiveau(o.getDepartement(), a, n, o);
            for (Etudiant etudiant : etudiants) {
                produceRelevetEtudiant(doc,etudiant, n,  o,  a, semestres, uniteEns1, uniteEns2, ues1, ues2);
                doc.newPage();
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
    
    private void produceRelevetEtudiant(Document doc, Etudiant e, Niveau n, Option o, AnneeAcademique a,List<Semestre> semestres, List<UniteEnseignement> uniteEns1, List<UniteEnseignement> uniteEns2, List<UEnseignementCredit> ues1, List<UEnseignementCredit> ues2){
        try {
            common.produceDocumentHeader(doc, null, n, o, a, null, null, null, true);
            produceRelevetTitle(doc, o.getDepartement());
            // doc.add(new Chunk("\n"));
            produireRelevetHeader(e, o.getDepartement(), n, o, doc);
            produceRelevetTable(doc,e ,n , o, a, semestres,uniteEns1,uniteEns2, ues1, ues2);
            doc.add(new Chunk("\n"));
            produceRelevetFooter(doc);
        } catch (Exception ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetTitle(Document doc, Departement departement) {
        try {
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            doc.add(new Phrase("\n"));
            StringBuilder str = new StringBuilder();
            
            str.append("RELEVE DE NOTES / ACADEMIC TRANSCRIPT             ");
            str.append("N°");
            str.append("/_______/");
            str.append(departement.getCode());
            str.append("DAACR/");
            str.append("DISS\n");
            Phrase phrase = new Phrase(str.toString(), font);
            doc.add(phrase);
        } catch (DocumentException ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produireRelevetHeader(Etudiant e, Departement d, Niveau n, Option o, Document doc) {

        try {

            Font french = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font english = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.BOLDITALIC);
            Font french2 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            Font english2 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.ITALIC);
            Font bf1 = new Font(Font.FontFamily.TIMES_ROMAN, 6);
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 5);

            float relativeWidths[];
            relativeWidths = new float[3];
            relativeWidths[0] = 3;
            relativeWidths[1] = 6;
            relativeWidths[2] = 3;
            PdfPTable table = new PdfPTable(relativeWidths);
            table.setWidthPercentage(100);

            Phrase cyclef = new Phrase("Cycle : ", french2);
            Phrase valuecyclef = new Phrase( n.getCycle().getDiplomeFr() + " en " + o.getDepartement().getFrenchDescription()+ "\n", french);
            Phrase cyclee = new Phrase("cycle : ", english2);
            Phrase valuecyclee = new Phrase(n.getCycle().getDiplomeEn() + " en " + o.getDepartement().getEnglishDescription()+ "\n", english);
            Phrase cycle = new Phrase();
            cycle.add(cyclef);
            cycle.add(valuecyclef);
            cycle.add(cyclee);
            cycle.add(valuecyclee);
            PdfPCell cell1 = new PdfPCell(cycle);
            cell1.setColspan(2);
            cell1.setBorderColor(BaseColor.WHITE);
            table.addCell(cell1);

            Phrase niveauf = new Phrase("Niveau : ", french2);
            Phrase valueniveauf = new Phrase(n.getCode() + "\n", french);
            Phrase niveaue = new Phrase("Level", english2);
            Phrase niveau = new Phrase();
            niveau.add(niveauf);
            niveau.add(valueniveauf);
            niveau.add(niveaue);
            PdfPCell cell2 = new PdfPCell(niveau);
            cell2.setBorderColor(BaseColor.WHITE);
            table.addCell(cell2);

            Phrase spf = new Phrase(new Chunk("Spécialité : ", french2));
            Phrase valuespf = new Phrase(new Chunk(o.getDepartement().getFrenchDescription() + "\n", french));
            Phrase spe = new Phrase(new Chunk("Speciality : ", english2));
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

            Phrase nomf = new Phrase(new Chunk("Nom(s) et Prénom(s) :", french2));
            Phrase valuenomf = new Phrase(new Chunk(e.getNom() + "\n", french));
            Phrase nome = new Phrase(new Chunk("Name and surename", english2));
            Phrase nom = new Phrase();
            nom.add(nomf);
            nom.add(valuenomf);
            nom.add(nome);
            PdfPCell cell4 = new PdfPCell(nom);
            cell4.setBorderColor(BaseColor.WHITE);
            cell4.setColspan(2);
            table.addCell(cell4);

            Phrase matf = new Phrase(new Chunk("Matricule : ", french2));
            Phrase valuematf = new Phrase(new Chunk(e.getMatricule() + "\n", french));
            Phrase mate = new Phrase(new Chunk("Registration number", english2));
            Phrase matricule = new Phrase();
            matricule.add(matf);
            matricule.add(valuematf);
            matricule.add(mate);
            PdfPCell cell5 = new PdfPCell(matricule);
            cell5.setBorderColor(BaseColor.WHITE);
            table.addCell(cell5);

            Phrase datef = new Phrase(new Chunk("Né(e) le : ", french2));
            System.out.println(e);
            Phrase valuedatef;
            if(e.getDateDeNaissance() != null){
                valuedatef = new Phrase(new Chunk(e.getDateDeNaissance().toString() + "\n", french));
            }
            else{
                valuedatef = new Phrase(new Chunk("Unknown" + "\n", french));
            }
            Phrase datee = new Phrase(new Chunk("Born on", english2));
            Phrase date = new Phrase();
            date.add(datef);
            date.add(valuedatef);
            date.add(datee);
            PdfPCell cell6 = new PdfPCell(date);
            cell6.setBorderColor(BaseColor.WHITE);
            table.addCell(cell6);

            Phrase lieuf = new Phrase(new Chunk("à : ", french2));
            Phrase valuelieuf;
            if(e.getLieuDeNaissance() != null){
                valuelieuf = new Phrase(new Chunk(e.getLieuDeNaissance() + "\n", french));
            }
            else{
                valuelieuf = new Phrase(new Chunk("unknown place" + "\n", french));
            }
            Phrase lieue = new Phrase(new Chunk("at", english2));
            Phrase lieu = new Phrase();
            lieu.add(lieuf);
            lieu.add(valuelieuf);
            lieu.add(lieue);
            PdfPCell cell7 = new PdfPCell(lieu);
            cell7.setBorderColor(BaseColor.WHITE);
            table.addCell(cell7);

            Phrase sexef = new Phrase(new Chunk("Sexe : ", french2));
            Phrase valuesexef = new Phrase(new Chunk(e.getGenre().toString() + "\n", french));
            Phrase sexee = new Phrase(new Chunk("Sexe", english2));
            Phrase sexe = new Phrase();
            sexe.add(lieuf);
            sexe.add(valuelieuf);
            sexe.add(lieue);
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

    private void produceRelevetTable(Document doc, Etudiant e, Niveau n, Option o, AnneeAcademique a,List<Semestre> semestres, List<UniteEnseignement> uniteEns1, List<UniteEnseignement> uniteEns2,List<UEnseignementCredit> ues1, List<UEnseignementCredit> ues2 ) {
        try {
            
            //List<UEnseignementCredit> ues1 = getTrash1();
            //List<UEnseignementCredit> ues2 = getTrash2();
            Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font bf1 = new Font(Font.FontFamily.TIMES_ROMAN, 6);
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 5);
            int taille1 = ues1.size();
            int taille2 = ues2.size();
            int nombreCredit = 0;
            int nombreCreditValide = 0;
            double produit = 0.0; /*produit : credit * moyenne */

            float relativeWidths[];
            relativeWidths = new float[8];
            relativeWidths[0] = 3;
            relativeWidths[1] = 10;
            relativeWidths[2] = 3;
            for (int i = 0; i < 5; i++) {
                relativeWidths[3 + i] = 3;
            }
            PdfPTable table = new PdfPTable(relativeWidths);
            table.setWidthPercentage(100);
            table.addCell(DocumentUtil.createDefaultBodyCell("Code UE", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Intitulé de l'Unité d'Enseignement", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Crédit", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Moyenne/20", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Moy/Grade", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Grade", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Semestre", bf, false));
            table.addCell(DocumentUtil.createDefaultBodyCell("Session", bf, false));
            
            Map<String, MoyenneUniteEnseignement> notes1 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), uniteEns1);
            Map<String, MoyenneUniteEnseignement> notes2 = noteService.listeNoteUniteEnseignement(e.getMatricule(), a.getId(), uniteEns2);
            //Map<String, MoyenneTrashData> notes = getTrash3();
            //System.out.println("code à : " + notes.containsKey("ITEL 110"));

            /* table.addCell(createSyntheseDefaultBodyCell(String.valueOf(i++), bf1, false, true));
             table.addCell(createSyntheseDefaultBodyCell(e.getNom(), bf1, false, false));
             table.addCell(createSyntheseDefaultBodyCell(e.getMatricule(), bf1, false, true));
             */
            /*            for (UEnseignementCredit ue : ues1) {
             MoyenneUniteEnseignement mue = notes.get(ue.getCodeUE());
             Double value = mue.getMoyenne();
             table.addCell(createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(String.valueOf(ue.getCredit()), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("3,4", bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(transformNoteGrade(value), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("I(2023)", bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("II", bf1, true, true));

             }
             for (UEnseignementCredit ue : ues2) {
             MoyenneUniteEnseignement mue = notes.get(ue.getCodeUE());
             Double value = mue.getMoyenne();
             table.addCell(createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(String.valueOf(ue.getCredit()), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("3,4", bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell(transformNoteGrade(value), bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("I(2023)", bf1, true, true));
             table.addCell(createSyntheseDefaultBodyCell("II", bf1, true, true));
             }            
             */
            for (UEnseignementCredit ue : ues1) {
                System.out.println("code : " + ue.getCodeUE());
                //MoyenneTrashData mue = notes.get(ue.getCodeUE());
                System.out.println("tooooooooo     "+notes1.get(ue.getCodeUE()));
                MoyenneUniteEnseignement mue = notes1.get(ue.getCodeUE());
                System.out.println(mue);
                Double value = mue.getMoyenne();
                produit += value * ue.getCredit();
                nombreCredit += ue.getCredit();
                if (value >= 10) {
                    nombreCreditValide += ue.getCredit();
                }
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.valueOf(ue.getCredit()), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell("3,4", bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.transformNoteGrade(value), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell("I(2023)", bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.sessionToString(mue.getSession()), bf1, false, true));

            }
            for (UEnseignementCredit ue : ues2) {
                //MoyenneTrashData mue = notes.get(ue.getCodeUE());
                 MoyenneUniteEnseignement mue = notes2.get(ue.getCodeUE());
                Double value = mue.getMoyenne();
                nombreCredit += ue.getCredit();
                produit += value * ue.getCredit();
                if (value >= 10) {
                    nombreCreditValide += ue.getCredit();
                }
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getCodeUE(), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(ue.getIntituleUE(), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.valueOf(ue.getCredit()), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(String.format("%.2f", value), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell("3,4", bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.transformNoteGrade(value), bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell("I(2023)", bf1, false, true));
                table.addCell(DocumentUtil.createSyntheseDefaultBodyCell(DocumentUtil.sessionToString(mue.getSession()), bf1, false, true));
            }
            PdfPCell cell = new PdfPCell();
//                 float relativeWidths2[];
//            relativeWidths2 = new float[8];
//            relativeWidths2[0] = 3;
//            relativeWidths[1] = 10;
//            relativeWidths[2] = 3;
//            for (int i = 0; i < 8; i++) {
//                relativeWidths2[3 + i] = 3;
//            }
            PdfPTable table2 = new PdfPTable(8);
            table2.setWidthPercentage(90);
            table2.addCell(createRelevetFootBodyCell("Rang", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Crédit Capitalisé", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Moy /20", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Moy / Grad", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Grade général", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Décision", bf, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Mention", bf, false, 1, 2));

            table2.addCell(createRelevetFootBodyCell("Rank", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Total Credit", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Average /20", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("GPA", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("General Grade", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("Decision", bf1, false, 1, 1));
            table2.addCell(createRelevetFootBodyCell("", bf1, false, 1, 2));
            /*Les valeurs   */

            // Sais pas comment obtenir le rang
            table2.addCell(DocumentUtil.createRelevetFootBodyCell("6", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(String.valueOf(nombreCreditValide), bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(String.format("%.2f", (produit * 1.0 / nombreCredit)), bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell("2,70", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell(DocumentUtil.transformNoteGrade(produit * 1.0 / nombreCredit), bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell("AD", bf, false, 1, 1));
            table2.addCell(DocumentUtil.createRelevetFootBodyCell("ASSEZ-Bien", bf, false, 2, 2));

            cell.addElement(table2);
            cell.setColspan(8);
            cell.setRowspan(6);
            table.addCell(cell);
            doc.add(table);

        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(RelevetDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produceRelevetFooter(Document doc) {
        try {
            // Font frenchFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

            Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);
            Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);
            Font font4 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.ITALIC);

            float relativewidhts[] = new float[2];
            relativewidhts[0] = 6;
            relativewidhts[1] = 5;
            PdfPTable date = new PdfPTable(relativewidhts);
            date.setTotalWidth(100);
            PdfPCell cell1 = new PdfPCell();
            cell1.addElement(new Phrase("En foi de quoi ce relevé de notes lui est délivré pour servir et valoir ce que de droit.", font3));
            cell1.addElement(new Phrase("This academic transcript is issued to serve where and when ever neccessary.", font4));
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_LEFT);
            cell1.setBorderColor(BaseColor.WHITE);
            date.addCell(cell1);

            PdfPCell cell2 = new PdfPCell();
            cell2.addElement(new Phrase("Maroua, le : ____________", font2));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setBorderColor(BaseColor.WHITE);
            date.addCell(cell2);
            doc.add(date);
            doc.add(new Phrase("\n"));

            PdfPTable cachet = new PdfPTable(2);
            cachet.setWidthPercentage(80);
            PdfPCell cell3 = new PdfPCell();
            cell3.addElement(new Phrase("Le Chef de Département", font1));
            cell3.addElement(new Phrase("The Head of Department", font2));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderColor(BaseColor.WHITE);
            cachet.addCell(cell3);
            PdfPCell cell4 = new PdfPCell();
            cell4.addElement(new Phrase("Le Directeur", font1));
            cell4.addElement(new Phrase("The Director", font2));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderColor(BaseColor.WHITE);
            cachet.addCell(cell4);
            doc.add(cachet);

        } catch (DocumentException ex) {
            Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private PdfPCell createRelevetFootBodyCell(String message, Font bf, boolean border, int rowspan, int colspan) {
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
    }

    private List<UEnseignementCredit> getTrash1() {
        List<UEnseignementCredit> result = new ArrayList<UEnseignementCredit>();
        result.add(new UEnseignementCredit("ITEL 110", "Mathematiques 1", 4));
        result.add(new UEnseignementCredit("ITEL 111", "Circuit Logique 1", 4));
        result.add(new UEnseignementCredit("ITEL 112", "Electronique de Base", 4));
        result.add(new UEnseignementCredit("ITEL 113", "Circuits Electriques", 2));
        result.add(new UEnseignementCredit("ITEL 114", "Electromagnetisme", 4));
        result.add(new UEnseignementCredit("ITEL 115", "Algorithme et Programmation", 6));
        result.add(new UEnseignementCredit("ITEL 116", "Mecanique du Point Materiel", 2));
        result.add(new UEnseignementCredit("ITEL 117", "Communication Technique 1", 4));
        return result;
    }

    private List<UEnseignementCredit> getTrash2() {
        List<UEnseignementCredit> result = new ArrayList<UEnseignementCredit>();
        result.add(new UEnseignementCredit("ITEL 120", "Mathématiques 2", 4));
        result.add(new UEnseignementCredit("ITEL 121", "Circuits Logiques 2", 4));
        result.add(new UEnseignementCredit("ITEL 122", "Système d’Exploitation 1", 4));
        result.add(new UEnseignementCredit("ITEL 123", "Programmation Avancée en C", 2));
        result.add(new UEnseignementCredit("ITEL 124", "Programmation et Technologies Web", 4));
        result.add(new UEnseignementCredit("ITEL 125", "Électromagnétisme 2", 4));
        result.add(new UEnseignementCredit("ITEL 126", "Dessin Technique et Fabrication Mécanique", 2));
        result.add(new UEnseignementCredit("ITEL 127", "Communication Technique II", 4));
        result.add(new UEnseignementCredit("ITEL 128", "Stage", 2));
        return result;
    }

    private Map<String, MoyenneTrashData> getTrash3() {

        Map<String, MoyenneTrashData> result = new HashMap<String, MoyenneTrashData>();
        MoyenneTrashData data1 = new MoyenneTrashData(12.14, Session.normale, new Semestre("1"));
        result.put("ITEL 110", data1);
        MoyenneTrashData data2 = new MoyenneTrashData(13.20, Session.normale, new Semestre("1"));
        result.put("ITEL 111", data2);
        MoyenneTrashData data3 = new MoyenneTrashData(13.75, Session.normale, new Semestre("1"));
        result.put("ITEL 112", data3);
        MoyenneTrashData data4 = new MoyenneTrashData(11.30, Session.normale, new Semestre("1"));
        result.put("ITEL 113", data4);
        MoyenneTrashData data5 = new MoyenneTrashData(14.01, Session.normale, new Semestre("1"));
        result.put("ITEL 114", data5);
        MoyenneTrashData data6 = new MoyenneTrashData(10.20, Session.normale, new Semestre("1"));
        result.put("ITEL 115", data6);
        MoyenneTrashData data7 = new MoyenneTrashData(16.65, Session.normale, new Semestre("1"));
        result.put("ITEL 116", data7);
        MoyenneTrashData data8 = new MoyenneTrashData(10.05, Session.normale, new Semestre("1"));
        result.put("ITEL 117", data8);
        MoyenneTrashData data9 = new MoyenneTrashData(11.46, Session.rattrapage, new Semestre("II"));
        result.put("ITEL 120", data9);
        MoyenneTrashData data10 = new MoyenneTrashData(12.30, Session.normale, new Semestre("II"));
        result.put("ITEL 121", data10);
        MoyenneTrashData data11 = new MoyenneTrashData(11.55, Session.normale, new Semestre("II"));
        result.put("ITEL 122", data11);
        MoyenneTrashData data12 = new MoyenneTrashData(10.19, Session.normale, new Semestre("II"));
        result.put("ITEL 123", data12);
        MoyenneTrashData data13 = new MoyenneTrashData(12.60, Session.rattrapage, new Semestre("II"));
        result.put("ITEL 124", data13);
        MoyenneTrashData data14 = new MoyenneTrashData(11.18, Session.rattrapage, new Semestre("II"));
        result.put("ITEL 125", data14);
        MoyenneTrashData data15 = new MoyenneTrashData(11.05, Session.normale, new Semestre("II"));
        result.put("ITEL 126", data15);
        MoyenneTrashData data16 = new MoyenneTrashData(16.00, Session.normale, new Semestre("II"));
        result.put("ITEL 127", data16);
        MoyenneTrashData data17 = new MoyenneTrashData(16.00, Session.normale, new Semestre("II"));
        result.put("ITEL 128", data17);
        return result;
    }

    public class Watermark extends PdfPageEventHelper {

        protected Phrase watermark = new Phrase("ORIGINAL", new Font(Font.FontFamily.HELVETICA, 70, Font.BOLDITALIC, new BaseColor(254, 248, 108)));

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
        }
    }

}
