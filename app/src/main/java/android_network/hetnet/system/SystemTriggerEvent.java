package android_network.hetnet.system;

import java.util.Date;

import android_network.hetnet.common.trigger_events.TriggerEvent;

/**
 * SystemTriggerEvent
 */

public class SystemTriggerEvent extends TriggerEvent {
  public SystemTriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    super(eventOriginator, eventName, timeOfEvent);
  }
}
