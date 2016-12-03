package android_network.hetnet.data;

public class Application {
  private double applicationID;
  private String applicationType;

  public double getApplicationID() {
    return applicationID;
  }

  public void setApplicationID(double applicationID) {
    this.applicationID = applicationID;
  }

  public String getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }

  @Override
  public String toString() {
    return "Application{" +
      "applicationID=" + applicationID +
      ", applicationType='" + applicationType + '\'' +
      '}';
  }
}
