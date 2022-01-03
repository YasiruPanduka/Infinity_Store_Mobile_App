package com.example.myapp;

public class WishListModel {

    String Name;
    String Price;
    String Description;
    String currentDate;
    String currentTime;
    String documentId;

    public WishListModel() {
    }

    public WishListModel(String name, String price, String description, String currentDate, String currentTime) {
        Name = name;
        Price = price;
        Description = description;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
