package com.wellgel.london.Provider.ModelSerialized;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RescheduleAppointment {


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

    public class Appointments {

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
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("payment_mode")
        @Expose
        private Object paymentMode;
        @SerializedName("transcation_detail")
        @Expose
        private Object transcationDetail;
        @SerializedName("rating")
        @Expose
        private Object rating;
        @SerializedName("params")
        @Expose
        private Object params;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public Object getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(Object paymentMode) {
            this.paymentMode = paymentMode;
        }

        public Object getTranscationDetail() {
            return transcationDetail;
        }

        public void setTranscationDetail(Object transcationDetail) {
            this.transcationDetail = transcationDetail;
        }

        public Object getRating() {
            return rating;
        }

        public void setRating(Object rating) {
            this.rating = rating;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

    }


    public class Data {

        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("appointments")
        @Expose
        private Appointments appointments;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Appointments getAppointments() {
            return appointments;
        }

        public void setAppointments(Appointments appointments) {
            this.appointments = appointments;
        }

    }


}
