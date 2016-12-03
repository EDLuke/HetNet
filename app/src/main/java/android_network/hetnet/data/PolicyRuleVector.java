package android_network.hetnet.data;

import android.location.Location;

public class PolicyRuleVector {
  private String applicationId;
  private Location location;
  private String networkSSID;
  private double bandwidth;
  private double signalStrength;
  private double signalFrequency;
  private double timeToConnect;
  private double cost;

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

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
}
