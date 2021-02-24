package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class devisI implements Serializable {

    private int id;
    private String datedebut ;
    private String datefin ;
    private Statut statut ;
    private String description ;
    private String total;
    private ArrayList<Sousoperation> sousoperations ;
    private ArrayList<Operationimage> operationimages = new ArrayList<Operationimage>();

    public devisI(int id, String datedebut, String description ,String datefin, Statut statut, String total, ArrayList<Sousoperation> sousoperations, ArrayList<Operationimage> operationimages) {
        this.id = id;
        this.description = description ;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.statut = statut;
        this.total = total;
        this.sousoperations = sousoperations;
        this.operationimages = operationimages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<Sousoperation> getSousoperations() {
        return sousoperations;
    }

    public void setSousoperations(ArrayList<Sousoperation> sousoperations) {
        this.sousoperations = sousoperations;
    }

    public ArrayList<Operationimage> getOperationimages() {
        return operationimages;
    }

    public void setOperationimages(ArrayList<Operationimage> operationimages) {
        this.operationimages = operationimages;
    }
}
