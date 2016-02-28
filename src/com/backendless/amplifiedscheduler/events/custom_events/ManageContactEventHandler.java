package com.backendless.amplifiedscheduler.events.custom_events;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.amplifiedscheduler.models.Person;
import com.backendless.servercode.RunnerContext;
import com.backendless.servercode.annotation.Async;
import com.backendless.servercode.annotation.BackendlessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ManageContactEventHandler handles custom event "ManageContact". This is accomplished with the
 * BackendlessEvent( "ManageContact" ) annotation. The event can be raised by either
 * the client-side or the server-side code (in other event handlers or timers).
 * The name of the class is not significant, it can be changed, since the event
 * handler is associated with the event only through the annotation.
 */
@BackendlessEvent("ManageContact")
public class ManageContactEventHandler extends com.backendless.servercode.extension.CustomEventHandler {

  @Async
  @Override
  public Map handleEvent(RunnerContext context, Map eventArgs) {


    IDataStore<Person> iDataStore = Backendless.Data.of(Person.class);

    if (eventArgs.get("id").equals("removeContact")) {

      Boolean personFound = false;
      Boolean otherPersonFound = false;

      Person personLoggedIn;
      Person personOther;

      List<String> relations = new ArrayList<String>();
      relations.add("contacts");
      List<String> relations1 = new ArrayList<String>();
      relations1.add("contacts");
      personLoggedIn = iDataStore.findById((String) eventArgs.get("loggedinperson"), relations1);
      personOther = iDataStore.findById((String) eventArgs.get("otherperson"), relations);

      int pos1 = 0;

      for (int i = 0; i < personLoggedIn.getContacts().size(); i++) {

        if (personLoggedIn.getContacts().get(i).getObjectId().equals(personOther.getObjectId())) {
          pos1 = i;
          otherPersonFound = true;
          break;
        }
      }

      int pos = 0;

      for (int i = 0; i < personOther.getContacts().size(); i++) {

        if (personOther.getContacts().get(i).getObjectId().equals(personLoggedIn.getObjectId())) {
          pos = i;
          personFound = true;
          break;
        }
      }
      if (otherPersonFound) {
        personLoggedIn.getContacts().remove(pos1);
        iDataStore.save(personLoggedIn);
      }
      if (personFound) {
        personOther.getContacts().remove(pos);
        iDataStore.save(personOther);
      }

    } else if (eventArgs.get("id").equals("declineRequest")) {

      Boolean personFound = false;
      Boolean otherPersonFound = false;

      Person personLoggedIn;
      Person personOther;

      List<String> relationsRequestingMe = new ArrayList<String>();
      relationsRequestingMe.add("personsRequestingMe");
      personLoggedIn = iDataStore.findById((String) eventArgs.get("loggedinperson"), relationsRequestingMe);
      List<String> relationsImRequesting = new ArrayList<String>();
      relationsImRequesting.add("personsImRequesting");
      personOther = iDataStore.findById((String) eventArgs.get("otherperson"), relationsImRequesting);

      int pos1 = 0;

      for (int i = 0; i < personLoggedIn.personsRequestingMe.size(); i++) {

        if (personLoggedIn.personsRequestingMe.get(i).getObjectId().equals(personOther.getObjectId())) {
          pos1 = i;
          otherPersonFound = true;
          break;
        }
      }

      int pos = 0;

      for (int i = 0; i < personOther.personsImRequesting.size(); i++) {

        if (personOther.personsImRequesting.get(i).getObjectId().equals(personLoggedIn.getObjectId())) {
          pos = i;
          personFound = true;
          break;
        }
      }

      if (personFound) {
        personOther.personsImRequesting.remove(pos);
      }
      if (otherPersonFound) {
        personLoggedIn.personsRequestingMe.remove(pos1);
        iDataStore.save(personLoggedIn);
      }
      if (personFound) {
        iDataStore.save(personOther);
      }

    } else if (eventArgs.get("id").equals("acceptContactRequest")) {

      List<String> relationsRequestingMe = new ArrayList<String>();
      relationsRequestingMe.add("personsRequestingMe");
      relationsRequestingMe.add("contacts");
      Person personLoggedIn = iDataStore.findById((String) eventArgs.get("loggedinperson"), relationsRequestingMe);
      List<String> relationsImRequesting = new ArrayList<String>();
      relationsImRequesting.add("personsImRequesting");
      relationsImRequesting.add("contacts");
      Person personOther = iDataStore.findById((String) eventArgs.get("otherperson"), relationsImRequesting);

      for (int i = 0; i < personLoggedIn.getPersonsRequestingMe().size(); i++) {

        if (personLoggedIn.getPersonsRequestingMe().get(i).getObjectId().equals(eventArgs.get("otherperson"))) {
          personLoggedIn.getPersonsRequestingMe().remove(i);
          personLoggedIn.getContacts().add(personOther);
          iDataStore.save(personLoggedIn);
          break;
        }
      }

      for (int i = 0; i < personOther.getPersonsImRequesting().size(); i++) {

        if (personOther.getPersonsImRequesting().get(i).getObjectId().equals(eventArgs.get("loggedinperson"))) {
          personOther.getPersonsImRequesting().remove(i);
          personOther.getContacts().add(personLoggedIn);
          iDataStore.save(personOther);
          break;
        }
      }

    } else if (eventArgs.get("id").equals("sendContactInvite")) {

      Boolean imAmAlreadyBeingRequested = false;
      Boolean personFound = false;

      Person personLoggedIn;
      Person personOther;

      List<String> relationsRequestingMe = new ArrayList<String>();
      relationsRequestingMe.add("personsRequestingMe");
      relationsRequestingMe.add("contacts");
      relationsRequestingMe.add("personsImRequesting");
      personLoggedIn = iDataStore.findById((String) eventArgs.get("loggedinperson"), relationsRequestingMe);
      List<String> relationsImRequesting = new ArrayList<String>();
      relationsImRequesting.add("personsImRequesting");
      relationsImRequesting.add("contacts");
      relationsImRequesting.add("personsRequestingMe");
      personOther = iDataStore.findById((String) eventArgs.get("otherperson"), relationsImRequesting);

      int pos1 = 0;

      for (int i = 0; i < personLoggedIn.personsRequestingMe.size(); i++) {

        if (personLoggedIn.personsRequestingMe.get(i).getObjectId().equals(eventArgs.get("otherperson"))) {
          pos1 = i;
          imAmAlreadyBeingRequested = true;
        }
      }
      int pos = 0;

      for (int i = 0; i < personOther.personsImRequesting.size(); i++) {

        if (personOther.personsImRequesting.get(i).getObjectId().equals(eventArgs.get("loggedinperson"))) {
          pos = i;
          personFound = true;
          break;
        }
      }

      if (imAmAlreadyBeingRequested || personFound) {
        if (imAmAlreadyBeingRequested) {
          personLoggedIn.contacts.add(personOther);
          personLoggedIn.personsRequestingMe.remove(pos1);
        }
        // Not being requested in our list but other
        else {
          personLoggedIn.contacts.add(personOther);
        }
        // I am being requested in his list
        if (personFound) {
          personOther.contacts.add(personLoggedIn);
          personOther.personsImRequesting.remove(pos);
        } else {
          personOther.contacts.add(personLoggedIn);
        }
        if (imAmAlreadyBeingRequested) {
          iDataStore.save(personLoggedIn);
        }
        if (personFound) {
          iDataStore.save(personOther);
        }
      } else {

        personOther.getPersonsRequestingMe().add(personLoggedIn);
        iDataStore.save(personOther);
        personLoggedIn.getPersonsImRequesting().add(personOther);
        iDataStore.save(personLoggedIn);

      }
    } else if (eventArgs.get("id").equals("cancelContactInvite")) {

      Person personLoggedIn;
      Person personOther;

      Boolean personFoundContact = false;
      Boolean otherPersonFoundContact = false;

      List<String> relations = new ArrayList<String>();
      relations.add("personsRequestingMe");
      relations.add("personsImRequesting");
      relations.add("contacts");
      personLoggedIn = iDataStore.findById((String) eventArgs.get("loggedinperson"), relations);

      int pos1Contact = 0;

      for (int i = 0; i < personLoggedIn.contacts.size(); i++) {

        if (personLoggedIn.contacts.get(i).getObjectId().equals(eventArgs.get("otherperson"))) {
          pos1Contact = i;
          otherPersonFoundContact = true;
          break;
        }
      }

      if (otherPersonFoundContact) {
        personLoggedIn.contacts.remove(pos1Contact);
        iDataStore.save(personLoggedIn);
      }

      // Remove from other
      List<String> relations1 = new ArrayList<String>();
      relations1.add("personsRequestingMe");
      relations1.add("contacts");
      personOther = iDataStore.findById((String) eventArgs.get("otherperson"), relations1);

      int posContact = 0;

      for (int i = 0; i < personOther.contacts.size(); i++) {

        if (personOther.contacts.get(i).getObjectId().equals(personLoggedIn.getObjectId())) {
          posContact = i;
          personFoundContact = true;
          break;
        }
      }

      if (personFoundContact) {
        personOther.getContacts().remove(posContact);
        iDataStore.save(personOther);
      }


      // Just remove from requesting
      if ((!otherPersonFoundContact) || (!personFoundContact)) {

        Boolean personFoundRequesting = false;
        Boolean otherPersonFoundRequested = false;

        int pos1 = 0;

        for (int i = 0; i < personLoggedIn.personsImRequesting.size(); i++) {

          if (personLoggedIn.getPersonsImRequesting().get(i).getObjectId().equals(eventArgs.get("otherperson"))) {
            pos1 = i;
            personFoundRequesting = true;
            break;
          }
        }
        if (personFoundRequesting) {
          personLoggedIn.getPersonsImRequesting().remove(pos1);
          iDataStore.save(personLoggedIn);
        }

        int pos = 0;

        for (int i = 0; i < personOther.personsRequestingMe.size(); i++) {

          if (personOther.personsRequestingMe.get(i).getObjectId().equals(eventArgs.get("loggedinperson"))) {
            pos = i;
            otherPersonFoundRequested = true;
            break;
          }
        }
        if (otherPersonFoundRequested) {
          personOther.getPersonsRequestingMe().remove(pos);
          iDataStore.save(personOther);
        }
      }
    } else if (eventArgs.get("id").equals("updateAccount")) {

    }
    return eventArgs;
  }

}
