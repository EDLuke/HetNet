package android_network.hetnet.common.trigger_events;

import java.util.Date;

public class TriggerEvent {
  public String eventOriginator;
  public String eventName;
  public Date timeOfEvent;

  public TriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.eventName = eventName;
    this.timeOfEvent = timeOfEvent;
  }
}