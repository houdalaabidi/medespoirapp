package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Reclamation implements Serializable {
    DateReclamation date ;
    String title;
    String priorite;
    String description;
    String statut;
    String id ;
    ArrayList<Image> images ;


    public Reclamation(DateReclamation date,int id , String title, String priorite, String description, String statut) {
        this.date = date;
        this.title = title;
        this.priorite = priorite;
        this.description = description;
        this.statut = statut;
        this.id = id+"" ;
    }

    public Reclamation(DateReclamation date, String title, String priorite, String description, String statut, String id, ArrayList<Image> images) {
        this.date = date;
        this.title = title;
        this.priorite = priorite;
        this.description = description;
        this.statut = statut;
        this.id = id;
        this.images = images;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateReclamation getDate() {
        return date;
    }

    public void setDate(DateReclamation date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
