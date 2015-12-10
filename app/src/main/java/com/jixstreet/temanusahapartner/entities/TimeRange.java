package com.jixstreet.temanusahapartner.entities;

/**
 * Created by satryaway on 10/5/2015.
 * time range
 */
public class TimeRange {
    private int id;
    private String name;

    public TimeRange(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
