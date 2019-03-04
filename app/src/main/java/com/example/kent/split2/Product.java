package com.example.kent.split2;

import com.example.kent.split2.RecyclerViewFriends.FriendObject;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private double price;
    private List<FriendObject> users;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public List<FriendObject> getUsers() {
        return this.users;
    }

    public void addUser(FriendObject user) {
        users.add(user);
    }

    public void removeUser(FriendObject user) {
        users.remove(user);
    }

}
