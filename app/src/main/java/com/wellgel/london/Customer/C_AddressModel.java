package com.wellgel.london.Customer;

public class C_AddressModel {
    int addressID;
    String addressType;
    String fullAddress;
    String addressUser;
    String addresssOption;
    String streetAddres;
    String addressNme;
    String zipcode;
    String county;
    String country;
    String city;

    public String getStreetAddres() {
        return streetAddres;
    }

    public void setStreetAddres(String streetAddres) {
        this.streetAddres = streetAddres;
    }

    public String getAddressNme() {
        return addressNme;
    }

    public void setAddressNme(String addressNme) {
        this.addressNme = addressNme;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public String getAddresssOption() {
        return addresssOption;
    }

    public void setAddresssOption(String addresssOption) {
        this.addresssOption = addresssOption;
    }
}
