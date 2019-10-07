package com.wellgel.london.Provider.ModelSerialized;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class P_AppointmentListSerial {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Customerdetail {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("skin_color")
        @Expose
        private String skinColor;
        @SerializedName("nail_polish_color")
        @Expose
        private String nailPolishColor;
        @SerializedName("nail_shape")
        @Expose
        private String nailShape;
        @SerializedName("salon_user_id")
        @Expose
        private Integer salonUserId;
        @SerializedName("customer_user_id")
        @Expose
        private Integer customerUserId;
        @SerializedName("requested_datetime")
        @Expose
        private String requestedDatetime;
        @SerializedName("available_datetime")
        @Expose
        private String availableDatetime;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("rating")
        @Expose
        private Object rating;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("customerdetails")
        @Expose
        private List<Customerdetail> customerdetails = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSkinColor() {
            return skinColor;
        }

        public void setSkinColor(String skinColor) {
            this.skinColor = skinColor;
        }

        public String getNailPolishColor() {
            return nailPolishColor;
        }

        public void setNailPolishColor(String nailPolishColor) {
            this.nailPolishColor = nailPolishColor;
        }

        public String getNailShape() {
            return nailShape;
        }

        public void setNailShape(String nailShape) {
            this.nailShape = nailShape;
        }

        public Integer getSalonUserId() {
            return salonUserId;
        }

        public void setSalonUserId(Integer salonUserId) {
            this.salonUserId = salonUserId;
        }

        public Integer getCustomerUserId() {
            return customerUserId;
        }

        public void setCustomerUserId(Integer customerUserId) {
            this.customerUserId = customerUserId;
        }

        public String getRequestedDatetime() {
            return requestedDatetime;
        }

        public void setRequestedDatetime(String requestedDatetime) {
            this.requestedDatetime = requestedDatetime;
        }

        public String getAvailableDatetime() {
            return availableDatetime;
        }

        public void setAvailableDatetime(String availableDatetime) {
            this.availableDatetime = availableDatetime;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getRating() {
            return rating;
        }

        public void setRating(Object rating) {
            this.rating = rating;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public List<Customerdetail> getCustomerdetails() {
            return customerdetails;
        }

        public void setCustomerdetails(List<Customerdetail> customerdetails) {
            this.customerdetails = customerdetails;
        }

    }

}
