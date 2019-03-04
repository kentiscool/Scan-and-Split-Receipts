package com.example.kent.split2;

import android.support.annotation.NonNull;

import com.example.kent.split2.RecyclerViewFriends.FriendObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDatabaseItem implements Serializable{
    private String receiptId;
    private String productId;
    private String productName;
    private double productPrice;
    public ArrayList<FriendObject> users;
    private FriendObject currentUser;

    public ProductDatabaseItem() {}

    public ProductDatabaseItem(String receiptId, String productId) {
        this.productId = productId;
        this.receiptId = receiptId;
    }

    public ProductDatabaseItem(String receiptId,String productId, String name, double price) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.productName = name;
        this.productPrice = price;
        users = new ArrayList<>();
        currentUser = new FriendObject(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public ArrayList<FriendObject> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<FriendObject> users) {
        this.users = users;
    }

    public void addUser(FriendObject f) {
        users.add(f);
    }

    public void removeUser(FriendObject f){
        users.remove(f);
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public String getUsersString() {
        String text = "";
        if (!users.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                if (!users.get(i).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    System.out.println("name : "+ users.get(i).getEmail() + " = " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    text += users.get(i).getEmail() + ", ";
                } else {
                    String temp = text;
                    text = "You";
                    text = text + ", " + temp;
                }
            }
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    public boolean isSelected() {
        boolean b = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(currentUser.getEmail())) {
                b = true;
                break;
            }
        }
          return b;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("receiptId", receiptId);
        result.put("productId", productId);
        result.put("productName", productName);
        result.put("productPrice", productPrice);
        return result;
    }

}
