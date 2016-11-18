package android_network.hetnet.location;

import java.util.Date;

public class LocationResponseEvent {
  private String eventOriginator;
  private String location;
  private Date timeOfEvent;

  public LocationResponseEvent(String eventOriginator, String location, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.location = location;
    this.timeOfEvent = timeOfEvent;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }
}
