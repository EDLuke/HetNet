package android_network.hetnet.data;

public class PolicyEngineData {
  private PolicyVector ruleVector;
  private PolicyVector currentStateVector;
  private DataStoreObject dataStoreObject;

  public PolicyEngineData(PolicyVector ruleVector, PolicyVector currentStateVector, DataStoreObject dataStoreObject) {
    this.ruleVector = ruleVector;
    this.currentStateVector = currentStateVector;
    this.dataStoreObject = dataStoreObject;
  }

  public PolicyVector getRuleVector() {
    return ruleVector;
  }

  public void setRuleVector(PolicyVector ruleVector) {
    this.ruleVector = ruleVector;
  }

  public PolicyVector getCurrentStateVector() {
    return currentStateVector;
  }

  public void setCurrentStateVector(PolicyVector currentStateVector) {
    this.currentStateVector = currentStateVector;
  }

  public DataStoreObject getDataStoreObject() {
    return dataStoreObject;
  }

  public void setDataStoreObject(DataStoreObject dataStoreObject) {
    this.dataStoreObject = dataStoreObject;
  }
}
