package com.srp.rkim.argus;

import java.util.Date;

/**
 * Created by 2018rkim on 11/9/2017.
 */
public class TrackeeModel {
    private String trackeeName;
    private double latitude;
    private double longitude;
    private Date time;
    private String uid;

//    public TrackeeModel(String uid) {
//
//    }


    public TrackeeModel(String trackeeName, double latitude, double longitude, Date time) {
        this.trackeeName = trackeeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getTrackeeName() {
        return trackeeName;
    }

    public void setTrackeeName(String trackeeName) {
        this.trackeeName = trackeeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
