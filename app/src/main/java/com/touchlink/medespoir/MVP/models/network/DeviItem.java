package com.touchlink.medespoir.MVP.models.network;

import java.util.ArrayList;

public class DeviItem {

    private ArrayList<devisI> devisI ;

    public DeviItem(ArrayList<com.touchlink.medespoir.MVP.models.network.devisI> devisI) {
        this.devisI = devisI;
    }

    public ArrayList<com.touchlink.medespoir.MVP.models.network.devisI> getDevisI() {
        return devisI;
    }

    public void setDevisI(ArrayList<com.touchlink.medespoir.MVP.models.network.devisI> devisI) {
        this.devisI = devisI;
    }
}
