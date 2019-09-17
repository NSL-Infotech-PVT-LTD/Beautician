package com.wellgel.london.Customer.SerializeModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class C_PlaceOrderSerial {


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

    public class Data {

        @SerializedName("scalar")
        @Expose
        private String scalar;

        public String getScalar() {
            return scalar;
        }

        public void setScalar(String scalar) {
            this.scalar = scalar;
        }

    }


}
