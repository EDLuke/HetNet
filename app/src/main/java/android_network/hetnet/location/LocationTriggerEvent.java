package android_network.hetnet.location;

import java.util.Date;

import android_network.hetnet.common.trigger_events.TriggerEvent;

/**
 * Created by abroy240484 on 11/17/16.
 */

public class LocationTriggerEvent extends TriggerEvent {
  public LocationTriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    super(eventOriginator, eventName, timeOfEvent);
  }
}
