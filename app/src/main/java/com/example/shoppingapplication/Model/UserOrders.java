package com.example.shoppingapplication.Model;

public class UserOrders {
    private String pid, pname, discount, quantity, price;

    public UserOrders() {
    }

    public UserOrders(String pid, String pname, String discount, String quantity, String price) {
        this.pid = pid;
        this.pname = pname;
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
