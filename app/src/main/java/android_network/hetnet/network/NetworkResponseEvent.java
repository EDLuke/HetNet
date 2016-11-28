package android_network.hetnet.network;

import java.util.Date;

public class NetworkResponseEvent {
  private String eventOriginator;
  private NetworkList listOfNetworks;
  private Date timeOfEvent;

  public NetworkResponseEvent(String eventOriginator, String listOfNetworks, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.listOfNetworks = new NetworkList();
    this.listOfNetworks.setListOfNetworks(listOfNetworks);
    this.timeOfEvent = timeOfEvent;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public NetworkList getNetworkList() {
    return listOfNetworks;
  }

  public void setListOfNetworks(String listOfNetworks) {
    this.listOfNetworks.setListOfNetworks(listOfNetworks);
  }

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }
}
