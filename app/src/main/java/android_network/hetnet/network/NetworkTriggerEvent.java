package android_network.hetnet.network;

import java.util.Date;

import android_network.hetnet.trigger_events.TriggerEvent;

/**
 * Created by abroy240484 on 11/17/16.
 */

public class NetworkTriggerEvent extends TriggerEvent {
  public NetworkTriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    super(eventOriginator, eventName, timeOfEvent);
  }
}
