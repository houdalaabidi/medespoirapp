package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Statut implements Serializable {

    private int decliner;
    private int accepter;

    public Statut(int decliner, int accepter) {
        this.decliner = decliner;
        this.accepter = accepter;
    }


    public int getDecliner() {
        return decliner;
    }

    public void setDecliner(int decliner) {
        this.decliner = decliner;
    }

    public int getAccepter() {
        return accepter;
    }

    public void setAccepter(int accepter) {
        this.accepter = accepter;
    }
}
