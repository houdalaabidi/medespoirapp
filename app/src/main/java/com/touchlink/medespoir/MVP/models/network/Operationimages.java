package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Operationimages implements Serializable {
    private ArrayList<Operationimage> operationimages;

    public Operationimages(ArrayList<Operationimage> operationimages) {
        this.operationimages = operationimages;
    }

    public ArrayList<Operationimage> getOperationimages() {
        return operationimages;
    }

    public void setOperationimages(ArrayList<Operationimage> operationimages) {
        this.operationimages = operationimages;
    }
}
