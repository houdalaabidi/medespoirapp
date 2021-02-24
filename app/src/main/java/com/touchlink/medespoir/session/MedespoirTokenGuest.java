package com.touchlink.medespoir.session;

public class MedespoirTokenGuest {


    private String token_geust;
    private boolean statut_geust_conected;

    public MedespoirTokenGuest(String token_geust, boolean statut_geust_conected) {
        this.token_geust = token_geust;
        this.statut_geust_conected = statut_geust_conected;
    }

    public String getToken_geust() {
        return token_geust;
    }

    public void setToken_geust(String token_geust) {
        this.token_geust = token_geust;
    }

    public boolean isStatut_geust_conected() {
        return statut_geust_conected;
    }

    public void setStatut_geust_conected(boolean statut_geust_conected) {
        this.statut_geust_conected = statut_geust_conected;
    }
}
