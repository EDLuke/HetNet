package android_network.hetnet.policy_engine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

import android_network.hetnet.common.Constants;
import android_network.hetnet.common.trigger_events.TriggerEvent;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.Network;
import android_network.hetnet.data.PolicyRuleVector;
import android_network.hetnet.location.LocationEventTracker;
import android_network.hetnet.location.LocationFetcher;
import android_network.hetnet.location.LocationResponseEvent;
import android_network.hetnet.network.NetworkEventTracker;
import android_network.hetnet.network.NetworkList;
import android_network.hetnet.network.NetworkListFetcher;
import android_network.hetnet.network.NetworkResponseEvent;
import android_network.hetnet.system.SystemEventTracker;
import android_network.hetnet.system.SystemList;
import android_network.hetnet.system.SystemListFetcher;
import android_network.hetnet.system.SystemResponseEvent;

public class PolicyEngine extends Service {
  private static final String LOG_TAG = "PolicyEngine";

  NetworkList m_networkList;

  StringBuilder data;
  Intent networkListFetcherService;
  Intent locationFetcherService;
  Intent systemListFetcherService;

  PolicyRuleVector ruleVector;
  PolicyRuleVector currentStateVector;

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

    m_networkList = new NetworkList();
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
    data = new StringBuilder();
//    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getEventName(), event.getTimeOfEvent()));

    ruleVector = new PolicyRuleVector();
    currentStateVector = new PolicyRuleVector();

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
      data.append(event.getLocation()).append("\n\n");
      currentStateVector.setLocation(event.getLocation());
      checkIfAllDataReceived();
    }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(NetworkResponseEvent event) {
    networkDataReceived = true;
    this.stopService(networkListFetcherService);
    List<Network> networkList = event.getListOfNetworks();
    data.append(networkList).append("\n\n");
    networkList = getRankedNetworkList(networkList);
    m_networkList.setListOfNetworks(networkList);

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

    if(checkIfAllDataReceived())
      EventBus.getDefault().post(new UITriggerEvent(Constants.NETWORK_LIST_FETCHER, String.valueOf(data), m_networkList, Calendar.getInstance().getTime()));

  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(SystemResponseEvent event) {
    systemDataReceived = true;
    this.stopService(systemListFetcherService);
    currentStateVector.setApplicationId(SystemList.makeDecisionSystem(event.getSystemList()));
  }

  private boolean checkIfAllDataReceived() {
    return networkDataReceived && locationDataReceived;


  }

  private List<Network> getRankedNetworkList(List<Network> networkList) {
    return networkList;
  }
}