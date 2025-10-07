package vue;

import javax.swing.*;
import java.awt.*;

public class VueQRCode extends JFrame {
    // --- Boutons de sauvegarde/chargement ---
    public JButton saveButton;
    public JButton loadButton;

    // --- Éléments principaux ---
    public JTextField inputField;
    public JTextField qrContentField;
    public JButton generateButton;
    public JLabel statusLabel;

    // --- Personnalisation du texte ---
    public JButton chooseFontButton;
    public JLabel fontPathLabel;
    public JComboBox<Integer> fontSizeCombo;
    public JButton chooseColorButton;
    public JLabel colorPreview;
    public JCheckBox includeQrCheckBox;
    public JLabel previewLabel;

    // --- Image ---
    public JButton chooseImageButton;
    public JLabel imagePathLabel;
    public JComboBox<String> imagePositionCombo;
    public JComboBox<Integer> imageWidthCombo;
    public JComboBox<Integer> imageHeightCombo;

    public VueQRCode() {
        setTitle("Générateur de QR Code - Personnalisation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // --- Zone texte et contenu QR ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.add(new JLabel("Texte à afficher : "), BorderLayout.WEST);
        inputField = new JTextField(30);
        textPanel.add(inputField, BorderLayout.CENTER);

        JPanel qrPanel = new JPanel(new BorderLayout(5, 5));
        qrPanel.add(new JLabel("Contenu QR code : "), BorderLayout.WEST);
        qrContentField = new JTextField(30);
        qrPanel.add(qrContentField, BorderLayout.CENTER);

        topPanel.add(textPanel);
        topPanel.add(qrPanel);

        // --- Options principales ---
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(8, 2, 5, 5));

        chooseFontButton = new JButton("Choisir police (.ttf)");
        fontPathLabel = new JLabel("Police par défaut : Helvetica");
        optionsPanel.add(chooseFontButton);
        optionsPanel.add(fontPathLabel);

        optionsPanel.add(new JLabel("Taille police :"));
        Integer[] sizes = {10, 12, 14, 16, 18, 20, 24, 28, 32};
        fontSizeCombo = new JComboBox<>(sizes);
        fontSizeCombo.setSelectedItem(14);
        optionsPanel.add(fontSizeCombo);

        chooseColorButton = new JButton("Choisir couleur texte");
        colorPreview = new JLabel("    ");
        colorPreview.setOpaque(true);
        colorPreview.setBackground(Color.BLACK);
        optionsPanel.add(chooseColorButton);
        optionsPanel.add(colorPreview);

        includeQrCheckBox = new JCheckBox("Inclure QR code dans le PDF", true);
        optionsPanel.add(new JLabel(""));
        optionsPanel.add(includeQrCheckBox);

        optionsPanel.add(new JLabel("Aperçu :"));
        previewLabel = new JLabel("Exemple : ABC abc 123");
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        previewLabel.setForeground(Color.BLACK);
        optionsPanel.add(previewLabel);

        chooseImageButton = new JButton("Choisir image");
        imagePathLabel = new JLabel("Aucune image sélectionnée");
        optionsPanel.add(chooseImageButton);
        optionsPanel.add(imagePathLabel);

        optionsPanel.add(new JLabel("Position image :"));
        String[] positions = {"Haut", "Bas", "Après texte"};
        imagePositionCombo = new JComboBox<>(positions);
        imagePositionCombo.setSelectedItem("Après texte");
        optionsPanel.add(imagePositionCombo);

        optionsPanel.add(new JLabel("Largeur image :"));
        Integer[] widths = {100, 150, 200, 250, 300};
        imageWidthCombo = new JComboBox<>(widths);
        imageWidthCombo.setSelectedItem(150);
        optionsPanel.add(imageWidthCombo);

        optionsPanel.add(new JLabel("Hauteur image :"));
        Integer[] heights = {100, 150, 200, 250, 300};
        imageHeightCombo = new JComboBox<>(heights);
        imageHeightCombo.setSelectedItem(150);
        optionsPanel.add(imageHeightCombo);

        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JPanel saveLoadPanel = new JPanel(new FlowLayout());
        saveButton = new JButton("💾 Sauvegarder projet");
        loadButton = new JButton("📂 Charger projet");
        saveLoadPanel.add(saveButton);
        saveLoadPanel.add(loadButton);

        bottomPanel.add(saveLoadPanel, BorderLayout.NORTH);

        generateButton = new JButton("Générer QR Code PDF");
        bottomPanel.add(generateButton, BorderLayout.CENTER);

        statusLabel = new JLabel("Entrez un texte ou un lien.");
        statusLabel.setForeground(Color.BLUE);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(optionsPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}


