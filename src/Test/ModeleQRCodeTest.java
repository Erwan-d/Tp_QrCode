package Test;

import modele.ModeleQRCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ModeleQRCodeTest {

    private ModeleQRCode modele;
    private final String qrFilePath = "qr.png";
    private final String pdfFilePath = "QrCodeResult.pdf";

    @BeforeEach
    public void setUp() {
        modele = new ModeleQRCode();
    }

    @AfterEach
    public void tearDown() {
        // Clean up generated files after each test
        File qrFile = new File(qrFilePath);
        if (qrFile.exists()) qrFile.delete();

        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) pdfFile.delete();
    }

    @Test
    public void testGenererQRCodePDF() throws Exception {
        String data = "https://www.example.com";

        // Run the QR code PDF generation method
        modele.genererQRCodePDF(data);

        // Verify that the QR code PNG file was created and is not empty
        File qrFile = new File(qrFilePath);
        assertTrue(qrFile.exists(), "QR code image file should exist");
        assertTrue(qrFile.length() > 0, "QR code image file should not be empty");

        // Verify that the PDF file was created and is not empty
        File pdfFile = new File(pdfFilePath);
        assertTrue(pdfFile.exists(), "PDF file should exist");
        assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
    }
}

