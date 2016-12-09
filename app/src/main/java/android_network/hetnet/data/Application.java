package android_network.hetnet.data;

public class Application {
  private String applicationID;
  private String applicationType;

  public Application(String applicationID, String applicationType) {
    this.applicationID = applicationID;
    this.applicationType = applicationType;
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

  @Override
  public String toString() {
    return "Application{" +
      "applicationID=" + applicationID +
      ", applicationType='" + applicationType + '\'' +
      '}';
  }
}
