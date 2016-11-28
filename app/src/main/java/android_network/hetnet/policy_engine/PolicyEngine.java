package android_network.hetnet.policy_engine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

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
import android_network.hetnet.system.SystemListFetcher;
import android_network.hetnet.system.SystemResponseEvent;

public class PolicyEngine extends Service {
  StringBuilder data;
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

//    if(checkIfAllDataReceived()){
//      EventBus.getDefault()
//    }
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(NetworkResponseEvent event) {
    networkDataReceived = true;
    this.stopService(networkListFetcherService);
    data.append(event.getNetworkList()).append("\n\n");

    if(checkIfAllDataReceived())
      EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getNetworkList(), event.getTimeOfEvent()));
  }


  private boolean checkIfAllDataReceived() {

    return networkDataReceived && locationDataReceived;

//    if (networkDataReceived && locationDataReceived) {
//      EventBus.getDefault().post(new UITriggerEvent(Constants.POLICY_ENGINE, String.valueOf(data), Calendar.getInstance().getTime()));
//    }
  }


  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(SystemResponseEvent event) {
    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getSystemList(), event.getTimeOfEvent()));
  }
}