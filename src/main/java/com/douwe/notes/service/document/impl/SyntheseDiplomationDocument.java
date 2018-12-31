package com.douwe.notes.service.document.impl;

import com.douwe.generic.dao.DataAccessException;
import com.douwe.notes.config.MessageHelper;
import com.douwe.notes.dao.IAnneeAcademiqueDao;
import com.douwe.notes.dao.ICycleDao;
import com.douwe.notes.dao.IDepartementDao;
import com.douwe.notes.dao.IEtudiantDao;
import com.douwe.notes.dao.INiveauDao;
import com.douwe.notes.dao.IOptionDao;
import com.douwe.notes.dao.IUniteEnseignementDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cycle;
import com.douwe.notes.entities.Departement;
import com.douwe.notes.entities.Etudiant;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Semestre;
import com.douwe.notes.entities.Session;
import com.douwe.notes.entities.UniteEnseignement;
import com.douwe.notes.projection.EtudiantCycle;
import com.douwe.notes.projection.EtudiantNiveau;
import com.douwe.notes.projection.MoyenneUniteEnseignement;
import com.douwe.notes.projection.RelevetEtudiantNotesInfos;
import com.douwe.notes.projection.UEnseignementCredit;
import com.douwe.notes.service.INoteService;
import com.douwe.notes.service.ServiceException;
import com.douwe.notes.service.document.ISyntheseDiplomationDocument;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
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
 * @author cellule
 */
@Stateless
public class SyntheseDiplomationDocument implements ISyntheseDiplomationDocument {

    MessageHelper msgHelper = new MessageHelper();

    @Inject
    private ICycleDao cycleDao;

    @Inject
    private IDepartementDao departementDao;

    @Inject
    private IAnneeAcademiqueDao anneeAcademiqueDao;

    @Inject
    private INiveauDao niveauDao;

    @Inject
    private IOptionDao optionDao;

    @Inject
    private IEtudiantDao etudiantDao;

    @Inject
    private IUniteEnseignementDao uniteEnseignementDao;

    @EJB
    private INoteService noteService;

