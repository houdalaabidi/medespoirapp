package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Sousoperation implements Serializable {


    private int id ;
    private String name ;
    private String prix ;
    private boolean extras ;

    public Sousoperation(int id, String name, String prix, boolean extras) {
        this.id = id;
        this.name = name;
        this.prix = prix;
        this.extras = extras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public boolean isExtras() {
        return extras;
    }

    public void setExtras(boolean extras) {
        this.extras = extras;
    }
}
