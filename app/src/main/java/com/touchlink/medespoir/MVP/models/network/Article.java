package com.touchlink.medespoir.MVP.models.network;

public class Article {

    private String label ;
    private String description ;
    private DateReclamation date ;
    private String image ;
    private String categorie ;

    public Article(String label, String description, DateReclamation date, String image) {
        this.label = label;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public Article(String label, String description, DateReclamation date, String image, String categorie) {
        this.label = label;
        this.description = description;
        this.date = date;
        this.image = image;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateReclamation getDate() {
        return date;
    }

    public void setDate(DateReclamation date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
