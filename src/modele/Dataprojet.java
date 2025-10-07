package modele;

import java.awt.Color;
import java.io.Serializable;

public class Dataprojet implements Serializable {
    private static final long serialVersionUID = 1L;

    
    public String texteAffichage;   
    public String contenuQR;        
    public String fontPath;         
    public int fontSize;            
    public Color color;             
    public boolean includeQr;       

    public String imagePath;        
    public int imageWidth;          
    public int imageHeight;         
    public String imagePosition;    

  
    public Dataprojet() {}

   
    public Dataprojet(String texteAffichage, String contenuQR, String fontPath, int fontSize, Color color,
                      boolean includeQr, String imagePath, int imageWidth, int imageHeight, String imagePosition) {
        this.texteAffichage = texteAffichage;
        this.contenuQR = contenuQR;
        this.fontPath = fontPath;
        this.fontSize = fontSize;
        this.color = color;
        this.includeQr = includeQr;
        this.imagePath = imagePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imagePosition = imagePosition;
    }

    @Override
    public String toString() {
        return "Dataprojet{" +
                "texteAffichage='" + texteAffichage + '\'' +
                ", contenuQR='" + contenuQR + '\'' +
                ", fontPath='" + fontPath + '\'' +
                ", fontSize=" + fontSize +
                ", color=" + color +
                ", includeQr=" + includeQr +
                ", imagePath='" + imagePath + '\'' +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", imagePosition='" + imagePosition + '\'' +
                '}';
    }
}
