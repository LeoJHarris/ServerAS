package com.backendless.amplifiedscheduler.timers;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.amplifiedscheduler.models.Plan;
import com.backendless.amplifiedscheduler.models.Slot;
import com.backendless.servercode.annotation.BackendlessTimer;

import java.util.Calendar;
import java.util.Iterator;

/**
 * RemoveexpiredeventsTimer is a timer.
 * It is executed according to the schedule defined in Backendless Console. The
 * class becomes a timer by extending the TimerExtender class. The information
 * about the timer, its name, schedule, expiration date/time is configured in
 * the special annotation - BackendlessTimer. The annotation contains a JSON
 * object which describes all properties of the timer.
 */
@BackendlessTimer("{'startDate':1448857860000,'frequency':{'schedule':'daily','repeat':{'every':1}},'timername':'removeexpiredevents'}")
public class RemoveexpiredeventsTimer extends com.backendless.servercode.extension.TimerExtender {

  @Override
  public void execute(String appVersionId) throws Exception {

    Calendar minDate = Calendar.getInstance();

    IDataStore<Slot> iDataStoreEvents = Backendless.Data.of(Slot.class);

    BackendlessCollection<Slot> events = iDataStoreEvents.find();

    Iterator<Slot> iterator = events.getCurrentPage().iterator();

    while (iterator.hasNext()) {
      Slot slot = iterator.next();

      if (slot.getEndCalendar().before(minDate)) {
        Backendless.Geo.removePoint(slot.getLocation());
        iDataStoreEvents.remove(slot);
      }

      IDataStore<Plan> iDataStorePlans = Backendless.Data.of(Plan.class);

      BackendlessCollection<Plan> plans = iDataStorePlans.find();

      Iterator<Plan> iteratorPlans = plans.getCurrentPage().iterator();

      while (iterator.hasNext()) {
        Plan plan = iteratorPlans.next();

        if (plan.getEndCalendar().before(minDate)) {
          Backendless.Geo.removePoint(plan.getLocation());
          iDataStorePlans.remove(plan);
        }
      }
    }
  }
}
