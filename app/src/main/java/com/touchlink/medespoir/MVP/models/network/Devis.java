package com.touchlink.medespoir.MVP.models.network;

import java.util.ArrayList;

public class Devis {

    ArrayList<DevisItem>  devis ;

    public Devis(ArrayList<DevisItem> devis) {
        this.devis = devis;
    }

    public ArrayList<DevisItem> getDevis() {
        return devis;
    }

    public void setDevis(ArrayList<DevisItem> devis) {
        this.devis = devis;
    }
}
