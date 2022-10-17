package com.example.spmons;

import android.os.Parcel;
import android.os.Parcelable;

class ParkingSampleData implements Parcelable {
    private String DeviceId;
    private String date;
    private String time;
    private String parkingstatus;



    protected ParkingSampleData(Parcel in) {
        DeviceId = in.readString();
        date = in.readString();
        time = in.readString();
        parkingstatus = in.readString();

    }

    public static final Creator<ParkingSampleData> CREATOR = new Creator<ParkingSampleData>() {
        @Override
        public ParkingSampleData createFromParcel(Parcel in) {
            return new ParkingSampleData(in);
        }

        @Override
        public ParkingSampleData[] newArray(int size) {
            return new ParkingSampleData[size];
        }
    };

    public ParkingSampleData() {

    }


    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParkingstatus() {
        return parkingstatus;
    }

    public void setParkingstatus(String parkingstatus) {
        this.parkingstatus = parkingstatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(DeviceId);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(parkingstatus);
    }
}