package com.touchlink.medespoir.MVP.models.network;


import java.io.Serializable;
import java.util.ArrayList;

public class Actualite implements Serializable {

    ArrayList<Article> actualite ;

    public Actualite(ArrayList<Article> actualite) {
        this.actualite = actualite;
    }

    public ArrayList<Article> getActualite() {
        return actualite;
    }

    public void setActualite(ArrayList<Article> actualites) {
        this.actualite = actualite;
    }
}