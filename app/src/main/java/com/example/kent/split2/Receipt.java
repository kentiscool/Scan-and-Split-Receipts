package com.example.kent.split2;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private List<Product> items;
    private List<Product> itemsBoughtAfterTaxes = new ArrayList<>();
    private List<Product> taxes = new ArrayList<>();
    private double total;
    private Bitmap picture;

    Receipt(List<Product> items, Bitmap picture) {
        this.items = items;
        this.itemsBoughtAfterTaxes.addAll(items);
        this.picture = picture;
    }

    Receipt(List<Product> items) {
        this.items = items;
        this.itemsBoughtAfterTaxes.addAll(items);
        double largest=0;
        int i;
        for (i =0; i < items.size(); i++) {
            double CurrentPrice = items.get(i).getPrice();
            if (CurrentPrice > largest) {
                largest = CurrentPrice;
            }
        }

    }

    public double calculateTotal () {
        double sum =0;
        for (int i =0; i < itemsBoughtAfterTaxes.size(); i++) {
            sum += itemsBoughtAfterTaxes.get(i).getPrice();
        }
        return sum;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotal(int position){
        this.total = itemsBoughtAfterTaxes.get(position).getPrice();
    }

    public List<Product> getItemsBoughtAfterTaxes() {
        return itemsBoughtAfterTaxes;
    }

    public List<Product> getItems() {
        return items;
    }

    public double getCalculatedTotal() {
        double sum =0;
        for (int i =0;i< itemsBoughtAfterTaxes.size();i++) {
            sum += itemsBoughtAfterTaxes.get(i).getPrice();
        }
        return sum;
    }

    public void delete(Product product) {
        if (taxes.contains(product)) {
            removeTax(product);
            adjustPricesForTax();
        }else {
            itemsBoughtAfterTaxes.remove(product);
        }
    }

    public Product getLargest() {
        double largest=0;
        int pos = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getPrice() > largest) {
                largest = items.get(i).getPrice();
                pos =i;
            }
        }
        return itemsBoughtAfterTaxes.get(pos);
    }

    public boolean totalIsCorrect() {
        if (getLargest().getPrice() - getCalculatedTotal() < getLargest().getPrice()/20) {
            return true;
        }
        return false;
    }

    public void addItem(Product p) {
        items.add(p);
    }

    public void removeItem(Product p) {
        itemsBoughtAfterTaxes.remove(p);
    }
    public void removeItem(int position) {itemsBoughtAfterTaxes.remove(position);}

    public void addTax(Product tax) {
        taxes.add(tax);
        itemsBoughtAfterTaxes.remove(tax);
        adjustPricesForTax();
    }

    public void addTax(int position) {
        taxes.add(itemsBoughtAfterTaxes.get(position));
        itemsBoughtAfterTaxes.remove(position);
        adjustPricesForTax();
    }

    public void removeTax(Product tax) {
        itemsBoughtAfterTaxes.add(tax);
        taxes.remove(tax);
        adjustPricesForTax();
    }

    public double calculateTaxPercentage() {
        double totalTax = 0;
        for (int i = 0; i < taxes.size();i++) {
            totalTax += taxes.get(i).getPrice();
        }
        return totalTax/getCalculatedTotal();
    }

    public void adjustPricesForTax() {
        itemsBoughtAfterTaxes.clear();
        for (int i =0; i<items.size();i++) {
            Product p = items.get(i);
            if (!taxes.contains(p)) {
                Product newP = new Product(p.getName(),p.getPrice());
                itemsBoughtAfterTaxes.add(p);
            }
        }
    }

    public Bitmap getPicture() {
        return picture;
    }

}
