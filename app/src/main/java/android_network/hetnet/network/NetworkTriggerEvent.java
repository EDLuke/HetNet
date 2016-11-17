package android_network.hetnet.network;

import java.util.Date;

import android_network.hetnet.common.trigger_events.TriggerEvent;

public class NetworkTriggerEvent extends TriggerEvent {
  public NetworkTriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    super(eventOriginator, eventName, timeOfEvent);
  }
}
