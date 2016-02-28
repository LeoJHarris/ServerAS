package com.backendless.amplifiedscheduler.models;

import com.backendless.geo.GeoPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Leonard on 25/04/2015.
 */

public class Slot {

    public Date startdate;
    public Date enddate;
    public boolean alldayevent;
    public String subject;
    public String message;
    public List<Person> attendees;
    public Integer maxattendees;
    public String objectId;
    public GeoPoint location;
    public String phone;
    public String note;
    public String ownername;

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public void setAlldayevent(Boolean alldayevent) {
        this.alldayevent = alldayevent;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getEndCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(enddate);
        return cal;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public List<Person> getAttendees() {
        return attendees;
    }

    public void addAttendee(Person person) {
        if (attendees == null)
            attendees = new ArrayList<Person>();
        attendees.add(person);
    }

    public void setMaxattendees(Integer maxattendees) {
        this.maxattendees = maxattendees;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setSubject(String title) {
        this.subject = title;
    }
}