    @Override
    public void produireSyntheseDiplomation(long cycleId, long departementId, long anneeId, OutputStream output) {
        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, output);
            //writer.setPageEvent(new ReleveDecorator());
            doc.setPageSize(PageSize.A4.rotate());
            doc.setMargins(24, 24, 24, 24);
            Cycle cycle = cycleDao.findById(cycleId);
            Departement departement = departementDao.findById(departementId);
            AnneeAcademique annee = anneeAcademiqueDao.findById(anneeId);
            // Recupérer le niveau terminal du cycle en question
            Niveau n = niveauDao.findNiveauTerminalParCycle(cycle);
            doc.open();
            List<Option> options = optionDao.findByDepartementNiveau(departement, n);
            boolean isFirst = true;
            for (Option option : options) {
                if (!isFirst) {
                    doc.newPage();
                }
                produireSyntheseDiplomationOption(cycle, departement, n, option, annee, doc);
                isFirst = false;
            }
            doc.close();
        } catch (DocumentException | DataAccessException ex) {
            Logger.getLogger(SyntheseDiplomationDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void produireSyntheseDiplomationOption(Cycle cycle, Departement departement, Niveau niveau, Option option, AnneeAcademique annee, Document doc) {
        try {
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            Font bf = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            // Définition de l'entete du document
            // Définition de l'entete du document
            StringBuilder builder = new StringBuilder("République du Cameroun\n");
            builder.append("Paix -- Travail -- Patrie\n");
            builder.append("****\n");
            builder.append("Ministère de l'Enseignement Supérieur\n");
            builder.append("****\n");
            builder.append("Université de Maroua\n");
            builder.append("****\n");
            if (annee.getNumeroDebut() < 2017) {
                builder.append("Institut Supérieur du Sahel");
            } else {
                builder.append("École Nationale Supérieure Polytechnique de Maroua");
            }

            Paragraph frecnch = new Paragraph(new Phrase(builder.toString(), bf12));
            frecnch.setAlignment(Element.ALIGN_CENTER);
            builder = new StringBuilder();
            builder.append("Republic of Cameroon\n");
            builder.append("Peace -- Work -- Fatherland\n");
            builder.append("****\n");
            builder.append("The Ministry of Higher Education\n");
            builder.append("****\n");
            builder.append("The University of Maroua\n");
            builder.append("****\n");
            if (annee.getNumeroDebut() < 2017) {
                builder.append("The Higher Institute of the Sahel");
            } else {
                builder.append("National Advanced School of Engineering of Maroua");
            }

            Paragraph eng = new Paragraph(new Phrase(builder.toString(), bf12));
            eng.setAlignment(Element.ALIGN_CENTER);
            builder = new StringBuilder();
            builder.append(msgHelper.getProperty("header.adress"));

            builder.append(msgHelper.getProperty("header.tel"));
            builder.append(msgHelper.getProperty("header.fax"));
            /*builder.append(msgHelper.getProperty("header.mail"));
            builder.append(msgHelper.getProperty("header.site"));*/
            if (annee.getNumeroDebut() < 2017) {
                builder.append(msgHelper.getProperty("header.mail"));
                builder.append(msgHelper.getProperty("header.site"));
            } else {
                builder.append(msgHelper.getProperty("header.mail2"));
                builder.append(msgHelper.getProperty("header.site2"));
            }
            Paragraph coordonnees = new Paragraph(new Phrase(builder.toString(), bf12));
            coordonnees.setAlignment(Element.ALIGN_CENTER);
            float widths2[] = {3, 4, 3};
            PdfPTable header = new PdfPTable(widths2);
            header.setWidthPercentage(100);
            PdfPCell cell1;
            cell1 = new PdfPCell(frecnch);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.addCell(cell1);
            URL url = new ClassPathResource("logo4.png").getURL();
            //java.awt.Image img = ImageIO.read(new File("logo4.png"));
            //Image logo = Image.getInstance(img, null);
            Image logo = Image.getInstance(url);
            logo.scalePercent(60f);
            Paragraph p = new Paragraph();
            p.setAlignment(Element.ALIGN_CENTER);
            p.add(new Paragraph(new Chunk(logo, 0, 0, true)));
            Paragraph pp = new Paragraph(coordonnees);
            p.add(pp);
            PdfPCell cel = new PdfPCell(p);
            cel.setBorderColor(BaseColor.WHITE);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setVerticalAlignment(Element.ALIGN_CENTER);

            header.addCell(cel);
            cell1 = new PdfPCell(eng);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBorderColor(BaseColor.WHITE);
            header.addCell(cell1);
            doc.add(header);
            // Ajout des informations concernant l'entête
            Font fontHeader = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font fontHeaderTable = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            int nombreAnnee = cycle.getNiveaux().size();
            StringBuilder build = new StringBuilder(cycle.getDiplomeFr());
            build.append(" en ");
            String parcours = departement.getFrenchDescription();
            parcours = parcours.replace("Département des", "").replaceAll("Département de", "").replaceAll("Département d'", "");
            build.append(parcours);
            build.append(" / Promotion ");
            int an = annee.getNumeroDebut() + 1;
            build.append(an - nombreAnnee);
            build.append(" - ");
            build.append(an);
            Paragraph par = new Paragraph(build.toString(), fontHeader);
            par.setAlignment(Element.ALIGN_CENTER);
            par.setSpacingBefore(10f);
            doc.add(par);
            par = new Paragraph("PROCÈS VERBAL DES DÉLIBÉRATIONS D'ADMISSION DÉFINITIVE", fontHeader);
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);
            par = new Paragraph("Session de Septembre " + an, fontHeader);
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);
            int taille = 7 + nombreAnnee * 2;
            int i;
            float[] widths = new float[taille];
            widths[0] = 2;
            widths[1] = 14;
            widths[2] = 3.5f;
            for (i = 0; i < nombreAnnee * 2; i++) {
                widths[3 + i] = 3.2f;
            }
            widths[3 + i++] = 3;
            widths[3 + i++] = 3;
            widths[3 + i++] = 3;
            widths[3 + i++] = 10;
            PdfPTable ptable = new PdfPTable(widths);
            ptable.setWidthPercentage(98);
            PdfPCell cell = new PdfPCell(new Phrase("PARCOURS: " + parcours.toUpperCase(), fontHeaderTable));
            cell.setColspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            List<Niveau> niveaux = cycle.getNiveaux();
            for (Niveau n : niveaux) {
                cell = new PdfPCell(new Phrase("Crédits " + n.getCode(), fontHeaderTable));
                cell.setRowspan(3);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5f);
                ptable.addCell(cell);
                cell = new PdfPCell(new Phrase("MGP " + n.getCode(), fontHeaderTable));
                cell.setRowspan(3);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5f);
                ptable.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Crédits cumulés", fontHeaderTable));
            cell.setRowspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("MGPA du cycle", fontHeaderTable));
            cell.setRowspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("Décision", fontHeaderTable));
            cell.setRowspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("Mention", fontHeaderTable));
            cell.setRowspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("Option: " + option.getDescription().toUpperCase(), fontHeaderTable));
            cell.setColspan(3);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("No", fontHeaderTable));
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("Nom(s) et prenom", fontHeaderTable));
            cell.setPadding(5f);
            ptable.addCell(cell);
            cell = new PdfPCell(new Phrase("Matricule", fontHeaderTable));
            cell.setPadding(5f);
            ptable.addCell(cell);
            // Il faut recupérer la liste des étudiants inscrits au niveau en question
            List<Etudiant> etudiants = etudiantDao.listeEtudiantParDepartementEtNiveau(departement, annee, niveau, option);
            int number = 1;
            for (Etudiant etudiant : etudiants) {
                cell = new PdfPCell(new Phrase(String.valueOf(number++), bf12));
                cell.setPadding(4f);
                ptable.addCell(cell);
                cell = new PdfPCell(new Phrase(etudiant.getNom().toUpperCase(), bf12));
                cell.setPadding(4f);
                ptable.addCell(cell);
                cell = new PdfPCell(new Phrase(etudiant.getMatricule().toUpperCase(), bf12));
                cell.setPadding(4f);
                ptable.addCell(cell);
                int status = 0;
                int nombreCredit = 0;
                EtudiantCycle etudiantCycle = noteService.calculerPerformanceCycle(etudiant.getMatricule(), cycle.getId(), annee.getId());
                for (Niveau niv : niveaux) {
                    //EtudiantNiveau eNiveau = noteService.calculerPerformanceNiveau(etudiant.getMatricule(), niv.getId(), annee.getId());
                    boolean eNiveau = etudiantCycle.getCreditNiveau(niv.getCode()) != null;
                    if (eNiveau) {
                        // I need to work hard here or maybe before
                        if (status != 0) {
                            cell = new PdfPCell(new Phrase("Admis au " + niv.getCode().toLowerCase(), bf));
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell.setColspan(status);
                            cell.setPadding(4f);
                            ptable.addCell(cell);
                        }
                        nombreCredit += etudiantCycle.getCreditNiveau(niv.getCode());
                        Double mgp = etudiantCycle.getMGPNiveau(niv.getCode());
                        cell = new PdfPCell(new Phrase(String.valueOf(etudiantCycle.getCreditNiveau(niv.getCode())), bf12));
                        cell.setPadding(4f);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        ptable.addCell(cell);
                        cell = new PdfPCell(new Phrase((mgp == null) ? " " : String.format("%.2f", mgp), bf12));
                        cell.setPadding(4f);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        ptable.addCell(cell);
                    } else {
                        status += 2;
                    }
                }
                cell = new PdfPCell(new Phrase(String.valueOf(nombreCredit), bf12));
                cell.setPadding(4f);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                ptable.addCell(cell);
                Double val = etudiantCycle.getMgp();
                cell = new PdfPCell(new Phrase(val == null ? " " : String.format("%.2f", val), bf12));
                cell.setPadding(4f);
                ptable.addCell(cell);
                if (val != null) {
                    cell = new PdfPCell(new Phrase("Admis", bf12));
                } else {
                    cell = new PdfPCell(new Phrase("Refusé", bf12));
                }
                cell.setPadding(4f);
                ptable.addCell(cell);
                if (val != null) {
                    cell = new PdfPCell(new Phrase(DocumentUtil.transformMoyenneMgpToMentionRelevet(val), bf12));
                } else {
                    cell = new PdfPCell(new Phrase("", bf12));
                }
                cell.setPadding(4f);
                ptable.addCell(cell);

            }
            ptable.setSpacingBefore(10f);
            doc.add(ptable);
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(95);
            cell = DocumentUtil.createDefaultHeaderCell(msgHelper.getProperty("synthese.annuelleBody.membres"), bf);
            //cell = DocumentUtil.createDefaultHeaderCell(msgHelper.getProperty("synthese.annuelleBody.president"), bf);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBackgroundColor(BaseColor.WHITE);
            table.addCell(cell);
            cell = DocumentUtil.createDefaultHeaderCell(msgHelper.getProperty("synthese.annuelleBody.vicePresident"), bf);
            cell.setBorder(0);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = DocumentUtil.createDefaultHeaderCell(msgHelper.getProperty("synthese.annuelleBody.president"), bf);
            cell.setBorder(0);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            table.setSpacingBefore(10f);
            //table.setSpacingAfter(25f);
            table.setSpacingAfter(5f);

            doc.add(table);
        } catch (IOException | DocumentException | DataAccessException | ServiceException ex) {
            Logger.getLogger(SyntheseDiplomationDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
