package com.example.kent.split2.RecyclerViewReciept3;

public class RecyclerViewReceipt3ProductObject {
    private String name;
    private double price;
    private Boolean isChosen;

    public RecyclerViewReceipt3ProductObject(String name, double price) {
        this.name = name;
        this.price = price;
        this.isChosen = false;
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
        return isChosen;
    }
    public void setIsTax(Boolean bool) { this.isChosen = bool; }

}
