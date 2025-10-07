package controleur;

import modele.ModeleQRCode;
import modele.Dataprojet;
import vue.VueQRCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class ControleurQRCode {
	public JButton saveButton;
	public JButton loadButton;
    private ModeleQRCode modele;
    private VueQRCode vue;
    private String fontPath = null;
    private Color chosenColor = Color.BLACK;
    private String imagePath = null;

    public ControleurQRCode(ModeleQRCode modele, VueQRCode vue) {
        this.modele = modele;
        this.vue = vue;

        // --- Sélection police ---
        this.vue.chooseFontButton.addActionListener((ActionEvent e) -> selectFontFromFile());

        // --- Sélection couleur ---
        this.vue.chooseColorButton.addActionListener((ActionEvent e) -> {
            Color c = JColorChooser.showDialog(vue, "Choisir une couleur pour le texte", chosenColor);
            if (c != null) {
                chosenColor = c;
                vue.colorPreview.setBackground(c);
                vue.previewLabel.setForeground(c);
                vue.statusLabel.setText("✓ Couleur sélectionnée");
            }
        });

        // --- Sélection image ---
        this.vue.chooseImageButton.addActionListener((ActionEvent e) -> selectImage());

        // --- Génération PDF ---
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
                        "PDF généré avec succès ! Voulez-vous ouvrir le fichier ?",
                        "Succès",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(dest));
                }
            } catch (Exception ex) {
                vue.statusLabel.setText("❌ Erreur : " + ex.getMessage());
                JOptionPane.showMessageDialog(vue,
                        "Erreur détaillée : " + ex.getMessage(),
                        "Erreur de génération",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Mise à jour de l’aperçu de la police ---
        this.vue.fontSizeCombo.addActionListener((ActionEvent e) -> updateFontPreview());

        // --- Sauvegarde du projet ---
        this.vue.saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Sauvegarder le projet");
            chooser.setSelectedFile(new File("mon_projet.proj"));
            int res = chooser.showSaveDialog(vue);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                try {
                    Dataprojet data = collecterDonnees();
                    modele.sauvegarderProjet(data, f.getAbsolutePath());
                    vue.statusLabel.setText("✅ Projet sauvegardé : " + f.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(vue,
                            "Erreur sauvegarde : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Chargement du projet ---
        this.vue.loadButton.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Charger un projet");
            int res = chooser.showOpenDialog(vue);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                try {
                    Dataprojet data = modele.chargerProjet(f.getAbsolutePath());
                    appliquerDonnees(data);
                    vue.statusLabel.setText("✅ Projet chargé : " + f.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vue,
                            "Erreur chargement : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // --- Collecter les données actuelles de la vue ---
    private Dataprojet collecterDonnees() {
        Dataprojet data = new Dataprojet();
        data.texteAffichage = vue.inputField.getText();
        data.contenuQR = vue.qrContentField.getText();
        data.fontPath = fontPath;
        data.fontSize = (int) vue.fontSizeCombo.getSelectedItem();
        data.color = chosenColor;
        data.includeQr = vue.includeQrCheckBox.isSelected();
        data.imagePath = imagePath;
        data.imageWidth = (int) vue.imageWidthCombo.getSelectedItem();
        data.imageHeight = (int) vue.imageHeightCombo.getSelectedItem();
        data.imagePosition = (String) vue.imagePositionCombo.getSelectedItem();
        return data;
    }

    // --- Appliquer les données chargées ---
    private void appliquerDonnees(Dataprojet data) {
        vue.inputField.setText(data.texteAffichage);
        vue.qrContentField.setText(data.contenuQR);
        fontPath = data.fontPath;
        chosenColor = data.color;
        vue.colorPreview.setBackground(chosenColor);
        vue.previewLabel.setForeground(chosenColor);
        vue.fontSizeCombo.setSelectedItem(data.fontSize);
        vue.includeQrCheckBox.setSelected(data.includeQr);
        vue.imageWidthCombo.setSelectedItem(data.imageWidth);
        vue.imageHeightCombo.setSelectedItem(data.imageHeight);
        vue.imagePositionCombo.setSelectedItem(data.imagePosition);
        imagePath = data.imagePath;

        if (fontPath != null) {
            File f = new File(fontPath);
            vue.fontPathLabel.setText("Police : " + f.getName());
        } else {
            vue.fontPathLabel.setText("Police : Helvetica (défaut)");
        }

        if (imagePath != null) {
            File img = new File(imagePath);
            vue.imagePathLabel.setText("Image : " + img.getName());
        } else {
            vue.imagePathLabel.setText("Aucune image sélectionnée");
        }

        updateFontPreview();
    }

    // --- Sélection d’une image ---
    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une image");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg")
                        || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
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
        try {
            if (fontPath != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath))
                        .deriveFont(Font.PLAIN, getPreviewFontSize());
                vue.previewLabel.setFont(customFont);
            } else {
                Font newFont = vue.previewLabel.getFont().deriveFont(getPreviewFontSize());
                vue.previewLabel.setFont(newFont);
            }
        } catch (Exception e) {
            vue.previewLabel.setFont(new Font("SansSerif", Font.PLAIN, (int) getPreviewFontSize()));
        }
    }

    private void selectFontFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir une police TTF");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
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
                        "Impossible de charger la police : " + ex.getMessage(),
                        "Erreur de police",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

