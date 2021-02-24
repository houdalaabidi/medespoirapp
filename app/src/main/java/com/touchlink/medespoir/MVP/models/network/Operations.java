package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;

import com.touchlink.medespoir.MVP.models.network.Operation;

public class Operations implements Serializable {

    ArrayList<Operation> operations ;

    public Operations(ArrayList<Operation> operations) {
        this.operations = operations;
    }


    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }
}
