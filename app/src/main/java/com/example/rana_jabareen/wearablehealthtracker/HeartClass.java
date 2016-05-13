package com.example.rana_jabareen.wearablehealthtracker;

/**
 * Created by RANA_JABAREEN on 04/21/2016.
 */
public class HeartClass {
    private double Value;
    private int HeartID;
    private int UploadFlag;
    private int DownloadFlag;
    private int PatientID;
    private String ValueDate;





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

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public int getHeartID() {
        return HeartID;
    }

    public void setHeartID(int heartID) {
        HeartID = heartID;
    }
}
