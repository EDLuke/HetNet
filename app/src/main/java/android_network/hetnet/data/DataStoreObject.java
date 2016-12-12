package android_network.hetnet.data;

import java.io.Serializable;
import java.util.List;

import android_network.hetnet.system.SystemList;

public class DataStoreObject implements Serializable {
  /*System Manager*/
  private String applicationID;
  private String applicationType;
  private SystemList systemList;

  private double latitude;
  private double longitude;
  private List<Network> listOfNetworks;

  public DataStoreObject(String applicationID, String applicationType, double latitude, double longitude, List<Network> listOfNetworks) {
    this.applicationID = applicationID;
    this.applicationType = applicationType;
    this.latitude = latitude;
    this.longitude = longitude;
    this.listOfNetworks = listOfNetworks;
  }

  public String getApplicationID() {
    return applicationID;
  }

  public void setApplicationID(String applicationID) {
    this.applicationID = applicationID;
  }

  public String getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public List<Network> getListOfNetworks() {
    return listOfNetworks;
  }

  public void setListOfNetworks(List<Network> listOfNetworks) {
    this.listOfNetworks = listOfNetworks;
  }

  public SystemList getSystemList() {
    return systemList;
  }

  public void setSystemList(SystemList systemList) {
    this.systemList = systemList;
  }
}
