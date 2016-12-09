package android_network.hetnet.common.trigger_events;

import java.util.Date;

public class UITriggerEvent {
  private String eventOriginator;
  private Object eventName;
  private Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, Object eventName, Date timeOfEvent) {
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

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }

  public Object getEventName() {
    return eventName;
  }

  public void setEventName(Object eventName) {
    this.eventName = eventName;
  }
}
