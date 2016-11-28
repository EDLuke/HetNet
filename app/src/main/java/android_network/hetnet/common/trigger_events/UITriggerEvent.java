package android_network.hetnet.common.trigger_events;

import java.util.Date;

import android_network.hetnet.common.EventList;
import android_network.hetnet.system.SystemList;

public class UITriggerEvent {
  private String eventOriginator;
  private EventList eventList;
  private Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, EventList eventList, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;

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
