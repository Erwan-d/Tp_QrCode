package modele;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class ModeleQRCode {

    public void genererQRCodePDF(String texteAffichage, String contenuQR,
                                 String fontPath, int fontSize, Color color,
                                 boolean includeQr, String imagePath,
                                 int imageWidth, int imageHeight, 
                                 String imagePosition, String dest) throws Exception {

        String dataQR = (contenuQR != null && !contenuQR.trim().isEmpty()) ? contenuQR : texteAffichage;

        Font customFont;
        BaseColor textColor = new BaseColor(color.getRed(), color.getGreen(), color.getBlue());
        
        if (fontPath != null && !fontPath.trim().isEmpty()) {
            try {
                File fontFile = new File(fontPath);
                if (fontFile.exists() && fontFile.isFile()) {
                    BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    customFont = new Font(bf, fontSize, Font.NORMAL, textColor);
                } else {
                    customFont = new Font(Font.FontFamily.HELVETICA, fontSize, Font.NORMAL, textColor);
                }
            } catch (Exception e) {
                customFont = new Font(Font.FontFamily.HELVETICA, fontSize, Font.NORMAL, textColor);
            }
        } else {
            customFont = new Font(Font.FontFamily.HELVETICA, fontSize, Font.NORMAL, textColor);
        }

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        Image pdfImage = null;
        if (imagePath != null && !imagePath.trim().isEmpty() && new File(imagePath).exists()) {
            try {
                pdfImage = Image.getInstance(imagePath);
                pdfImage.scaleToFit(imageWidth, imageHeight);
                pdfImage.setAlignment(Image.ALIGN_CENTER);
            } catch (Exception e) {
                System.err.println("Erreur image: " + e.getMessage());
            }
        }

        if (pdfImage != null && "Haut".equals(imagePosition)) {
            document.add(pdfImage);
            document.add(Chunk.NEWLINE);
        }

        Paragraph p = new Paragraph("Informations : " + texteAffichage, customFont);
        document.add(p);

        if (includeQr) {
            document.add(Chunk.NEWLINE);
            BufferedImage qrImage = generateQrBufferedImage(dataQR, 200, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] qrBytes = baos.toByteArray();

            Image qr = Image.getInstance(qrBytes);
            qr.scaleToFit(200, 200);
            document.add(qr);
        }

        if (pdfImage != null && "Après texte".equals(imagePosition)) {
            document.add(Chunk.NEWLINE);
            document.add(pdfImage);
        }

        if (pdfImage != null && "Bas".equals(imagePosition)) {
            document.add(Chunk.NEWLINE);
            document.add(pdfImage);
        }

        document.close();
    }

    private BufferedImage generateQrBufferedImage(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}


