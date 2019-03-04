package com.example.kent.split2.RecyclerViewReciept1;

public class ProductObject1 {
    private String name;
    private double price;
    private Boolean isTax;

    public ProductObject1(String name, double price) {
        this.name = name;
        this.price = price;
        this.isTax = false;
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

}
