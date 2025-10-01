package Test;

import modele.ModeleQRCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ModeleQRCodeTest {

    private ModeleQRCode modele;
    private final String pdfFilePath = "QRCodePerso.pdf";

    @BeforeEach
    public void setUp() {
        modele = new ModeleQRCode();
    }

    @AfterEach
    public void tearDown() {
        // supprime le fichier créé à la fin du test
        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) pdfFile.delete();
    }

    @Test
    public void testGenererQRCodePDF() throws Exception {
        String data = "https://www.example.com";
        String fontPath = null; // Utilise la police par défaut
        int fontSize = 14;
        Color color = Color.BLACK;
        boolean includeQr = true;
        String dest = pdfFilePath;

        // Crée le fichier PDF avec QR code
        modele.genererQRCodePDF(data, fontPath, fontSize, color, includeQr, dest);

        // Vérifie que le fichier PDF a été créé et n'est pas vide
        File pdfFile = new File(pdfFilePath);
        assertTrue(pdfFile.exists(), "PDF file should exist");
        assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
    }

    @Test
    public void testGenererQRCodePDFSansQR() throws Exception {
        String data = "Test sans QR code";
        String fontPath = null;
        int fontSize = 16;
        Color color = Color.BLUE;
        boolean includeQr = false; // Pas de QR code
        String dest = pdfFilePath;

        // Crée le fichier PDF sans QR code
        modele.genererQRCodePDF(data, fontPath, fontSize, color, includeQr, dest);

        // Vérifie que le fichier PDF a été créé et n'est pas vide
        File pdfFile = new File(pdfFilePath);
        assertTrue(pdfFile.exists(), "PDF file should exist");
        assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
    }

    @Test
    public void testGenererQRCodePDFDonneesVides() {
        String data = "";
        String fontPath = null;
        int fontSize = 14;
        Color color = Color.BLACK;
        boolean includeQr = true;
        String dest = pdfFilePath;

        // Vérifie qu'une exception est levée pour des données vides
        Exception exception = assertThrows(Exception.class, () -> {
            modele.genererQRCodePDF(data, fontPath, fontSize, color, includeQr, dest);
        });

        assertNotNull(exception, "Exception should be thrown for empty data");
    }
}

