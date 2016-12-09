package android_network.hetnet.data;

import java.io.Serializable;

public class Network implements Serializable {
  private String networkSSID;
  private double bandwidth;
  private double signalStrength;
  private double signalFrequency;
  private String securityProtocol;
  private boolean possibleToConnect;
  private double timeToConnect;
  private double cost;
  private boolean currentNetwork;

  public String getNetworkSSID() {
    return networkSSID;
  }

  public void setNetworkSSID(String networkSSID) {
    this.networkSSID = networkSSID;
  }

  public double getBandwidth() {
    return bandwidth;
  }

  public void setBandwidth(double bandwidth) {
    this.bandwidth = bandwidth;
  }

  public double getSignalStrength() {
    return signalStrength;
  }

  public void setSignalStrength(double signalStrength) {
    this.signalStrength = signalStrength;
  }

  public double getSignalFrequency() {
    return signalFrequency;
  }

  public void setSignalFrequency(double signalFrequency) {
    this.signalFrequency = signalFrequency;
  }

  public String getSecurityProtocol() {
    return securityProtocol;
  }

  public void setSecurityProtocol(String securityProtocol) {
    this.securityProtocol = securityProtocol;
  }

  public boolean isPossibleToConnect() {
    return possibleToConnect;
  }

  public void setPossibleToConnect(boolean possibleToConnect) {
    this.possibleToConnect = possibleToConnect;
  }

  public double getTimeToConnect() {
    return timeToConnect;
  }

  public void setTimeToConnect(double timeToConnect) {
    this.timeToConnect = timeToConnect;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public boolean isCurrentNetwork() {
    return currentNetwork;
  }

  public void setCurrentNetwork(boolean currentNetwork) {
    this.currentNetwork = currentNetwork;
  }

  @Override
  public String toString() {
    return "\n\nNetwork{" +
      "networkSSID='" + networkSSID + '\'' +
      ", bandwidth=" + bandwidth +
      ", signalStrength=" + signalStrength +
      ", signalFrequency=" + signalFrequency +
      ", securityProtocol='" + securityProtocol + '\'' +
      ", possibleToConnect=" + possibleToConnect +
      ", timeToConnect=" + timeToConnect +
      ", cost=" + cost +
      ", currentNetwork=" + currentNetwork +
      "}" + "\n";
  }

}
