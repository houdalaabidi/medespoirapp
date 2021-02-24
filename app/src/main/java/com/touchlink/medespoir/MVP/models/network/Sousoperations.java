package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Sousoperations implements Serializable {

    private ArrayList<Sousoperation> sousoperations ;

    public Sousoperations(ArrayList<Sousoperation> sousoperations) {
        this.sousoperations = sousoperations;
    }

    public ArrayList<Sousoperation> getSousoperations() {
        return sousoperations;
    }

    public void setSousoperations(ArrayList<Sousoperation> sousoperations) {
        this.sousoperations = sousoperations;
    }
}
