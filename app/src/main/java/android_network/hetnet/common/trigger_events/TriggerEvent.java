package android_network.hetnet.common.trigger_events;

import java.util.Date;

public class TriggerEvent {
  private String eventOriginator;
  private String eventName;
  private Date timeOfEvent;

  public TriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.eventName = eventName;
    this.timeOfEvent = timeOfEvent;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }

  @Override
  public String toString() {
    return eventName + "\t" + eventOriginator + "\t" + timeOfEvent.toString();
  }
}