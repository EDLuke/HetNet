package android_network.hetnet.network;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;

public class NetworkListFetcher extends Service {
  WifiManager wifiManager;
  StringBuilder mainText = new StringBuilder();
  TelephonyManager telephonyManager;

  @Override
  public void onCreate() {
    super.onCreate();
    EventBus.getDefault().register(this);
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
  protected void onMessageEvent(NetworkRequestEvent event) {
    getWifiInfo();
    getLTEInfo();
    EventBus.getDefault().post(new NetworkResponseEvent(NETWORK_LIST_FETCHER, String.valueOf(mainText), Calendar.getInstance().getTime()));
  }

  private void getLTEInfo() {
    // Getting telephony manager for LTE
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    // Trial for carrier cost - DON'T DELETE!!!
    String carrierName = telephonyManager.getNetworkOperatorName();
    String cost = "$0";

    switch (carrierName) {
      case "Fi Network":
        cost = "$0.5";
        break;
      case "Verizon":
        cost = "$1";
        break;
      case "AT&T":
        cost = "$2";
        break;
      case "Sprint":
        cost = "$3";
        break;
      case "Tmobile":
        cost = "$4";
        break;
    }

    System.out.println("Carrier Name: " + carrierName);
    System.out.println("Cost: " + cost);
    // END OF TRIAL

    List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

    for (CellInfo cellInfo : cellInfoList) {
      if (cellInfo instanceof CellInfoLte) {
        mainText.append(cellInfo.toString());
      }
    }
  }

  private void getWifiInfo() {
    // Getting the WiFi Manager
    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    // Initiate the network scan
    WifiReceiver receiverWifi = new WifiReceiver();
    registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifiManager.startScan();

    // Sending the message back by broadcasting the Intent to receivers in this app.
    while (String.valueOf(mainText).equals("")) {
      ;
    }
  }

  private class WifiReceiver extends BroadcastReceiver {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context c, Intent intent) {
      List<ScanResult> wifiList = wifiManager.getScanResults();
      mainText.append("\nNumber Of WiFi connections: ").append(wifiList.size()).append("\n\n");

      for (int i = 0; i < wifiList.size(); i++) {
        mainText.append(Integer.valueOf(i + 1).toString()).append(". ");
        mainText.append((wifiList.get(i)).toString());
        mainText.append("\n\n");
      }
    }
  }
}