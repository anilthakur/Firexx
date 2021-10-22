package com.example.fireex.model;

public class FireControlDetail {

    private String buildingName;
    private String extinguisherNum;
    private String extinguisherType;
    private String firePointNum;
    private String address;

    public FireControlDetail() {

    }

    public FireControlDetail(String buildingName, String extinguisherNum, String extinguisherType, String firePointNum, String address) {
        this.buildingName = buildingName;
        this.extinguisherNum = extinguisherNum;
        this.extinguisherType = extinguisherType;
        this.firePointNum = firePointNum;
        this.address = address;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getExtinguisherNum() {
        return extinguisherNum;
    }

    public void setExtinguisherNum(String extinguisherNum) {
        this.extinguisherNum = extinguisherNum;
    }

    public String getExtinguisherType() {
        return extinguisherType;
    }

    public void setExtinguisherType(String extinguisherType) {
        this.extinguisherType = extinguisherType;
    }

    public String getFirePointNum() {
        return firePointNum;
    }

    public void setFirePointNum(String firePointNum) {
        this.firePointNum = firePointNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
