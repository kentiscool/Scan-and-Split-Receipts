package com.example.kent.split2.RecyclerViewRequests;

import java.util.ArrayList;

public class ReceiptObject {

    private String name;
    private double total;
    private ArrayList<String> users;
    private ArrayList<String> userUids;
    private String uid;

    public ReceiptObject(String name, double total, ArrayList<String> users, ArrayList<String> userUids, String uid) {
        this.name = name;
        this.total = total;
        this.users = new ArrayList<String>();
        this.userUids = new ArrayList<String>();
        this.uid = uid;
    }

    public ArrayList<String> getUserUids() {
        return userUids;
    }

    public void setUserUids(ArrayList<String> userUids) {
        this.userUids = userUids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
