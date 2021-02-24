package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Reclamations implements Serializable {

    ArrayList<Reclamation> reclamations ;

    public Reclamations(ArrayList<Reclamation> reclamations) {
        this.reclamations = reclamations;
    }

    public ArrayList<Reclamation> getReclamations() {
        return reclamations;
    }

    public void setReclamations(ArrayList<Reclamation> reclamations) {
        this.reclamations = reclamations;
    }
}
