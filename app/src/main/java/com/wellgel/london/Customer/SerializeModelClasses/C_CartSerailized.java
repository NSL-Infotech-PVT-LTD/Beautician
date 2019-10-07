package com.wellgel.london.Customer.SerializeModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class C_CartSerailized {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Cart {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customer_id")
        @Expose
        private Integer customerId;
        @SerializedName("cartdetails")
        @Expose
        private List<Cartdetail> cartdetails = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public List<Cartdetail> getCartdetails() {
            return cartdetails;
        }

        public void setCartdetails(List<Cartdetail> cartdetails) {
            this.cartdetails = cartdetails;
        }

    }

    public class Cartdetail {

        @SerializedName("cart_id")
        @Expose
        private Integer cartId;
        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("product")
        @Expose
        private Product product;

        public Integer getCartId() {
            return cartId;
        }

        public void setCartId(Integer cartId) {
            this.cartId = cartId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

    }


    public class Data {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("cart")
        @Expose
        private List<Cart> cart = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Cart> getCart() {
            return cart;
        }

        public void setCart(List<Cart> cart) {
            this.cart = cart;
        }

    }


    public class Product {

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
        private Integer wholesale_price;

        public Integer getWholesale_price() {
            return wholesale_price;
        }

        public void setWholesale_price(Integer wholesale_price) {
            this.wholesale_price = wholesale_price;
        }

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

    }

}
