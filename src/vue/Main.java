package vue;

import modele.ModeleQRCode;
import controleur.ControleurQRCode;

public class Main {
    public static void main(String[] args) {
        ModeleQRCode modele = new ModeleQRCode();
        VueQRCode vue = new VueQRCode();
        ControleurQRCode controleur = new ControleurQRCode(modele, vue);
    }
}
