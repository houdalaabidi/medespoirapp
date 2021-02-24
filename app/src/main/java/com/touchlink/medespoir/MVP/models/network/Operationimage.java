package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Operationimage implements Serializable {

    private int id ;
    private String image;

    public Operationimage(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
