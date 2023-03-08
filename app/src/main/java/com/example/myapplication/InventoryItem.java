package com.example.myapplication;

public class InventoryItem {
    private int itemID;
    private String itemName;
    private int isCase;
    private int reqStock;
    private int perCase;
    private String caseName;

    public InventoryItem(String itemName, int isCase, int reqStock, int perCase, String caseName, int itemID) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.isCase = isCase;
        this.reqStock = reqStock;
        this.perCase = perCase;
        this.caseName = caseName;
    }

    public String getItemName() {
        return itemName;
    }

    public int isCase() {
        return isCase;
    }

    public int getReqStock() {
        return reqStock;
    }

    public int getPerCase() {
        return perCase;
    }

    public String getCaseName() {
        return caseName;
    }
}