package com.backendless.amplifiedscheduler.models;

import com.backendless.geo.GeoPoint;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leonard on 09-Feb-16.
 */
public class Plan {
    public Date startdate;
    public Date enddate;
    public boolean alldayevent;
    public String subject;
    public String objectId;
    public GeoPoint location;
    public String note;

    public Calendar getEndCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(enddate);
        return cal;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setAlldayevent(boolean alldayevent) {
        this.alldayevent = alldayevent;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
