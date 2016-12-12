package android_network.hetnet.policy_engine;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android_network.hetnet.cloud.DummyMachineLearningEngine;
import android_network.hetnet.cloud.SendCloud;
import android_network.hetnet.common.Constants;
import android_network.hetnet.common.trigger_events.TriggerEvent;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.Application;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.Network;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.data.PolicyVector;
import android_network.hetnet.location.LocationEventTracker;
import android_network.hetnet.location.LocationFetcher;
import android_network.hetnet.location.LocationResponseEvent;
import android_network.hetnet.network.NetworkEventTracker;
import android_network.hetnet.network.NetworkListFetcher;
import android_network.hetnet.network.NetworkResponseEvent;
import android_network.hetnet.system.SystemEventTracker;
import android_network.hetnet.system.SystemList;
import android_network.hetnet.system.SystemListFetcher;
import android_network.hetnet.system.SystemResponseEvent;

public class PolicyEngine extends Service {
  private static final String LOG_TAG = "PolicyEngine";

  Intent networkListFetcherService;
  Intent locationFetcherService;
  Intent systemListFetcherService;

  PolicyVector ruleVector;
  PolicyVector currentStateVector;

  Location location;
  Application application;
  SystemList  systemList;
  List<Network> networkList;

  boolean locationDataReceived = false;
  boolean networkDataReceived = false;
  boolean systemDataReceived = false;

  @Override
  public void onCreate() {
    super.onCreate();
    EventBus.getDefault().register(this);

    Intent networkEventTrackerService = new Intent(this, NetworkEventTracker.class);
    this.startService(networkEventTrackerService);

    Intent systemEventTrackerService = new Intent(this, SystemEventTracker.class);
    this.startService(systemEventTrackerService);

    Intent locationEventTrackerService = new Intent(this, LocationEventTracker.class);
    this.startService(locationEventTrackerService);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(TriggerEvent event) {
    locationDataReceived = networkDataReceived = systemDataReceived = false;

    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getEventName(), event.getTimeOfEvent()));

    currentStateVector = new PolicyVector();

    locationFetcherService = new Intent(this, LocationFetcher.class);
    this.startService(locationFetcherService);

    networkListFetcherService = new Intent(this, NetworkListFetcher.class);
    this.startService(networkListFetcherService);

    systemListFetcherService = new Intent(this, SystemListFetcher.class);
    this.startService(systemListFetcherService);
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(LocationResponseEvent event) {
    if (event.getLocation() != null) {
      locationDataReceived = true;
      this.stopService(locationFetcherService);
      location = event.getLocation();
      currentStateVector.setLatitude(location.getLatitude());
      currentStateVector.setLongitude(location.getLongitude());
      checkDataAndSendData();
    }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(NetworkResponseEvent event) {
    networkDataReceived = true;
    this.stopService(networkListFetcherService);
    networkList = event.getListOfNetworks();
    networkList = getRankedNetworkList(networkList);

    for (Network network : networkList) {
      if (network.isCurrentNetwork()) {
        currentStateVector.setNetworkSSID(network.getNetworkSSID());
        currentStateVector.setBandwidth(network.getBandwidth());
        currentStateVector.setSignalStrength(network.getSignalStrength());
        currentStateVector.setSignalFrequency(network.getSignalFrequency());
        currentStateVector.setTimeToConnect(network.getTimeToConnect());
        currentStateVector.setCost(network.getCost());
        break;
      }
    }

    checkDataAndSendData();
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(SystemResponseEvent event) {
    systemDataReceived = true;
    application = SystemList.getForegroundApplication(event.getSystemList());
    systemList  = event.getSystemList();

    this.stopService(systemListFetcherService);

    currentStateVector.setApplicationID(application.getApplicationID());
    currentStateVector.setApplicationType(application.getApplicationType());
    checkDataAndSendData();
  }

  private void checkDataAndSendData() {
    if (networkDataReceived && locationDataReceived && systemDataReceived) {
      ruleVector = DummyMachineLearningEngine.getPolicyRuleVector(location, application);
      DataStoreObject dataStoreObject = new DataStoreObject(application.getApplicationID(), application.getApplicationType(), location.getLatitude(),
        location.getLongitude(), networkList);
      sendDataToCloud(dataStoreObject);

      dataStoreObject.setSystemList(systemList);

      PolicyEngineData data = new PolicyEngineData(ruleVector, currentStateVector, dataStoreObject);
      EventBus.getDefault().post(new UITriggerEvent(Constants.POLICY_ENGINE, data, Calendar.getInstance().getTime()));
    }
  }

  private void sendDataToCloud(DataStoreObject dataStoreObject) {
    ArrayList<DataStoreObject> dataStoreObjectList = createDataStoreList(dataStoreObject);
    Intent cloudService = new Intent(this, SendCloud.class);
    cloudService.putExtra("currentData", dataStoreObjectList);
    this.startService(cloudService);
  }

  private ArrayList<DataStoreObject> createDataStoreList(DataStoreObject dataStoreObject) {
    ArrayList<DataStoreObject> dataStoreObjectList = new ArrayList<>();
    dataStoreObjectList.add(dataStoreObject);
    return dataStoreObjectList;
  }

  private List<Network> getRankedNetworkList(List<Network> networkList) {
    return networkList;
  }
}