package com.touchlink.medespoir.MVP.models.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MedespoirResponse implements Serializable {

    @SerializedName("status")
    private int status ;

    @SerializedName("data")
    private Object data ;
    @SerializedName("message")
    private String message ;

    public MedespoirResponse(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "MedespoirResponse{" +
                "status=" + status +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
