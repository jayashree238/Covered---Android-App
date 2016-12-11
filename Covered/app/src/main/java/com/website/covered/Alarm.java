package com.website.covered;

/**
 * Created by jayashreemadhanraj on 11/25/16.
 */

public class Alarm {

    String homeAddress;
    String destAddress;
    double homeLat, homeLng, destLat, destLng;

    int homeAlarm;
    int destAlarm;
    Boolean mon, tues, wed, thur, fri, sat, sun;

    Alarm(String homeAddress, double homeLat, double homeLng, String destAddress, double destLat, double destLng, int homeAlarm, int destAlarm, Boolean mon, Boolean tues, Boolean wed, Boolean thur, Boolean fri, Boolean sat, Boolean sun){
        this.homeAddress = homeAddress;
        this.destAddress = destAddress;
        this.homeLat = homeLat;
        this.homeLng = homeLng;
        this.destLat = destLat;
        this.destLng = destLng;
        this.homeAlarm = homeAlarm;
        this.destAlarm = destAlarm;
        this.mon = mon;
        this.tues = tues;
        this.wed = wed;
        this.thur = thur;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;

    }

    public double getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(double homeLat) {
        this.homeLat = homeLat;
    }

    public double getHomeLng() {
        return homeLng;
    }

    public void setHomeLng(double homeLng) {
        this.homeLng = homeLng;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLng() {
        return destLng;
    }


    public String getHomeAddress(){return homeAddress;}
    public String getDestAddress(){return destAddress;}
    public int getHomeAlarm(){return homeAlarm;}
    public int getDestAlarm(){return destAlarm;}

    public Boolean getMon(){return mon;}
    public Boolean getTues(){return tues;}
    public Boolean getWed(){return wed;}
    public Boolean getThur(){return thur;}
    public Boolean getFri(){return fri;}
    public Boolean getSat(){return sat;}
    public Boolean getSun(){return sun;}

}
