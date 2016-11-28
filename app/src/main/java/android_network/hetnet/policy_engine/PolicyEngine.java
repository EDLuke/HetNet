package android_network.hetnet.policy_engine;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android_network.hetnet.common.Constants;
import android_network.hetnet.common.trigger_events.TriggerEvent;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
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
import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;

public class PolicyEngine extends Service {
  private static final String LOG_TAG = "PolicyEngine";

  StringBuilder data;
  SystemList systemList;
  Intent networkListFetcherService;
  Intent locationFetcherService;
  Intent systemListFetcherService;

  boolean locationDataReceived = false;
  boolean networkDataReceived = false;

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
    locationDataReceived = networkDataReceived = false;
    data = new StringBuilder();
    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getEventName(), null, event.getTimeOfEvent()));


    locationFetcherService = new Intent(this, LocationFetcher.class);
    this.startService(locationFetcherService);

    networkListFetcherService = new Intent(this, NetworkListFetcher.class);
    this.startService(networkListFetcherService);

    systemListFetcherService  = new Intent(this, SystemListFetcher.class);
    this.startService(systemListFetcherService);
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(LocationResponseEvent event) {
    locationDataReceived = true;
    this.stopService(locationFetcherService);
    data.append(event.getLocation()).append("\n\n");
    checkIfAllDataReceived();
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(NetworkResponseEvent event) {
    networkDataReceived = true;
    this.stopService(networkListFetcherService);
    data.append(event.getListOfNetworks()).append("\n\n");
    checkIfAllDataReceived();
    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getListOfNetworks(), null, event.getTimeOfEvent()));
  }


  private void checkIfAllDataReceived() {
    if (networkDataReceived && locationDataReceived) {
      EventBus.getDefault().post(new UITriggerEvent(Constants.POLICY_ENGINE, String.valueOf(data), null, Calendar.getInstance().getTime()));
    }
  }


  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(SystemResponseEvent event) {
    systemList = event.getSystemList();

    //EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), "", event.getSystemList(), event.getTimeOfEvent()));

    makeDecision_System();
  }

  //Current decision: send second largest CPU usage application to the cloud
  private void makeDecision_System(){
    List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = systemList.getRunningAppProcessInfos();
    HashMap<String, Integer> cpuUsageApplication = systemList.getCpuUsage_app();


    HashMap.Entry<String, Integer> maxEntry = null;

    for (HashMap.Entry<String, Integer> entry : cpuUsageApplication.entrySet())
    {
      if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
        maxEntry = entry;

    }

    EventBus.getDefault().post(new UITriggerEvent(SYSTEM_EVENT_TRACKER, "Send " + maxEntry.getKey(), null, Calendar.getInstance().getTime()));
    Log.v(LOG_TAG, "Send " + maxEntry.getKey() + " to the cloud");
  }


}