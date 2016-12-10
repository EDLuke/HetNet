package android_network.hetnet.data;

public class PolicyVector {
  private String applicationId;
  private String applicationType;
  private double latitude;
  private double longitude;
  private String networkSSID;
  private double bandwidth;
  private double signalStrength;
  private double signalFrequency;
  private double timeToConnect;
  private double cost;

  public PolicyVector() {
    super();
  }

  public PolicyVector(String applicationId, String applicationType, double latitude, double longitude, String networkSSID,
                      double bandwidth, double signalStrength, double signalFrequency, double timeToConnect, double cost) {
    this.applicationId = applicationId;
    this.applicationType = applicationType;
    this.latitude = latitude;
    this.longitude = longitude;
    this.networkSSID = networkSSID;
    this.bandwidth = bandwidth;
    this.signalStrength = signalStrength;
    this.signalFrequency = signalFrequency;
    this.timeToConnect = timeToConnect;
    this.cost = cost;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
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
