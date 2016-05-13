package com.example.rana_jabareen.wearablehealthtracker;

/**
 * Created by RANA_JABAREEN on 04/21/2016.
 */
public class LocationClass {
    private double Longitude;
    private double Latitude;
    private int LocationID;
    private int UploadFlag;
    private int DownloadFlag;
    private int PatientID;
    private String ValueDate;

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public int getLocationID() {
        return LocationID;
    }

    public void setLocationID(int locationID) {
        LocationID = locationID;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public int getDownloadFlag() {
        return DownloadFlag;
    }

    public void setDownloadFlag(int downloadFlag) {
        DownloadFlag = downloadFlag;
    }

    public int getPatientID() {
        return PatientID;
    }

    public void setPatientID(int patientID) {
        PatientID = patientID;
    }

    public String getValueDate() {
        return ValueDate;
    }

    public void setValueDate(String valueDate) {
        ValueDate = valueDate;
    }
}
