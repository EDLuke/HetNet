package android_network.hetnet.trigger_events;

import java.util.Date;

/**
 * Created by lukez_000 on 11/02/2016.
 */
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