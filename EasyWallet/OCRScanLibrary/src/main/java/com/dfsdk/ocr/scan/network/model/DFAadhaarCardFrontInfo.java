package com.dfsdk.ocr.scan.network.model;

public class DFAadhaarCardFrontInfo extends DFCardBaseInfo{
    private String cardNo;
    private String name;
    private String gender;
    private String motherName;
    private String fatherName;
    private String dateType;
    private String dateInfo;
    private String phoneNumber;
    private String vid;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "DFAadhaarCardFrontInfo{" +
                "cardNo='" + cardNo + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", motherName='" + motherName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", dateType='" + dateType + '\'' +
                ", dateInfo='" + dateInfo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", vid='" + vid + '\'' +
                '}';
    }
}
