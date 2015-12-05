package com.samstudio.temanusahapartner.entities;

/**
 * Created by satryaway on 11/10/2015.
 * Application model
 */
public class Application {
    private String id, datetime, status, processDatetime, meetupDatetime, meetupVenue, loanType, loanSegment, timeRange, notes;
    private Partner partner = new Partner();
    private Customer customer = new Customer();

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getLoanSegment() {
        return loanSegment;
    }

    public void setLoanSegment(String loanSegment) {
        this.loanSegment = loanSegment;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
