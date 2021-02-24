package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class CompteRBilan implements Serializable {
    private Compterendu compterendu ;

    public CompteRBilan(Compterendu compterendu) {
        this.compterendu = compterendu;
    }

    public Compterendu getCompterendu() {
        return compterendu;
    }

    public void setCompterendu(Compterendu compterendu) {
        this.compterendu = compterendu;
    }
}
