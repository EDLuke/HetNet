package android_network.hetnet.common.comparators;

import java.util.Comparator;

import android_network.hetnet.data.Network;

public class SignalStrengthComparator implements Comparator<Network> {

  @Override
  public int compare(Network lhs, Network rhs) {
    if (lhs.getSignalStrength() < rhs.getSignalStrength()) {
      return 1;
    } else if (lhs.getSignalStrength() > rhs.getSignalStrength()) {
      return -1;
    } else {
      return lhs.getNetworkSSID().compareTo(rhs.getNetworkSSID());
    }
  }
}