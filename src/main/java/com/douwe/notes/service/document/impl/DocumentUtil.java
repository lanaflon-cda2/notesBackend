package com.douwe.notes.service.document.impl;

import com.douwe.notes.config.MessageHelper;
import com.douwe.notes.entities.Session;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

/**
 *
 * @author Kenfack Valmy-Roi <roykenvalmy@gmail.com>
 */
public class DocumentUtil {

    private static final MessageHelper MSGHELPER = new MessageHelper();

    public static String transformMoyenneMgpToGradeRelevet(double moyenne) {
        /*if (Double.compare(moyenne, 4.0) == 0) {
            return "A+";
        }*/
        if (moyenne <= 4 && moyenne >= 3.6) {
            return "A+";
        }
        if (moyenne < 3.6 && moyenne >= 3.2) {
            return "A-";
        }
        if (moyenne < 3.2 && moyenne >= 2.8) {
            return "B";
        }
        if (moyenne < 2.8 && moyenne >= 2.4) {
            return "B-";
        }
        if (moyenne < 2.4 && moyenne >= 2.0) {
            return "C";
        }
        if (moyenne < 2.0 && moyenne >= 1.3) {
            return "C-";
        }
        if (moyenne < 1.3 && moyenne >= 1.2) {
            return "D";
        }
        if (moyenne < 1.2 && moyenne >= 1.0) {
            return "E";
        }
        return "F";
    }

    public static String transformMoyenneMgpToMentionRelevet(double moyenne) {
        /*if (Double.compare(moyenne, 4.0) == 0) {
            return "Excellent";
        }*/
        if (moyenne <= 4 && moyenne >= 3.6) {

            return MSGHELPER.getProperty("util.excellent");
        }

        if (moyenne < 3.6 && moyenne >= 3.2) {
            return MSGHELPER.getProperty("util.tresBien");
        }

        if (moyenne < 3.2 && moyenne >= 2.8) {
            return MSGHELPER.getProperty("util.bien");
        }

        if (moyenne < 2.8 && moyenne >= 2.4) {
            return MSGHELPER.getProperty("util.assezBien");
        }

        if (moyenne < 2.4 && moyenne >= 2) {
            return MSGHELPER.getProperty("util.passable");
        }
        return MSGHELPER.getProperty("util.echoue");
    }

    public static String transformNoteGradeUE(double note) {
        if (note <= 20 && note >= 18) {
            return "A+";
        }
        if (note < 18 && note >= 16) {
            return "A";
        }

        if (note < 16 && note >= 14) {
            return "B+";
        }
        if (note < 14 && note >= 13) {
            return "B";
        }
        if (note < 13 && note >= 12) {
            return "B-";
        }
        if (note < 12 && note >= 11) {
            return "C+";
        }
        if (note < 11 && note >= 10) {
            return "C";
        }
        if (note < 10 && note >= 9) {
            return "C-";
        }
        if (note < 9 && note >= 8) {
            return "D";
        }
        if (note < 8 && note >= 6) {
            return "E";
        }

        return "F";
    }

    public static double transformNoteMgpUE(double note) {
        /*Systeme anglophone : la note / 100 */
        if (note <= 20 && note >= 18) {
            return 4;
        }
        if (note >= 16) {
            return 3.7;
        }
        if (note >= 14) {
            return 3.3;
        }
        if (note >= 13) {
            return 3.0;
        }
        if (note >= 12) {
            return 2.7;
        }
        if (note >= 11) {
            return 2.3;
        }
        if (note >= 10) {
            return 2.0;
        }
        if (note >= 9) {
            return 1.7;
        }
        if (note >= 8) {
            return 1.3;
        }
        if (note >= 6) {
            return 1.0;
        }
        return 0;
    }

    public static String transformNoteMention(double note) {
        if (note <= 20 && note >= 16) {

            return MSGHELPER.getProperty("util.mention.tresBien");
        }
        if (note < 16 && note >= 14) {
            return MSGHELPER.getProperty("util.mention.bien");
        }
        if (note < 14 && note >= 12) {
            return MSGHELPER.getProperty("util.mention.assezBien");
        }
        if (note < 12 && note >= 10) {
            return MSGHELPER.getProperty("util.mention.passable");
        }
        if (note < 10 && note >= 9) {
            return MSGHELPER.getProperty("util.mention.insuffisant");
        }
        if (note < 9 && note >= 8) {
            return MSGHELPER.getProperty("util.mention.faible");
        }
        if (note < 8 && note >= 6) {
            return MSGHELPER.getProperty("util.mention.tresFaible");
        }

        return MSGHELPER.getProperty("util.mention.nul");

    }

    public static String sessionToString(Session session) {
        if (session == Session.normale) {
            return "1";
        } else {
            return "2";
        }
    }

    public static PdfPCell createDefaultHeaderCell(String message, Font bf) {
        PdfPCell cell = new PdfPCell(new Phrase(message, bf));
        cell.setBackgroundColor(new BaseColor(230, 230, 230));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(4f);
        cell.setPaddingTop(5f);
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(BaseColor.BLACK);
        return cell;
    }

    public static PdfPCell createSyntheseDefaultHeaderCell(String message, Font bf, boolean color) {
        PdfPCell cell = new PdfPCell(new Phrase(message, bf));
        if (color) {
            cell.setBackgroundColor(new BaseColor(230, 230, 230));
        }
        // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(4f);
        cell.setPaddingTop(5f);
        cell.setBorderWidth(0.01f);

        cell.setBorderColor(BaseColor.BLACK);
        if (message.length() > 50) {
            cell.setFixedHeight(215f);
        }
        cell.setRotation(90);
        return cell;
    }

    public static PdfPCell createDefaultBodyCell(String message, Font bf, boolean color) {
        PdfPCell cell = new PdfPCell(new Phrase(message, bf));
        if (color) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(4f);
        cell.setPaddingTop(5f);
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(BaseColor.BLACK);
        return cell;
    }

    public static PdfPCell createEmptyCell() {
        PdfPCell cell = new PdfPCell(new Phrase(" "));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(2f);
        cell.setPaddingTop(2f);
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(BaseColor.BLACK);
        return cell;
    }

    public static PdfPCell createRelevetFootBodyCell(String message, Font bf, boolean border, int rowspan, int colspan) {
        Chunk ch = new Chunk(message, bf);
        ch.setGenericTag("shadow");
        PdfPCell cell = new PdfPCell(new Phrase(ch));
        /*if (border) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        }
        cell.setRowspan(rowspan);
        cell.setColspan(colspan);*/
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(4f);
        cell.setPaddingTop(4f);
        cell.setBorderWidth(0);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }

    public static PdfPCell createSyntheseDefaultBodyCell(String message, Font bf, boolean color, boolean isCentered, float paddingTop, float paddingBotton) {
        PdfPCell cell = new PdfPCell(new Phrase(message, bf));
        if (color) {
            cell.setBackgroundColor(new BaseColor(230, 230, 230));
        }
        if (isCentered) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        } else {
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(3f);
        }
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingBottom(paddingBotton);
        cell.setPaddingTop(paddingTop);
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(BaseColor.BLACK);
        //cell.setTop(2f);
        return cell;
    }

    public static PdfPCell createSyntheseDefaultBodyCell(String message, Font bf, boolean color, boolean isCentered) {
        return createSyntheseDefaultBodyCell(message, bf, color, isCentered, 4f, 4f);
    }
}
