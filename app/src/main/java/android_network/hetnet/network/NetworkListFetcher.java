package android_network.hetnet.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;

public class NetworkListFetcher extends IntentService {
  WifiManager wifiManager;
  StringBuilder mainText;
  TelephonyManager telephonyManager;

  public NetworkListFetcher() {
    super("NetworkListFetcher");
  }

  @Override
  public void onCreate() {
    super.onCreate();

  }

  @Override
  protected void onHandleIntent(Intent intent) {
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
        mainText.append("\n\n");
      }
    }
  }

  private void getWifiInfo() {
    mainText = new StringBuilder();

    // Getting the WiFi Manager
    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    // Initiate the network scan
    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifiManager.startScan();

    // Sending the message back by broadcasting the Intent to receivers in this app.
    while (String.valueOf(mainText).equals("")) {
      ;
    }

    unregisterReceiver(wifiReceiver);
  }

  private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context context, Intent intent) {
      List<ScanResult> wifiList = wifiManager.getScanResults();
      mainText.append("\nNumber Of WiFi connections: ").append(wifiList.size()).append("\n\n");

      // Column Headings
      mainText.append("\nNetwork Name  |" + "\t\tSecurity  |" + "\t\tSignal Level  |" + "\t\tFrequency \n\n\n");

      for (int i = 0; i < wifiList.size(); i++) {
        //mainText.append(Integer.valueOf(i + 1).toString()).append(". "); removed ranking

        // workaround solution for table view => needs improvement
        String list = wifiList.get(i).toString();

        // seperate each column
        StringTokenizer tokens = new StringTokenizer(list, ",");

        //SSID
        String SSID = tokens.nextToken();

        //BSSID
        String BSSID = tokens.nextToken();

        //Capabilities
        String sec = tokens.nextToken();

        //Level
        String lev = tokens.nextToken();

        //freq
        String freq = tokens.nextToken();


        StringTokenizer m = new StringTokenizer(SSID, ":");
        String lab1 = m.nextToken();
        String dat1 = m.nextToken();
        mainText.append(dat1 +"\t\t");

        StringTokenizer n = new StringTokenizer(sec, ":");
        String lab2 = n.nextToken();
        String dat2 = n.nextToken();
        mainText.append(dat2 +"\t\t");

        StringTokenizer o = new StringTokenizer(lev, ":");
        String lab3 = o.nextToken();
        String dat3 = o.nextToken();
        mainText.append(dat3 +"\t\t");

        StringTokenizer p = new StringTokenizer(freq, ":");
        String lab4 = p.nextToken();
        String dat4 = p.nextToken();
        mainText.append(dat4 +"\n");

        mainText.append("\n\n");


      }
    }
  };
}