package com.backendless.amplifiedscheduler.events.custom_events;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.amplifiedscheduler.models.Person;
import com.backendless.amplifiedscheduler.models.Plan;
import com.backendless.amplifiedscheduler.models.Slot;
import com.backendless.geo.GeoPoint;
import com.backendless.servercode.RunnerContext;
import com.backendless.servercode.annotation.Async;
import com.backendless.servercode.annotation.BackendlessEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * CreateEventEventHandler handles custom event "CreateEvent". This is accomplished with the
 * BackendlessEvent( "CreateEvent" ) annotation. The event can be raised by either
 * the client-side or the server-side code (in other event handlers or timers).
 * The name of the class is not significant, it can be changed, since the event
 * handler is associated with the event only through the annotation.
 */
@BackendlessEvent("CreateEvent")
public class CreateEventEventHandler extends com.backendless.servercode.extension.CustomEventHandler {

  @Async
  @Override
  public Map handleEvent(RunnerContext context, Map eventArgs) {


    if (eventArgs.get("id").equals("event")) {

      IDataStore<Slot> iDataStoreEvents = Backendless.Data.of(Slot.class);
      IDataStore<Person> iDataStorePersons = Backendless.Data.of(Person.class);

      Person personContact;
      Person loggedinperson;

      Slot slot = new Slot();

      slot.setSubject((String) eventArgs.get("subject"));
      slot.setMessage((String) eventArgs.get("message"));
      slot.setStartdate((Date) eventArgs.get("starttime"));
      slot.setEnddate((Date) eventArgs.get("endtime"));
      slot.setMaxattendees((Integer) eventArgs.get("attendees"));
      slot.setPhone((String) eventArgs.get("phone"));
      slot.setAlldayevent((Boolean) eventArgs.get("alldayevent"));
      slot.setNote((String) eventArgs.get("note"));
      slot.setOwnername((String) eventArgs.get("host"));
      String eventCategory = (String) eventArgs.get("category");
      CharSequence place = (CharSequence) eventArgs.get("location");
      double alat = (Double) eventArgs.get("lat");
      double aLong = (Double) eventArgs.get("long");

      GeoPoint geoPlace = new GeoPoint(alat, aLong);

      geoPlace.addCategory("FriendEvents");

//        Map<String, Object> locationMap = new HashMap();
//        locationMap.put("location", place);
//        geoPlace.setMetadata(locationMap);

      //Map<String, Object> placeMap = new HashMap<String, Object>(); // Added this need to get this a metadata
      // placeMap.put();
      geoPlace.addMetadata("address", place.toString());
      geoPlace.addMetadata("category", eventCategory);

      Slot savedSlot = iDataStoreEvents.save(slot);

      ArrayList<String> contactRelationProps = new ArrayList();
      contactRelationProps.add("pendingResponseSlot");

      Integer size = (Integer) eventArgs.get("size");


      for (int j = 0; j < size; j++) {

        personContact = iDataStorePersons.findById((String) eventArgs.get(String.valueOf(j)), contactRelationProps);

        personContact.pendingResponseSlot.add(savedSlot);

        iDataStorePersons.save(personContact);
      }

      ArrayList<String> relationProps = new ArrayList();
      relationProps.add("myCreatedSlot");

      //Map<String, Object> metaMap = new HashMap();
      // metaMap.put("slot", savedSlot);
      //geoPlace.setMetadata(metaMap);

      savedSlot.setLocation(geoPlace);

      loggedinperson = iDataStorePersons.findById((String) eventArgs.get("loggedinperson"), relationProps);

      loggedinperson.myCreatedSlot.add(savedSlot);

      iDataStorePersons.save(loggedinperson);
    } else if (eventArgs.get("id").equals("plan")) {

      IDataStore<Plan> iDataStorePlans = Backendless.Data.of(Plan.class);
      IDataStore<Person> iDataStorePersons = Backendless.Data.of(Person.class);

      Person loggedinperson;

      Plan plan = new Plan();

      plan.setSubject((String) eventArgs.get("subject"));
      plan.setStartdate((Date) eventArgs.get("starttime"));
      plan.setEnddate((Date) eventArgs.get("endtime"));
      plan.setAlldayevent((Boolean) eventArgs.get("alldayevent"));
      plan.setNote((String) eventArgs.get("note"));

      String eventCategory = (String) eventArgs.get("category");
      CharSequence place = (CharSequence) eventArgs.get("location");
      double alat = (Double) eventArgs.get("lat");
      double aLong = (Double) eventArgs.get("long");

      GeoPoint geoPlace = new GeoPoint(alat, aLong);

      geoPlace.addCategory("FriendEvents");

      geoPlace.addMetadata("address", place.toString());
      geoPlace.addMetadata("category", eventCategory);

      Plan savedPlan = iDataStorePlans.save(plan);

      ArrayList<String> relationProps = new ArrayList();
      relationProps.add("myPlans");

      savedPlan.setLocation(geoPlace);

      loggedinperson = iDataStorePersons.findById((String) eventArgs.get("loggedinperson"), relationProps);

      loggedinperson.getMyPlans().add(savedPlan);

      iDataStorePersons.save(loggedinperson);
    }
    return Collections.emptyMap();
  }
}
        