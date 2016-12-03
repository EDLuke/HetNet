package android_network.hetnet.location;

import android.location.Location;

import java.util.Date;

public class LocationResponseEvent {
  private String eventOriginator;
  private Location location;
  private Date timeOfEvent;

  public LocationResponseEvent(String eventOriginator, Location location, Date timeOfEvent) {
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

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }
}
