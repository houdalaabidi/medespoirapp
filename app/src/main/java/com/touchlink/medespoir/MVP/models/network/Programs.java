package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;
import java.util.ArrayList;


public class Programs implements Serializable {

    ArrayList<ProgramItem> programs ;

    public Programs(ArrayList<ProgramItem> programs) {
        this.programs = programs;
    }

    public ArrayList<ProgramItem> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<ProgramItem> programs) {
        this.programs = programs;
    }
}

