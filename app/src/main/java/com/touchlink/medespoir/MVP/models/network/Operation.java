package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

import com.touchlink.medespoir.MVP.models.network.Sousintervention;

public class Operation implements Serializable {


    private int id ;
    private String operation ;
    private ArrayList<Sousintervention> sousintervention ;

    public Operation(int id, String operation, ArrayList<Sousintervention> sousintervention) {
        this.id = id;
        this.operation = operation;
        this.sousintervention = sousintervention;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ArrayList<Sousintervention> getSousintervention() {
        return sousintervention;
    }

    public void setSousintervention(ArrayList<Sousintervention> sousintervention) {
        this.sousintervention = sousintervention;
    }
}

