package android_network.hetnet.location;

public class LocationRequestEvent {
  private String eventOriginator;

  public LocationRequestEvent(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }
}
