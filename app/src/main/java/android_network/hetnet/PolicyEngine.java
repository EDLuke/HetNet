package android_network.hetnet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import android_network.hetnet.network.NetworkEventTracker;
import android_network.hetnet.network.NetworkRequestEvent;
import android_network.hetnet.network.NetworkResponseEvent;
import android_network.hetnet.network.NetworkListFetcher;
import android_network.hetnet.trigger_events.TriggerEvent;
import android_network.hetnet.trigger_events.UITriggerEvent;

import static android_network.hetnet.Constants.POLICY_ENGINE;

public class PolicyEngine extends Service {
  @Override
  public void onCreate() {
    super.onCreate();
    EventBus.getDefault().register(this);

    Intent networkEventTrackerService = new Intent(this, NetworkEventTracker.class);
    this.startService(networkEventTrackerService);

    Intent networkListFetcherService = new Intent(this, NetworkListFetcher.class);
    this.startService(networkListFetcherService);
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
    EventBus.getDefault().post(new UITriggerEvent(event.eventOriginator, event.eventName, event.timeOfEvent));
    EventBus.getDefault().post(new NetworkRequestEvent(POLICY_ENGINE));
  }

  @Subscribe(threadMode = ThreadMode.ASYNC)
  public void onMessageEvent(NetworkResponseEvent event) {
    EventBus.getDefault().post(new UITriggerEvent(event.getEventOriginator(), event.getListOfNetworks(), Calendar.getInstance().getTime()));
  }
}