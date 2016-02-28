package com.backendless.amplifiedscheduler.events.custom_events;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.amplifiedscheduler.models.Person;
import com.backendless.amplifiedscheduler.models.Slot;
import com.backendless.servercode.RunnerContext;
import com.backendless.servercode.annotation.Async;
import com.backendless.servercode.annotation.BackendlessEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ManageEventEventHandler handles custom event "ManageEvent". This is accomplished with the
 * BackendlessEvent( "ManageEvent" ) annotation. The event can be raised by either
 * the client-side or the server-side code (in other event handlers or timers).
 * The name of the class is not significant, it can be changed, since the event
 * handler is associated with the event only through the annotation.
 */
@BackendlessEvent("ManageEvent")
public class ManageEventEventHandler extends com.backendless.servercode.extension.CustomEventHandler {

  @Async
  @Override
  public Map handleEvent(RunnerContext context, Map eventArgs) {

    //Delete an event which is one of the logged in users events
    if (eventArgs.get("id").equals("deleteevent")) {

      IDataStore<Slot> iDataStoreEvents = Backendless.Data.of(Slot.class);
      Slot slot;
      String eventObjectId = (String) eventArgs.get("event");

      slot = iDataStoreEvents.findById(eventObjectId);

      if (slot.getLocation() != null) {
        Backendless.Geo.removePoint(slot.getLocation());
      }
      iDataStoreEvents.remove(slot);
    }

    //Remove event that I was going to
    if (eventArgs.get("id").equals("removeevent")) {

      IDataStore<Slot> iDataStoreEvents = Backendless.Data.of(Slot.class);
      IDataStore<Person> iDataStorePerson = Backendless.Data.of(Person.class);

      String personObjectId = (String) eventArgs.get("objectIdPerson");
      String eventObjectId = (String) eventArgs.get("event");

      List<String> relations = new ArrayList<String>();
      relations.add("goingToSlot");
      Person person = iDataStorePerson.findById(personObjectId, relations);

      List<String> relationsSlot = new ArrayList<String>();
      relations.add("attendees");
      Slot event = iDataStoreEvents.findById(eventObjectId, relationsSlot);

      int posAttendees = 0;

      for (int i = 0; i < event.getAttendees().size(); i++) {

        if (event.getAttendees().get(i).getObjectId().equals(personObjectId)) {
          posAttendees = i;
          break;
        }
      }

      int pos = 0;

      for (int i = 0; i < person.getGoingToSlot().size(); i++) {

        if (person.getGoingToSlot().get(i).getObjectId().equals(eventObjectId)) {
          pos = i;
          break;
        }
      }

      event.getAttendees().remove(posAttendees);

      iDataStoreEvents.save(event);

      person.getGoingToSlot().remove(pos);

      iDataStorePerson.save(person);
    }

    // Not going to invited Event
    if (eventArgs.get("id").equals("declineinviteevent")) {

      IDataStore<Person> iDataStorePerson = Backendless.Data.of(Person.class);

      String personObjectId = (String) eventArgs.get("objectIdPerson");
      String eventObjectId = (String) eventArgs.get("event");

      List<String> relations = new ArrayList<String>();
      relations.add("pendingResponseSlot");
      Person person = iDataStorePerson.findById(personObjectId, relations);

      int pos = 0;

      for (int i = 0; i < person.pendingResponseSlot.size(); i++) {

        if (person.pendingResponseSlot.get(i).getObjectId().equals(eventObjectId)) {
          pos = i;
          break;
        }
      }
      person.pendingResponseSlot.remove(pos);
      iDataStorePerson.save(person);
    }
    // Going to invited event
    if (eventArgs.get("id").equals("acceptinviteevent")) {

      IDataStore<Slot> iDataStoreEvents = Backendless.Data.of(Slot.class);
      IDataStore<Person> iDataStorePerson = Backendless.Data.of(Person.class);

      String personObjectId = (String) eventArgs.get("objectIdPerson");
      String eventObjectId = (String) eventArgs.get("event");

      List<String> relations = new ArrayList<String>();
      relations.add("pendingResponseSlot");
      Person person = iDataStorePerson.findById(personObjectId, relations);

      List<String> relationsSlot = new ArrayList<String>();
      relations.add("attendees");
      Slot attendeesToSlot = Backendless.Data.of(Slot.class).findById(eventObjectId, relationsSlot);

      int pos = 0;

      for (int i = 0; i < person.pendingResponseSlot.size(); i++) {

        if (person.pendingResponseSlot.get(i).getObjectId().equals(eventObjectId)) {
          pos = i;
          break;
        }
      }

      person.pendingResponseSlot.remove(pos);
      person.addSlotGoingToSlot(attendeesToSlot);

      iDataStorePerson.save(person);

      attendeesToSlot.addAttendee(person);

      iDataStoreEvents.save(attendeesToSlot);
    }
    return Collections.emptyMap();
  }
}
        