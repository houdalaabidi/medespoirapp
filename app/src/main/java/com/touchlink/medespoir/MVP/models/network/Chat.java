package com.touchlink.medespoir.MVP.models.network;

public class Chat {

    private String sender ;
    private String receiver;
    private String created_date;
    private String message ;
    private String id_plateform;
    private String seen;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_plateform() {
        return id_plateform;
    }

    public void setId_plateform(String id_plateform) {
        this.id_plateform = id_plateform;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Chat(String sender, String receiver, String created_date, String message , String id_plateform , String seen) {

        this.sender = sender;
        this.id_plateform = id_plateform ;
        this.seen = seen;
        this.receiver = receiver;
        this.created_date = created_date;
        this.message = message;
    }

    public Chat() {

    }

    public String toString(){
        return "sender : " + this.getSender() +  "receiver : "+ this.receiver + " date de creation : "+ this.created_date +
                " seen : " + this.seen + " message : " + this.message +  " id_plareform : " + this.id_plateform ;
    }
}
