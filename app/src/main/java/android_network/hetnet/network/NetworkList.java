package android_network.hetnet.network;

import java.util.List;

import android_network.hetnet.common.EventList;
import android_network.hetnet.data.Network;

public class NetworkList extends EventList {
  private List<Network> listOfNetworks;

  //TODO: toString()
  @Override
  public String toString(){
    return "";
  }

  public List<Network> getListOfNetworks() {
    return listOfNetworks;
  }

  public void setListOfNetworks(List<Network> listOfNetworks) {
    this.listOfNetworks = listOfNetworks;
  }
}
