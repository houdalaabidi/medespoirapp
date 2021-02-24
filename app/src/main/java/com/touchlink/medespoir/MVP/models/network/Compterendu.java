package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;

public class Compterendu implements Serializable {

    private int id ;
    private String name ;
    private String secondeName ;
    private ArrayList<Compterendus> compterendus;

    public Compterendu(int id, String name, String secondeName, ArrayList<Compterendus> compterendus) {
        this.id = id;
        this.name = name;
        this.secondeName = secondeName;
        this.compterendus = compterendus;
    }

    public int getId() {
        return id;
    }


    @NonNull
    @Override
    public String toString() {
        return id + " " + name ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondeName() {
        return secondeName;
    }

    public void setSecondeName(String secondeName) {
        this.secondeName = secondeName;
    }

    public ArrayList<Compterendus> getCompterendus() {
        return compterendus;
    }

    public void setCompterendus(ArrayList<Compterendus> compterendus) {
        this.compterendus = compterendus;
    }
}
