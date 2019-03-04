package com.example.kent.split2.RecyclerViewRecipient;

public class RecipientObject {
    private String email;
    private String uid;
    private Boolean recieve;

    public RecipientObject(String email, String uid, Boolean recieve) {
        this.email = email;
        this.uid = uid;
        this.recieve = recieve;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRecieve() {
        return recieve;
    }
    public void setRecieve(Boolean recieve) {
        this.recieve = recieve;
    }

}
