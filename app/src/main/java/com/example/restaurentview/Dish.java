package com.example.restaurentview;

import android.net.Uri;

public class Dish {
    private String discount;
    private String dishAbout;
    private String dishImage;
    private String dishName;
    private String newDish;
    private String price;
    private String recommended;
    private String veg;


    public Dish(String discount, String dishAbout, String dishImage, String dishName, String newDish, String price, String recommended, String veg) {
        this.discount = discount;
        this.dishAbout = dishAbout;
        this.dishImage = dishImage;
        this.dishName = dishName;
        this.newDish = newDish;
        this.price = price;
        this.recommended = recommended;
        this.veg = veg;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setDishAbout(String dishAbout) {
        this.dishAbout = dishAbout;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setNewDish(String newDish) {
        this.newDish = newDish;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDishAbout() {
        return dishAbout;
    }

    public String getDishImage() {
        return dishImage;
    }
    public String getDishName() {
        return dishName;
    }

    public String getNewDish() {
        return newDish;
    }

    public String getPrice() {
        return price;
    }

    public String getRecommended() {
        return recommended;
    }

    public String getVeg() {
        return veg;
    }
}
