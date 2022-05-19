package com.example.ocr_1.Model;

public class Cart {

    private String cartId;
    private String userId;
    private String productId;
    private String quantity;

    public Cart() {
    }

    public Cart(String userId, String productId, String quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Cart(String cartId, String userId, String productId, String quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getCartId() {
        return cartId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
