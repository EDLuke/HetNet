package android_network.hetnet.network;

import android_network.hetnet.common.EventList;

public class NetworkList extends EventList {
  private String listOfNetworks;

  @Override
  public String toString(){
    return getListOfNetworks();
  }

  public String getListOfNetworks() {
    return listOfNetworks;
  }

  public void setListOfNetworks(String listOfNetworks) {
    this.listOfNetworks = listOfNetworks;
  }
}
