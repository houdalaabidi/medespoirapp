package com.touchlink.medespoir.MVP.models.network;

public class ProgramItem {

    private String label ;
    private String heuredebut ;
    private String heurefin ;
    private String description ;
    private String categorie;
    private String id ;

    public ProgramItem(String label, String heuredebut, String heurefin, String description, String categorie) {
        this.label = label;
        this.heuredebut = heuredebut;
        this.heurefin = heurefin;
        this.description = description;
        this.categorie = categorie;
    }

    public ProgramItem(String label, String heuredebut, String heurefin, String description, String categorie, String id) {
        this.label = label;
        this.heuredebut = heuredebut;
        this.heurefin = heurefin;
        this.description = description;
        this.categorie = categorie;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHeuredebut() {
        return heuredebut;
    }

    public void setHeuredebut(String heuredebut) {
        this.heuredebut = heuredebut;
    }

    public String getHeurefin() {
        return heurefin;
    }

    public void setHeurefin(String heurefin) {
        this.heurefin = heurefin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
