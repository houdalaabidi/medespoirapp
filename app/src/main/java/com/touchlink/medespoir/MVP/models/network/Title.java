package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Title implements Serializable {
    private ArrayList<Titre> title ;

    public Title(ArrayList<Titre> title) {
        this.title = title;
    }

    public ArrayList<Titre> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<Titre> title) {
        this.title = title;
    }
}
