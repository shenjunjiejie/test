package com.dfsdk.ocr.scan.network.model;

public class DFAadhaarCardBackInfo extends DFCardBaseInfo {
    private String cardNo;
    private String vid;
    private String sonOf;
    private String daughterOf;
    private String wifeOf;
    private String careOf;
    private String husbandOf;
    private String city;
    private String state;
    private String pin;
    private String address;
    private String addressLineOne;
    private String addressLineTwo;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSonOf() {
        return sonOf;
    }

    public void setSonOf(String sonOf) {
        this.sonOf = sonOf;
    }

    public String getDaughterOf() {
        return daughterOf;
    }

    public void setDaughterOf(String daughterOf) {
        this.daughterOf = daughterOf;
    }

    public String getWifeOf() {
        return wifeOf;
    }

    public void setWifeOf(String wifeOf) {
        this.wifeOf = wifeOf;
    }

    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getHusbandOf() {
        return husbandOf;
    }

    public void setHusbandOf(String husbandOf) {
        this.husbandOf = husbandOf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    @Override
    public String toString() {
        return "DFAadhaarCardBackInfo{" +
                "cardNo='" + cardNo + '\'' +
                ", vid='" + vid + '\'' +
                ", sonOf='" + sonOf + '\'' +
                ", daughterOf='" + daughterOf + '\'' +
                ", wifeOf='" + wifeOf + '\'' +
                ", careOf='" + careOf + '\'' +
                ", husbandOf='" + husbandOf + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", address='" + address + '\'' +
                ", addressLineOne='" + addressLineOne + '\'' +
                ", addressLineTwo='" + addressLineTwo + '\'' +
                '}';
    }
}
