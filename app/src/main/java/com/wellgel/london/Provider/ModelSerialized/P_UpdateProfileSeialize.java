package com.wellgel.london.Provider.ModelSerialized;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class P_UpdateProfileSeialize {


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

        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("user")
        @Expose
        private User user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }


    public class Pivot {

        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("role_id")
        @Expose
        private Integer roleId;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

    }

    public class Role {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("pivot")
        @Expose
        private Pivot pivot;

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

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
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

        public Pivot getPivot() {
            return pivot;
        }

        public void setPivot(Pivot pivot) {
            this.pivot = pivot;
        }

    }


    public class User {

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
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("image_1")
        @Expose
        private String image1;
        @SerializedName("image_2")
        @Expose
        private String image2;
        @SerializedName("image_3")
        @Expose
        private String image3;
        @SerializedName("service_ids")
        @Expose
        private List<Integer> serviceIds = null;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("business_hour_start")
        @Expose
        private String businessHourStart;
        @SerializedName("business_hour_end")
        @Expose
        private String businessHourEnd;
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
        @SerializedName("subscription_id")
        @Expose
        private Integer subscriptionId;
        @SerializedName("stripe_id")
        @Expose
        private Object stripeId;
        @SerializedName("card_brand")
        @Expose
        private Object cardBrand;
        @SerializedName("card_last_four")
        @Expose
        private Object cardLastFour;
        @SerializedName("trial_ends_at")
        @Expose
        private Object trialEndsAt;
        @SerializedName("roles")
        @Expose
        private List<Role> roles = null;

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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public List<Integer> getServiceIds() {
            return serviceIds;
        }

        public void setServiceIds(List<Integer> serviceIds) {
            this.serviceIds = serviceIds;
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

        public String getBusinessHourStart() {
            return businessHourStart;
        }

        public void setBusinessHourStart(String businessHourStart) {
            this.businessHourStart = businessHourStart;
        }

        public String getBusinessHourEnd() {
            return businessHourEnd;
        }

        public void setBusinessHourEnd(String businessHourEnd) {
            this.businessHourEnd = businessHourEnd;
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

        public Integer getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(Integer subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public Object getStripeId() {
            return stripeId;
        }

        public void setStripeId(Object stripeId) {
            this.stripeId = stripeId;
        }

        public Object getCardBrand() {
            return cardBrand;
        }

        public void setCardBrand(Object cardBrand) {
            this.cardBrand = cardBrand;
        }

        public Object getCardLastFour() {
            return cardLastFour;
        }

        public void setCardLastFour(Object cardLastFour) {
            this.cardLastFour = cardLastFour;
        }

        public Object getTrialEndsAt() {
            return trialEndsAt;
        }

        public void setTrialEndsAt(Object trialEndsAt) {
            this.trialEndsAt = trialEndsAt;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

    }

}
