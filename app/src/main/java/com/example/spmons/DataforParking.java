package com.example.spmons;

import androidx.annotation.NonNull;

public class DataforParking implements Comparable<DataforParking> {

    public String Parkingspot;
    public String Date;
    public String Time;
    public String Status;
    public String Statusvalue;

    public DataforParking () {
        this.Parkingspot = Parkingspot;
        this.Date = Date;
        this.Time = Time;
        this.Status = Status;
    }
    public DataforParking (String Parkingspot, String Date,String Time, String Status, String Statusvalue ) {
        this.Parkingspot = Parkingspot;
        this.Date = Date;
        this.Time = Time;
        this.Status = Status;
    }


    public String getParkingspot() {
        return Parkingspot;
    }
    public String getDate() {

        return Date;
    }
    public String getTime() {

        return Time;
    }
    public String getStatus() {

        return Status;
    }
    public String getStatusvalue() {

        return Statusvalue;
    }

    @Override
    public int compareTo(@NonNull DataforParking o) {
        return this.Date.compareTo(o.Date);
    }

    public int countVacant(){
        int count = 0;

        if(this.getStatus() == "Vacant")
        {
            count++;
        }

        return count;
    }

}
