package com.example.kent.split2.RecyclerViewFriends;

public class FriendObject {

    private String email;
    private String uid;

    public FriendObject(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
