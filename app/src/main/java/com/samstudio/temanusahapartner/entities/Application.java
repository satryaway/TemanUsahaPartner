package com.samstudio.temanusahapartner.entities;

/**
 * Created by satryaway on 11/10/2015.
 * Application model
 */
public class Application {
    private String id, datetime, status, processDatetime, meetupDatetime, meetupVenue;
    private Partner partner = new Partner();
    public Application (){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessDatetime() {
        return processDatetime;
    }

    public void setProcessDatetime(String processDatetime) {
        this.processDatetime = processDatetime;
    }

    public String getMeetupVenue() {
        return meetupVenue;
    }

    public void setMeetupVenue(String meetupVenue) {
        this.meetupVenue = meetupVenue;
    }

    public String getMeetupDatetime() {
        return meetupDatetime;
    }

    public void setMeetupDatetime(String meetupDatetime) {
        this.meetupDatetime = meetupDatetime;
    }
}
