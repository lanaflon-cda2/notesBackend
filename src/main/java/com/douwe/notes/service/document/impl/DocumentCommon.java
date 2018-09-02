package com.douwe.notes.service.document.impl;

import com.douwe.notes.config.MessageHelper;
import com.douwe.notes.dao.IEnseignantDao;
import com.douwe.notes.dao.IParcoursDao;
import com.douwe.notes.entities.AnneeAcademique;
import com.douwe.notes.entities.Cours;
import com.douwe.notes.entities.Credit;
import com.douwe.notes.entities.Enseignant;
import com.douwe.notes.entities.Niveau;
import com.douwe.notes.entities.Option;
import com.douwe.notes.entities.Parcours;
import com.douwe.notes.entities.Programme;
import com.douwe.notes.entities.Session;
import com.douwe.notes.projection.UEnseignementCredit;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.net.URL;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
@Named
@Service
public class DocumentCommon {

    @Inject
    private IParcoursDao parcoursDao;

    @Inject
    private IEnseignantDao enseignantDao;

    MessageHelper msgHelper = new MessageHelper();

    public IParcoursDao getParcoursDao() {
        return parcoursDao;
    }

    public void setParcoursDao(IParcoursDao parcoursDao) {
        this.parcoursDao = parcoursDao;
    }

    public IEnseignantDao getEnseignantDao() {
        return enseignantDao;
    }

    public void setEnseignantDao(IEnseignantDao enseignantDao) {
        this.enseignantDao = enseignantDao;
    }

    public void produceDocumentHeader(Document doc, Cours c, Niveau n, Option o, AnneeAcademique a, Session s, Programme prog, Credit credit, boolean departement) throws Exception {
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
        Font fontEntete = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        // Définition de l'entete du document
        StringBuilder builder = new StringBuilder("République du Cameroun\n");
        builder.append("****\n");
        builder.append("Paix -- Travail -- Patrie\n");
        builder.append("****\n");
        builder.append("Ministère de l'Enseignement Supérieur\n");
        builder.append("****\n");
        builder.append("Université de Maroua\n");
        builder.append("****\n");
        if (a.getNumeroDebut() < 2017) {
            builder.append("Institut Supérieur du Sahel");
        } else {
            builder.append("École Nationale Supérieure Polytechnique de Maroua");
        }
        if (departement) {
            builder.append("\n****\n");
            builder.append(o.getDepartement().getFrenchDescription());
        }
        Paragraph frecnch = new Paragraph(new Phrase(builder.toString(), bf12));
        frecnch.setAlignment(Element.ALIGN_CENTER);
        builder = new StringBuilder();
        builder.append("Republic of Cameroon\n");
        builder.append("****\n");
        builder.append("Peace -- Work -- Fatherland\n");
        builder.append("****\n");
        builder.append("The Ministry of Higher Education\n");
        builder.append("****\n");
        builder.append("The University of Maroua\n");
        builder.append("****\n");
        if (a.getNumeroDebut() < 2017) {
            builder.append("The Higher Institute of the Sahel");
        } else {
            builder.append("National Advanced School of Engineering of Maroua");
        }
        if (departement) {
            builder.append("\n****\n");
            builder.append(o.getDepartement().getEnglishDescription());
        }
        Paragraph eng = new Paragraph(new Phrase(builder.toString(), bf12));
        eng.setAlignment(Element.ALIGN_CENTER);
        builder = new StringBuilder();
        builder.append(msgHelper.getProperty("header.adress"));
        if (departement) {
            builder.append(msgHelper.getProperty("header.tel"));
            builder.append(msgHelper.getProperty("header.fax"));
        }
        if (a.getNumeroDebut() < 2017) {
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
        /////////////////////////////
        if (!departement) {
            PdfPTable table2 = new PdfPTable(7);
            table2.setWidthPercentage(100);
            PdfPCell cell;
            Phrase phrase;
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.mention"), fontEntete));
            phrase.add(new Chunk(o.getDepartement().getFrenchDescription(), bf12));
            cell = new PdfPCell(phrase);
            cell.setColspan(3);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.titreUe"), fontEntete));
            phrase.add(new Chunk(c.getIntitule(), bf12));
            cell = new PdfPCell(phrase);
            cell.setColspan(4);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);

            //    cell = new PdfPCell(new Phrase("Parcours : " + head.getParcours(), bf12));
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.parcours"), fontEntete));
            phrase.add(new Chunk(o.getDepartement().getCode(), bf12));
            cell = new PdfPCell(phrase);

            cell.setColspan(3);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            // Il faut retrouver le code de l'UE
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.codeUe"), fontEntete));
            phrase.add(new Chunk(prog.getUniteEnseignement().getCode(), bf12));
            cell = new PdfPCell(phrase);
            cell.setColspan(2);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.option"), fontEntete));
            phrase.add(new Chunk(o.getCode(), bf12));
            cell = new PdfPCell(phrase);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.credit"), fontEntete));
            phrase.add(new Chunk(String.valueOf(credit.getValeur()), bf12));
            cell = new PdfPCell(phrase);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.niveau"), fontEntete));
            phrase.add(new Chunk(n.getCode(), bf12));
            phrase.add(new Chunk("  ", bf12));
            phrase.add(new Chunk(msgHelper.getProperty("header.annee"), fontEntete));
            phrase.add(new Chunk(a.toString(), bf12));
            cell = new PdfPCell(phrase);
            cell.setBorderColor(BaseColor.WHITE);
            cell.setColspan(3);
            table2.addCell(cell);
            // I faut retrouver le semestre du cours
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.semestre"), fontEntete));
            phrase.add(new Chunk(prog.getSemestre().getIntitule(), bf12));
            phrase.add(new Chunk("  ", fontEntete));
            phrase.add(new Chunk(msgHelper.getProperty("header.session"), fontEntete));
            phrase.add(new Chunk("" + (s.ordinal() + 1), bf12));
            cell = new PdfPCell(phrase);
            cell.setColspan(2);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);

            // il faut retrouver les enseignants du cours
            Parcours pa = parcoursDao.findByNiveauOption(n, o);
            List<Enseignant> enseignants = enseignantDao.findByCours(c, a, pa);
            String ens = "";
            for (Enseignant enseignant : enseignants) {
                ens += enseignant.getNom() + " / ";
            }
            if (ens.endsWith(" / ")) {
                ens = ens.substring(0, ens.length() - 3);
            }
            phrase = new Phrase();
            phrase.add(new Chunk(msgHelper.getProperty("header.enseignants"), fontEntete));
            phrase.add(new Chunk(ens, bf12));
            cell = new PdfPCell(phrase);
            cell.setColspan(2);
            cell.setBorderColor(BaseColor.WHITE);
            table2.addCell(cell);
            table2.setSpacingBefore(2f);
            //table2.setSpacingAfter(2f);
            doc.add(table2);

        }

    }

    public int computeTotalCredit(List<UEnseignementCredit> ues) {
        int result = 0;
        for (UEnseignementCredit ue : ues) {
            result += ue.getCredit();
        }
        return result;
    }

}
