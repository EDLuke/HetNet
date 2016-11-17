package android_network.hetnet.network;

public class NetworkRequestEvent {
  private String eventOriginator;

  public NetworkRequestEvent(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }
}
