package vue;

import javax.swing.*;
import java.awt.*;

public class VueQRCode extends JFrame {
    public JTextField inputField;
    public JButton generateButton;
    public JLabel statusLabel;

    public VueQRCode() {
        setTitle("Générateur de QR Code");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        inputField = new JTextField(30);
        generateButton = new JButton("Générer QR Code PDF");
        statusLabel = new JLabel("Entrez un texte ou un lien.");

        JPanel centerPanel = new JPanel();
        centerPanel.add(inputField);
        centerPanel.add(generateButton);

        add(centerPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
