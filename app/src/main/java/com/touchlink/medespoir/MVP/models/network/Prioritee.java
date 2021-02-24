package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Prioritee implements Serializable {

    private int id ;
    private String lable ;

    public Prioritee(int id, String lable) {
        this.id = id;
        this.lable = lable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
}
