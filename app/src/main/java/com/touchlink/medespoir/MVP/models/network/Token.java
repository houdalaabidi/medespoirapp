package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class Token implements Serializable {
    private String token ;

    public Token(String token) {
        this.token = token;
    }

    public String getStringToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
