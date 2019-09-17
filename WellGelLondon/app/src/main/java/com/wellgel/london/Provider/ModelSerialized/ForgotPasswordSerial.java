package com.wellgel.london.Provider.ModelSerialized;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForgotPasswordSerial {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("error")
    @Expose
    private Error error;




    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }



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


        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }


    public class Error {

        @SerializedName("error_message")
        @Expose
        private ErrorMessage errorMessage;

        public ErrorMessage getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
        }

    }


    public class ErrorMessage {

        @SerializedName("message")
        @Expose
        private List<String> message = null;

        public List<String> getMessage() {
            return message;
        }

        public void setMessage(List<String> message) {
            this.message = message;
        }

    }



}
