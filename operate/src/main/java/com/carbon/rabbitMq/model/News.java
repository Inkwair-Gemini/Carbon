package com.carbon.rabbitMq.model;

import java.io.Serializable;

public class News implements Serializable {
    private String sender;
    private String receiver;
    private String context;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "News{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
