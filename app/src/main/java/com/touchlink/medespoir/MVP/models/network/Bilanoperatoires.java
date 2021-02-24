package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Bilanoperatoires implements Serializable {

    private int id ;
    private String operastion ;
    private String description ;

    public Bilanoperatoires(int id, String operastion, String description) {
        this.id = id;
        this.operastion = operastion;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperastion() {
        return operastion;
    }

    public void setOperastion(String operastion) {
        this.operastion = operastion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
