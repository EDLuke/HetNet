package android_network.hetnet.cloud;

import android.location.Location;

import android_network.hetnet.data.Application;
import android_network.hetnet.data.PolicyVector;

public class DummyMachineLearningEngine {
  public static PolicyVector getPolicyRuleVector(Location location, Application application) {
    return new PolicyVector(application.getApplicationID(), application.getApplicationType(), location.getLatitude(), location.getLongitude(),
      "Columbia University", 1.0, 0.0, 0.0, 0.0, 0.0);
  }
}
