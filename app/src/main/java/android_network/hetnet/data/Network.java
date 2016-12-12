package android_network.hetnet.data;

import java.io.Serializable;

import static android.R.id.list;

public class Network implements Serializable, Cloneable {
  private String networkSSID;
  private double bandwidth;
  private double signalStrength;
  private double signalFrequency;
  private String securityProtocol;
  private boolean possibleToConnect;
  private double timeToConnect;
  private double cost;
  private boolean currentNetwork;
  private String speed;

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

  public double getTimeToConnect() {return timeToConnect;}

  // time data not available -> timeToConnect = -1
  public void setTimeToConnect(long timeToConnect) {
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

  public double getSpeed(double speed) { return speed;
  }

  public String setSpeed(String speed) { return this.speed = speed;
  }

  @Override
  public String toString() {

    String cp = "";

    if (possibleToConnect == true)
    {
      cp = "Yes";
    }
    else
    {
      cp = "No";
    }

    String cn ="";

    if (currentNetwork == true)
    {
      cn = "Yes";
    }
    else
    {
      cn = "No";
    }


    String list = "\n" + networkSSID +
      "\n\t\t\t\tSignal Strength: "+ signalStrength + " dB" +
      "\n\t\t\t\tConnection Possible: "+ cp +
      "\n\t\t\t\tConnection Time: "+ timeToConnect +" ns" +
      "\n\t\t\t\tBandwidth: "+ bandwidth +" Mbps" +
      "\n\t\t\t\tSpeed: "+ speed +
      "\n\t\t\t\tSecurity Protocol: "+ securityProtocol +
      "\n\t\t\t\tCost: $"+ cost +
      "\n\t\t\t\tCurrent Network: "+ cn +
      "\n\t\t\t\tSignal Frequency: "+ signalFrequency ;
    
    return list;

  }

  public Network getCopy() throws CloneNotSupportedException {
    return (Network) this.clone();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


}
