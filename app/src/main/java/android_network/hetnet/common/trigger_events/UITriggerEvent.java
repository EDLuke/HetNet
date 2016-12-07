package android_network.hetnet.common.trigger_events;

import java.util.Date;

import android_network.hetnet.common.EventList;
import android_network.hetnet.system.SystemList;

public class UITriggerEvent {
  private String eventOriginator;
  private EventList eventList;
  private String eventName;
  private Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, String eventName, EventList eventList, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.eventName = eventName;
    this.timeOfEvent = timeOfEvent;
    this.setEventList(eventList);
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

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public EventList getEventList() {
    return eventList;
  }

  public void setEventList(EventList eventSystemList) {
    this.eventList = eventSystemList;
  }

  @Override
  public String toString(){
    return eventOriginator + "\t" + eventList.toString() + timeOfEvent.toString();
  }
}
