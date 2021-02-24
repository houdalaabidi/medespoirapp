package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Fichier implements Serializable {

    private String fichier ;

    public Fichier(String fichier) {
        this.fichier = fichier;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }
}
