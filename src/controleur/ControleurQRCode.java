package controleur;

import modele.ModeleQRCode;
import vue.VueQRCode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurQRCode {
    private ModeleQRCode modele;
    private VueQRCode vue;

    public ControleurQRCode(ModeleQRCode modele, VueQRCode vue) {
        this.modele = modele;
        this.vue = vue;

        this.vue.generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = vue.inputField.getText();
                try {
                    modele.genererQRCodePDF(data);
                    vue.statusLabel.setText("PDF généré avec succès !");
                } catch (Exception ex) {
                    vue.statusLabel.setText("Erreur : " + ex.getMessage());
                }
            }
        });
    }
}
