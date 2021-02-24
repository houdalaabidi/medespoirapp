package com.touchlink.medespoir.session;


public class MedespoirEmailGuest {


    private static String email_geust;
    private boolean statut_geust_conected;

    public MedespoirEmailGuest(String email_geust, boolean statut_geust_conected) {
        this.email_geust = email_geust;
        this.statut_geust_conected = statut_geust_conected;
    }

    public static String getEmail_geust() {
        return email_geust;
    }

    public void setEmail_geust(String email_geust) {
        this.email_geust = email_geust;
    }

    public boolean isStatut_geust_conected() {
        return statut_geust_conected;
    }

    public void setStatut_geust_conected(boolean statut_geust_conected) {
        this.statut_geust_conected = statut_geust_conected;
    }
}