package modele;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class ModeleQRCode {

    /**
     * Génère un PDF contenant du texte personnalisé et éventuellement un QR code
     * @param data texte ou lien à encoder
     * @param fontPath chemin d’un fichier TTF (null ou vide = Helvetica)
     * @param fontSize taille du texte
     * @param color couleur du texte (java.awt.Color)
     * @param includeQr inclure ou non le QR dans le PDF
     * @param dest fichier PDF de sortie
     */
    public void genererQRCodePDF(String data,
                                 String fontPath,
                                 int fontSize,
                                 Color color,
                                 boolean includeQr,
                                 String dest) throws Exception {

        // --- Définir police ---
        Font customFont;
        if (fontPath != null && !fontPath.trim().isEmpty()) {
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            customFont = new Font(bf, fontSize, Font.NORMAL,
                    new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
        } else {
            customFont = new Font(Font.FontFamily.HELVETICA, fontSize, Font.NORMAL,
                    new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
        }

        // --- Créer PDF ---
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        // Ajouter le texte avec style
        Paragraph p = new Paragraph("Informations : " + data, customFont);
        document.add(p);

        // Ajouter QR code si demandé
        if (includeQr) {
            BufferedImage qrImage = generateQrBufferedImage(data, 200, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] qrBytes = baos.toByteArray();

            Image qr = Image.getInstance(qrBytes);
            qr.scaleToFit(200, 200);
            document.add(qr);
        }

        document.close();
    }

    // --- Génération QR code en mémoire ---
    private BufferedImage generateQrBufferedImage(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}


