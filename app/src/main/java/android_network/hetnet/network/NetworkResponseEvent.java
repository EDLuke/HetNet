package android_network.hetnet.network;

import java.util.Date;
import java.util.List;

import android_network.hetnet.data.Network;

public class NetworkResponseEvent {
  private String eventOriginator;
  private List<Network> listOfNetworks;
  private Date timeOfEvent;

  public NetworkResponseEvent(String eventOriginator, List<Network> listOfNetworks, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.listOfNetworks = listOfNetworks;
    this.timeOfEvent = timeOfEvent;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public List<Network> getListOfNetworks() {
    return listOfNetworks;
  }

  public void setListOfNetworks(List<Network> listOfNetworks) {
    this.listOfNetworks = listOfNetworks;
  }

  public Date getTimeOfEvent() {
    return timeOfEvent;
  }

  public void setTimeOfEvent(Date timeOfEvent) {
    this.timeOfEvent = timeOfEvent;
  }
}
