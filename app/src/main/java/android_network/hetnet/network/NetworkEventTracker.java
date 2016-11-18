package android_network.hetnet.network;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import static android_network.hetnet.common.Constants.NETWORK_EVENT_TRACKER;

public class NetworkEventTracker extends Service {
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      System.out.println("Connection switched");
      ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo ni = manager.getActiveNetworkInfo();
      EventBus.getDefault().post(new NetworkTriggerEvent(NETWORK_EVENT_TRACKER, "Connection Changed", Calendar.getInstance().getTime()));
    }
  };

  private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context context, Intent intent) {
      System.out.println("Number of WiFi connections changed");
      EventBus.getDefault().post(new NetworkTriggerEvent(NETWORK_EVENT_TRACKER, "Networks Changed", Calendar.getInstance().getTime()));
    }
  };
}
