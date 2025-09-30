package modele;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ModeleQRCode {

    public void genererQRCodePDF(String data) throws Exception {
        String qrFile = "qr.png";
        generateQRCodeImage(data, qrFile);
        generatePDFWithQRCode(qrFile, data);
    }

    private void generateQRCodeImage(String data, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(filePath));
    }

    private void generatePDFWithQRCode(String qrImagePath, String data) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("QrCodeResult.pdf"));
        document.open();
        document.add(new Paragraph("Informations : " + data));
        Image qr = Image.getInstance(qrImagePath);
        document.add(qr);
        document.close();
    }
}


