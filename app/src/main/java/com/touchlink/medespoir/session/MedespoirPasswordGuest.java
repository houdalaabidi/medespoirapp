package com.touchlink.medespoir.session;


public class MedespoirPasswordGuest {


    private String password_geust;
    private boolean statut_geust_conected;

    public MedespoirPasswordGuest(String password_geust, boolean statut_geust_conected) {
        this.password_geust = password_geust;
        this.statut_geust_conected = statut_geust_conected;
    }

    public String getPassword_geust() {
        return password_geust;
    }

    public void setPassword_geust(String password_geust) {
        this.password_geust = password_geust;
    }

    public boolean isStatut_geust_conected() {
        return statut_geust_conected;
    }

    public void setStatut_geust_conected(boolean statut_geust_conected) {
        this.statut_geust_conected = statut_geust_conected;
    }
}