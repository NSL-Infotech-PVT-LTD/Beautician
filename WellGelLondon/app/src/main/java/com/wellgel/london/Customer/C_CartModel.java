package com.wellgel.london.Customer;

public class C_CartModel {

    String cartProductName;
    int cartProductPrice;
    String cartImage;
    String cartID;
    int cartQuantity;

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public String getCartImage() {
        return cartImage;
    }

    public void setCartImage(String cartImage) {
        this.cartImage = cartImage;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getCartProductName() {
        return cartProductName;
    }

    public void setCartProductName(String cartProductName) {
        this.cartProductName = cartProductName;
    }

    public int getCartProductPrice() {
        return cartProductPrice;
    }

    public void setCartProductPrice(int cartProductPrice) {
        this.cartProductPrice = cartProductPrice;
    }
}
