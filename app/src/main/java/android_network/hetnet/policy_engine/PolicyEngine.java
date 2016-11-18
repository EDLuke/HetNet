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

public class PolicyEngine extends Service {
  StringBuilder data;
  Intent networkListFetcherService;
  Intent locationFetcherService;

  boolean locationDataReceived = false;
  boolean networkDataReceived = false;

  @Override
  public void onCreate() {
    super.onCreate();
    EventBus.getDefault().register(this);

    Intent networkEventTrackerService = new Intent(this, NetworkEventTracker.class);
    this.startService(networkEventTrackerService);

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
    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getEventName(), event.getTimeOfEvent()));

    locationFetcherService = new Intent(this, LocationFetcher.class);
    this.startService(locationFetcherService);

    networkListFetcherService = new Intent(this, NetworkListFetcher.class);
    this.startService(networkListFetcherService);
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
  }

  private void checkIfAllDataReceived() {
    if (networkDataReceived && locationDataReceived) {
      EventBus.getDefault().post(new UITriggerEvent(Constants.POLICY_ENGINE, String.valueOf(data), Calendar.getInstance().getTime()));
    }
  }
}