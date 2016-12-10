package android_network.hetnet.common.trigger_events;

import java.util.Date;

public class UITriggerEvent {
  private String eventOriginator;
  private Object event;
  private Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, Object event, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.event = event;
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

  public Object getEvent() {
    return event;
  }

  public void setEvent(Object event) {
    this.event = event;
  }
}
