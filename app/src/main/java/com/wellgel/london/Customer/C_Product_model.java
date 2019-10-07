package com.wellgel.london.Customer;

public class C_Product_model {

    String productImage;
    String productName;
    String proddductPrice;
    int productID;



    private boolean isChecked = false;
    private int serviceID;

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public boolean isChecked() {
        return isChecked;
    }
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProddductPrice() {
        return proddductPrice;
    }

    public void setProddductPrice(String proddductPrice) {
        this.proddductPrice = proddductPrice;
    }
}
