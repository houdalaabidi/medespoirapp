package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Compterendus implements Serializable {
    private int id ;
    private String docteur ;
    private String  categorie;
    private String bilanpreparatoire ;
    private ArrayList<Bilanoperatoires> bilanoperatoires ;

    public Compterendus(int id, String docteur, String categorie, String bilanpreparatoire, ArrayList<Bilanoperatoires> bilanoperatoires) {
        this.id = id;
        this.docteur = docteur;
        this.categorie = categorie;
        this.bilanpreparatoire = bilanpreparatoire;
        this.bilanoperatoires = bilanoperatoires;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocteur() {
        return docteur;
    }

    public void setDocteur(String docteur) {
        this.docteur = docteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getBilanpreparatoire() {
        return bilanpreparatoire;
    }

    public void setBilanpreparatoire(String bilanpreparatoire) {
        this.bilanpreparatoire = bilanpreparatoire;
    }

    public ArrayList<Bilanoperatoires> getBilanoperatoires() {
        return bilanoperatoires;
    }

    public void setBilanoperatoires(ArrayList<Bilanoperatoires> bilanoperatoires) {
        this.bilanoperatoires = bilanoperatoires;
    }
}
