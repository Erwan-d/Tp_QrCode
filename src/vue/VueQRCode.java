package vue;

import javax.swing.*;
import java.awt.*;

public class VueQRCode extends JFrame {
    public JTextField inputField;
    public JButton generateButton;
    public JLabel statusLabel;

    // Personnalisation
    public JButton chooseFontButton;
    public JLabel fontPathLabel;
    public JComboBox<Integer> fontSizeCombo;
    public JButton chooseColorButton;
    public JLabel colorPreview;
    public JCheckBox includeQrCheckBox;
    public JLabel previewLabel; // aperçu du texte

    public VueQRCode() {
        setTitle("Générateur de QR Code - Personnalisation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // --- Haut : saisie texte ---
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(new JLabel("Texte ou lien : "), BorderLayout.WEST);
        inputField = new JTextField(30);
        topPanel.add(inputField, BorderLayout.CENTER);

        // --- Centre : options de personnalisation ---
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(5, 2, 5, 5));

        // Police
        chooseFontButton = new JButton("Choisir police (.ttf)");
        fontPathLabel = new JLabel("Police par défaut : Helvetica");
        optionsPanel.add(chooseFontButton);
        optionsPanel.add(fontPathLabel);

        // Taille
        optionsPanel.add(new JLabel("Taille police :"));
        Integer[] sizes = {10, 12, 14, 16, 18, 20, 24, 28, 32};
        fontSizeCombo = new JComboBox<>(sizes);
        fontSizeCombo.setSelectedItem(14);
        optionsPanel.add(fontSizeCombo);

        // Couleur
        chooseColorButton = new JButton("Choisir couleur texte");
        colorPreview = new JLabel("    ");
        colorPreview.setOpaque(true);
        colorPreview.setBackground(Color.BLACK);
        optionsPanel.add(chooseColorButton);
        optionsPanel.add(colorPreview);

        // Inclure QR
        includeQrCheckBox = new JCheckBox("Inclure QR code dans le PDF", true);
        optionsPanel.add(new JLabel(""));
        optionsPanel.add(includeQrCheckBox);
        previewLabel = new JLabel("Exemple : ABC abc 123");
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        previewLabel.setForeground(Color.BLACK);
        optionsPanel.add(previewLabel);

        // --- Bas : bouton générer et statut ---
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        generateButton = new JButton("Générer QR Code PDF");
        bottomPanel.add(generateButton, BorderLayout.CENTER);

        statusLabel = new JLabel("Entrez un texte ou un lien.");
        statusLabel.setForeground(Color.BLUE);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        // --- Ajouter tout ---
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}


