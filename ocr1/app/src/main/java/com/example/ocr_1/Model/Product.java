package com.example.ocr_1.Model;

public class Product {

    private String pId;
    private String productName;
    private String price;
    private String description;

    public Product() {
    }

    public Product(String pId, String productName, String price, String description) {
        this.pId = pId;
        this.productName = productName;
        this.price = price;
        this.description = description;
    }

    public String getpId() {
        return pId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
