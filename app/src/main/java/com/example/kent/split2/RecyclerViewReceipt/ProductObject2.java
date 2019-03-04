package com.example.kent.split2.RecyclerViewReceipt;

import com.example.kent.split2.RecyclerViewFriends.FriendObject;

import java.util.ArrayList;

public class ProductObject2 {

    private String name;
    private double price;
    private Boolean isTax;
    private ArrayList<FriendObject> users;

    public ProductObject2(String name, double price) {
        this.name = name;
        this.price = price;
        this.isTax = false;
        this.users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getIsTax() {
        return isTax;
    }
    public void setIsTax(Boolean isTax) { this.isTax = isTax; }

    public ArrayList<FriendObject> getUsers() {
        return users;
    }

    public void addUser(FriendObject user) {
        users.add(user);
    }



}
