package com.wellgel.london.Customer.SerializeModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class C_CreateAppointment {




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
        private String salonUserId;
        @SerializedName("requested_datetime")
        @Expose
        private String requestedDatetime;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("customer_user_id")
        @Expose
        private Integer customerUserId;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;

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

        public String getSalonUserId() {
            return salonUserId;
        }

        public void setSalonUserId(String salonUserId) {
            this.salonUserId = salonUserId;
        }

        public String getRequestedDatetime() {
            return requestedDatetime;
        }

        public void setRequestedDatetime(String requestedDatetime) {
            this.requestedDatetime = requestedDatetime;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public Integer getCustomerUserId() {
            return customerUserId;
        }

        public void setCustomerUserId(Integer customerUserId) {
            this.customerUserId = customerUserId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }


    public class Data {

        @SerializedName("message")
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
