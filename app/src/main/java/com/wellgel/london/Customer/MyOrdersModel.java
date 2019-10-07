package com.wellgel.london.Customer;

import java.io.Serializable;

public class MyOrdersModel implements Serializable {

    String myProductname;
    String myProductQuan;
    int myProductPrice;
    String myProductAddress;
    String myOrderstatus;
    String myOrderTotalAmount;
    String invoiceNum;
    String productImagel;

    public String getProductImagel() {
        return productImagel;
    }

    public void setProductImagel(String productImagel) {
        this.productImagel = productImagel;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getMyOrderTotalAmount() {
        return myOrderTotalAmount;
    }

    public void setMyOrderTotalAmount(String myOrderTotalAmount) {
        this.myOrderTotalAmount = myOrderTotalAmount;
    }

    public String getMyOrderstatus() {
        return myOrderstatus;
    }

    public void setMyOrderstatus(String myOrderstatus) {
        this.myOrderstatus = myOrderstatus;
    }

    public String getMyProductname() {
        return myProductname;
    }

    public void setMyProductname(String myProductname) {
        this.myProductname = myProductname;
    }

    public String getMyProductQuan() {
        return myProductQuan;
    }

    public void setMyProductQuan(String myProductQuan) {
        this.myProductQuan = myProductQuan;
    }

    public int getMyProductPrice() {
        return myProductPrice;
    }

    public void setMyProductPrice(int myProductPrice) {
        this.myProductPrice = myProductPrice;
    }

    public String getMyProductAddress() {
        return myProductAddress;
    }

    public void setMyProductAddress(String myProductAddress) {
        this.myProductAddress = myProductAddress;
    }
}
