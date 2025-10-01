package controleur;

import modele.ModeleQRCode;
import vue.VueQRCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ControleurQRCode {
    private ModeleQRCode modele;
    private VueQRCode vue;

    private String fontPath = null;
    private Color chosenColor = Color.BLACK;

    public ControleurQRCode(ModeleQRCode modele, VueQRCode vue) {
        this.modele = modele;
        this.vue = vue;

        // --- Choisir police ---
        this.vue.chooseFontButton.addActionListener((ActionEvent e) -> {
            // Use the hybrid approach: let user choose between methods
            Object[] options = {"Parcourir fichiers TTF", "Choisir police système", "Annuler"};
            int choice = JOptionPane.showOptionDialog(
                vue,
                "Comment voulez-vous sélectionner la police?",
                "Méthode de sélection de police",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice == 0) {
                // File browser method
                selectFontFromFile();
            } else if (choice == 1) {
                // System font method
                selectSystemFont();
            }
            // If choice == 2 (Annuler) or closed dialog, do nothing
        });

        // --- Choisir couleur ---
        this.vue.chooseColorButton.addActionListener((ActionEvent e) -> {
            Color c = JColorChooser.showDialog(vue, "Choisir une couleur pour le texte", chosenColor);
            if (c != null) {
                chosenColor = c;
                vue.colorPreview.setBackground(c);
                vue.previewLabel.setForeground(c); // MAJ couleur du texte exemple
                vue.statusLabel.setText("✓ Couleur sélectionnée");
            }
        });

        // --- Générer PDF ---
        this.vue.generateButton.addActionListener((ActionEvent e) -> {
            String data = vue.inputField.getText();
            if (data == null || data.trim().isEmpty()) {
                vue.statusLabel.setText("⚠ Entrez un texte ou un lien !");
                return;
            }

            int fontSize = (int) vue.fontSizeCombo.getSelectedItem();
            boolean includeQr = vue.includeQrCheckBox.isSelected();
            String dest = "QRCodePerso.pdf";

            try {
                modele.genererQRCodePDF(data, fontPath, fontSize, chosenColor, includeQr, dest);
                vue.statusLabel.setText("✅ PDF généré : " + dest);
                
                // Ask if user wants to open the file
                int response = JOptionPane.showConfirmDialog(vue, 
                    "PDF généré avec succès! Voulez-vous ouvrir le fichier?", 
                    "Succès", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(dest));
                }
            } catch (Exception ex) {
                vue.statusLabel.setText("❌ Erreur : " + ex.getMessage());
                ex.printStackTrace();
                
                // Show detailed error in dialog for debugging
                JOptionPane.showMessageDialog(vue, 
                    "Erreur détaillée: " + ex.getMessage(), 
                    "Erreur de génération", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update preview when font size changes
        this.vue.fontSizeCombo.addActionListener((ActionEvent e) -> {
            updateFontPreview();
        });
    }

    /**
     * Get the current preview font size based on selected size
     */
    private float getPreviewFontSize() {
        int selectedSize = (int) vue.fontSizeCombo.getSelectedItem();
        // Use a reasonable preview size, but scale it if the selected size is very large
        return Math.min(selectedSize, 24);
    }

    /**
     * Update the font preview with current settings
     */
    private void updateFontPreview() {
        Font currentFont = vue.previewLabel.getFont();
        Font newFont = currentFont.deriveFont(getPreviewFontSize());
        vue.previewLabel.setFont(newFont);
    }

    /**
     * Method to select font from file system
     */
    private void selectFontFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une police (.ttf)");
        
        // Set to user home directory instead of Windows Fonts
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        // Create a filter to show only .ttf files
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".ttf") || name.endsWith(".otf");
            }
            
            @Override
            public String getDescription() {
                return "Fichiers de police (*.ttf, *.otf)";
            }
        });
        
        int res = chooser.showOpenDialog(vue);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            fontPath = f.getAbsolutePath();
            vue.fontPathLabel.setText("Police fichier : " + f.getName());

            try {
                // Charger la police pour l'aperçu
                Font awtFont = Font.createFont(Font.TRUETYPE_FONT, f);
                awtFont = awtFont.deriveFont(Font.PLAIN, getPreviewFontSize());
                vue.previewLabel.setFont(awtFont);
                vue.statusLabel.setText("✓ Police chargée : " + f.getName());
                updateFontPreview();
            } catch (Exception ex) {
                vue.statusLabel.setText("⚠ Impossible de charger la police.");
                JOptionPane.showMessageDialog(vue, 
                    "Impossible de charger la police: " + ex.getMessage(), 
                    "Erreur de police", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Method to select from system fonts
     */
    private void selectSystemFont() {
        // Get all available fonts from the system
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        
        // Sort font names alphabetically for easier selection
        java.util.Arrays.sort(fontNames);
        
        // Create a dialog to select from available fonts
        String selectedFont = (String) JOptionPane.showInputDialog(
            vue,
            "Choisissez une police système:",
            "Sélection de police système",
            JOptionPane.QUESTION_MESSAGE,
            null,
            fontNames,
            "Arial"
        );
        
        if (selectedFont != null && !selectedFont.trim().isEmpty()) {
            // For system fonts, we store the font name
            fontPath = selectedFont;
            vue.fontPathLabel.setText("Police système : " + selectedFont);
            
            try {
                // Apply the selected system font to preview - FIXED CONSTRUCTOR
                Font awtFont = new Font(selectedFont, Font.PLAIN, (int) getPreviewFontSize());
                vue.previewLabel.setFont(awtFont);
                vue.statusLabel.setText("✓ Police système appliquée : " + selectedFont);
                updateFontPreview();
            } catch (Exception ex) {
                vue.statusLabel.setText("⚠ Impossible d'appliquer la police système.");
                JOptionPane.showMessageDialog(vue, 
                    "Impossible d'appliquer la police système: " + ex.getMessage(), 
                    "Erreur de police", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

