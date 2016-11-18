package android_network.hetnet.common.trigger_events;

import java.util.Date;

import android_network.hetnet.system.SystemList;

public class UITriggerEvent {
  private String eventOriginator;
  private String eventName;
  private SystemList eventSystemList;
  private Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, String eventName, SystemList eventSystemList, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.eventName = eventName;
    this.timeOfEvent = timeOfEvent;
    this.setEventSystemList(eventSystemList);
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

  public SystemList getEventSystemList() {
    return eventSystemList;
  }

  public void setEventSystemList(SystemList eventSystemList) {
    this.eventSystemList = eventSystemList;
  }
}
