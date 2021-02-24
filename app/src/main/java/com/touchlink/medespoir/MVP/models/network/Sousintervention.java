package com.touchlink.medespoir.MVP.models.network;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Sousintervention implements Parcelable, Serializable {

    public static final Creator<Sousintervention> CREATOR = new Creator<Sousintervention>() {
        @Override
        public Sousintervention createFromParcel(Parcel in) {
            return new Sousintervention(in);
        }

        @Override
        public Sousintervention[] newArray(int size) {
            return new Sousintervention[size];
        }
    };


    private int id ;
    private String sousoperation ;

    protected Sousintervention(Parcel in) {
        id = in.readInt();
        sousoperation = in.readString();

    }


    public Sousintervention(int id, String sousoperation) {
        this.id = id;
        this.sousoperation = sousoperation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSousoperation() {
        return sousoperation;
    }

    public void setSousoperation(String sousoperation) {
        this.sousoperation = sousoperation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(sousoperation);
    }







}
