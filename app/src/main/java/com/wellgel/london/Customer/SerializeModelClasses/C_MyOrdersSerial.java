package com.wellgel.london.Customer.SerializeModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class C_MyOrdersSerial implements Serializable{



    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }



    public class Address implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("street_address")
        @Expose
        private String streetAddress;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("county")
        @Expose
        private String county;
        @SerializedName("zip")
        @Expose
        private String zip;
        @SerializedName("country")
        @Expose
        private String country;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }


    public class Datum implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("address_id")
        @Expose
        private Integer addressId;
        @SerializedName("total_paid")
        @Expose
        private String totalPaid;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("orderdetails")
        @Expose
        private ArrayList<Orderdetail> orderdetails = null;
        @SerializedName("addresses")
        @Expose
        private ArrayList<Address> addresses = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Integer getAddressId() {
            return addressId;
        }

        public void setAddressId(Integer addressId) {
            this.addressId = addressId;
        }

        public String getTotalPaid() {
            return totalPaid;
        }

        public void setTotalPaid(String totalPaid) {
            this.totalPaid = totalPaid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ArrayList<Orderdetail> getOrderdetails() {
            return orderdetails;
        }

        public void setOrderdetails(ArrayList<Orderdetail> orderdetails) {
            this.orderdetails = orderdetails;
        }

        public ArrayList<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(ArrayList<Address> addresses) {
            this.addresses = addresses;
        }

    }



    public class Orderdetail implements Serializable{

        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("product_id")
        @Expose
        private ProductId productId;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public ProductId getProductId() {
            return productId;
        }

        public void setProductId(ProductId productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

    }


    public class ProductId implements Serializable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("wholesale_price")
        @Expose
        private Integer wholesalePrice;
        @SerializedName("is_added_to_cart_quantity")
        @Expose
        private Integer isAddedToCartQuantity;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getWholesalePrice() {
            return wholesalePrice;
        }

        public void setWholesalePrice(Integer wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
        }

        public Integer getIsAddedToCartQuantity() {
            return isAddedToCartQuantity;
        }

        public void setIsAddedToCartQuantity(Integer isAddedToCartQuantity) {
            this.isAddedToCartQuantity = isAddedToCartQuantity;
        }

    }

}
