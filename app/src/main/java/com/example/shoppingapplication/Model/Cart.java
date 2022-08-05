package com.example.shoppingapplication.Model;

import android.net.Uri;

public class Cart {
    private String pid, pname, discount, quantity, price, image;

    public Cart() {
    }

    public Cart(String pid, String pname, String discount, String quantity, String price, String image) {
        this.pid = pid;
        this.pname = pname;
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
