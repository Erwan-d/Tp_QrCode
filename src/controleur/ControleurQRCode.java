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
    private String imagePath = null;

    public ControleurQRCode(ModeleQRCode modele, VueQRCode vue) {
        this.modele = modele;
        this.vue = vue;

        this.vue.chooseFontButton.addActionListener((ActionEvent e) -> {
            selectFontFromFile();
        });

        this.vue.chooseColorButton.addActionListener((ActionEvent e) -> {
            Color c = JColorChooser.showDialog(vue, "Choisir une couleur pour le texte", chosenColor);
            if (c != null) {
                chosenColor = c;
                vue.colorPreview.setBackground(c);
                vue.previewLabel.setForeground(c);
                vue.statusLabel.setText("✓ Couleur sélectionnée");
            }
        });

        this.vue.chooseImageButton.addActionListener((ActionEvent e) -> {
            selectImage();
        });

        this.vue.generateButton.addActionListener((ActionEvent e) -> {
            String texteAffichage = vue.inputField.getText();
            String contenuQR = vue.qrContentField.getText();
            
            if ((texteAffichage == null || texteAffichage.trim().isEmpty()) && 
                (contenuQR == null || contenuQR.trim().isEmpty())) {
                vue.statusLabel.setText("⚠ Entrez un texte ou un lien !");
                return;
            }

            if (texteAffichage == null || texteAffichage.trim().isEmpty()) {
                texteAffichage = contenuQR;
            }
            
            if (contenuQR == null || contenuQR.trim().isEmpty()) {
                contenuQR = texteAffichage;
            }

            int fontSize = (int) vue.fontSizeCombo.getSelectedItem();
            boolean includeQr = vue.includeQrCheckBox.isSelected();
            int imageWidth = (int) vue.imageWidthCombo.getSelectedItem();
            int imageHeight = (int) vue.imageHeightCombo.getSelectedItem();
            String imagePosition = (String) vue.imagePositionCombo.getSelectedItem();
            String dest = "QRCodePerso.pdf";

            try {
                modele.genererQRCodePDF(texteAffichage, contenuQR, fontPath, fontSize, chosenColor, 
                                      includeQr, imagePath, imageWidth, imageHeight, imagePosition, dest);
                vue.statusLabel.setText("✅ PDF généré : " + dest);
                
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
                JOptionPane.showMessageDialog(vue, 
                    "Erreur détaillée: " + ex.getMessage(), 
                    "Erreur de génération", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        this.vue.fontSizeCombo.addActionListener((ActionEvent e) -> {
            updateFontPreview();
        });
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une image");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                       name.endsWith(".png") || name.endsWith(".gif") ||
                       name.endsWith(".bmp");
            }
            
            @Override
            public String getDescription() {
                return "Images (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
            }
        });
        
        int res = chooser.showOpenDialog(vue);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            imagePath = f.getAbsolutePath();
            vue.imagePathLabel.setText("Image : " + f.getName());
            vue.statusLabel.setText("✓ Image sélectionnée : " + f.getName());
        }
    }

    private float getPreviewFontSize() {
        int selectedSize = (int) vue.fontSizeCombo.getSelectedItem();
        return Math.min(selectedSize, 24);
    }

    private void updateFontPreview() {
        Font currentFont = vue.previewLabel.getFont();
        Font newFont = currentFont.deriveFont(getPreviewFontSize());
        vue.previewLabel.setFont(newFont);
    }

    private void selectFontFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une police TTF");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
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
            vue.fontPathLabel.setText("Police : " + f.getName());

            try {
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
}

