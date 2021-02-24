package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Priorite implements Serializable {

    ArrayList<Prioritee> priorite ;

    public Priorite(ArrayList<Prioritee> priorite) {
        this.priorite = priorite;
    }

    public ArrayList<Prioritee> getPriorite() {
        return priorite;
    }

    public void setPriorite(ArrayList<Prioritee> priorite) {
        this.priorite = priorite;
    }
}
