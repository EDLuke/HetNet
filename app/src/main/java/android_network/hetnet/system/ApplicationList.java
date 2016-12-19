package android_network.hetnet.system;

import java.io.Serializable;

public class ApplicationList implements Serializable {
  private String processName = "";

  /*TrafficStats*/
  private long rxBytes = -1;
  private long rxPackets = -1;
  private long txBytes = -1;
  private long txPackets = -1;

  /*Memory Info*/
  private int privateClean = -1;
  private int privateDirty = -1;
  private int pss = -1;
  private int uss = -1;

  /*Battery Info*/
  private double batteryMah = -1;
  private double batteryPercent = -1;

  private int cpuUsage = -1;

  private int wakeLockCount = -1;

  public long getRxBytes() {
    return rxBytes;
  }

  public void setRxBytes(long rxBytes) {
    if (rxBytes < 0)
      rxBytes = 0;

    if (rxBytes == 0)
      return;

    this.rxBytes = rxBytes;
  }

  public long getRxPackets() {
    return rxPackets;
  }

  public void setRxPackets(long rxPackets) {
    if (rxPackets < 0)
      rxPackets = 0;

    if (rxPackets == 0)
      return;

    this.rxPackets = rxPackets;
  }

  public long getTxBytes() {
    return txBytes;
  }

  public void setTxBytes(long txBytes) {
    if (txBytes < 0)
      txBytes = 0;

    if (txBytes == 0)
      return;

    this.txBytes = txBytes;
  }

  public long getTxPackets() {
    return txPackets;
  }

  public void setTxPackets(long txPackets) {
    if (txPackets < 0)
      txPackets = 0;

    if (txPackets == 0)
      return;

    this.txPackets = txPackets;
  }

  public int getCpuUsage() {
    return cpuUsage;
  }

  public void setCpuUsage(int cpuUsage) {
    this.cpuUsage = cpuUsage;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public int getPrivateClean() {
    return privateClean;
  }

  public void setPrivateClean(int privateClean) {
    this.privateClean = privateClean;
  }

  public int getPrivateDirty() {
    return privateDirty;
  }

  public void setPrivateDirty(int privateDirty) {
    this.privateDirty = privateDirty;
  }

  public int getPss() {
    return pss;
  }

  public void setPss(int pss) {
    this.pss = pss;
  }

  public int getUss() {
    return uss;
  }

  public void setUss(int uss) {
    this.uss = uss;
  }

  public double getBatteryMah() {
    return batteryMah;
  }

  public void setBatteryMah(double batteryMah) {
    this.batteryMah = batteryMah;
  }

  public double getBatteryPercent() {
    return batteryPercent;
  }

  public void setBatteryPercent(double batteryPercent) {
    this.batteryPercent = batteryPercent;
  }

  public int getWakeLockCount() {
    return wakeLockCount;
  }

  public void addWakeLockCount() {
    this.wakeLockCount++;
  }
}