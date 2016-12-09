package android_network.hetnet.network;

import android.net.wifi.WifiManager;

import android_network.hetnet.data.Network;

public class NetworkBandwidthCalculator {

  static WifiManager wifiManager;

  //public static double getNetworkBandwidth(Network network) {

  //int BW = wifiManager.getConnectionInfo().getLinkSpeed();

  //return BW;
  //}

  public static double getTimeToConnect(Network network) {
    return 1.0;
  }
}
