package com.touchlink.medespoir.MVP.models.network;

import java.io.Serializable;

public class User implements Serializable {

    private String token ;
    private String id ;
    private String username ;
    private String email ;

    public User() {
    }

    public User(String token,String email , String id, String username) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
