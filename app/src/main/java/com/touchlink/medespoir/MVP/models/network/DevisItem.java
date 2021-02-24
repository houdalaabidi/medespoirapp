package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class DevisItem implements Serializable {
    private int id ;
    private String heure ;
    private String datedebut ;
    private String datefin;
    private String statut ;
    private String total ;

    public DevisItem(int id, String heure, String datedebut, String datefin, String statut, String total) {
        this.id = id;
        this.heure = heure;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.statut = statut;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(String datedebut) {
        this.datedebut = datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
