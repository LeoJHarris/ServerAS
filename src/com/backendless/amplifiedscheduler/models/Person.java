package com.backendless.amplifiedscheduler.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonard on 18/07/2015.
 */
public class Person {

    //TODO make private for all classes
    public List<Person> contacts;
    public String objectId;
    public List<Person> personsImRequesting;
    public List<Person> personsRequestingMe;
    public List<Slot> pendingResponseSlot;
    public List<Slot> myCreatedSlot;
    public List<Slot> goingToSlot;
    public List<Plan> myPlans;

    public List<Person> getPersonsRequestingMe() {
        return personsRequestingMe;
    }

    public List<Person> getPersonsImRequesting() {
        return personsImRequesting;
    }

    public void addSlotGoingToSlot(Slot slotItem) {
        if (goingToSlot == null)
            goingToSlot = new ArrayList<Slot>();
        goingToSlot.add(slotItem);
    }

    public List<Plan> getMyPlans() {
        return myPlans;
    }

    public void addContacts(Person person) {
        if (contacts == null)
            contacts = new ArrayList<Person>();
        contacts.add(person);
    }

    public List<Slot> getGoingToSlot() {
        return goingToSlot;
    }

    public List<Person> getContacts() {
        return contacts;
    }

    public String getObjectId() {
        return objectId;
    }

}

