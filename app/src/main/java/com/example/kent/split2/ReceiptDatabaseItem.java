package com.example.kent.split2;

import java.sql.Date;

public class ReceiptDatabaseItem {

    private String receiptName;
    private String Uid;

    public ReceiptDatabaseItem(){}

    public ReceiptDatabaseItem(String name, String Uid) {
        this.receiptName = name;
        this.Uid = Uid;
    }

    public String getReceiptName() {
        return receiptName;
    }
    public void setReceiptName(String name) {
        this.receiptName = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

}
