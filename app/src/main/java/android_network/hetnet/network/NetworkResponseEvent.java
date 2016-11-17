package android_network.hetnet.network;

public class NetworkResponseEvent {
  private String eventOriginator;
  private String listOfNetworks;

  public NetworkResponseEvent(String eventOriginator, String listOfNetworks) {
    this.eventOriginator = eventOriginator;
    this.listOfNetworks = listOfNetworks;
  }

  public String getEventOriginator() {
    return eventOriginator;
  }

  public void setEventOriginator(String eventOriginator) {
    this.eventOriginator = eventOriginator;
  }

  public String getListOfNetworks() {
    return listOfNetworks;
  }

  public void setListOfNetworks(String listOfNetworks) {
    this.listOfNetworks = listOfNetworks;
  }
}
