package com.example.myapplication;
public class Order {
    private String orderText;
    private String orderDate;

    public Order(String orderText, String orderDate) {
        this.orderText = orderText;
        this.orderDate = orderDate;
    }

    public String getOrderText() {
        return orderText;
    }

    public String getOrderDate() {
        return orderDate;
    }
}