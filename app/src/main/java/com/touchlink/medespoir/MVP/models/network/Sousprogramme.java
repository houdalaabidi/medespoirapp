package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Sousprogramme implements Serializable {
    ProgramItem program ;

    public Sousprogramme(ProgramItem program) {
        this.program = program;
    }

    public ProgramItem getProgram() {
        return program;
    }

    public void setProgram(ProgramItem program) {
        this.program = program;
    }
}
