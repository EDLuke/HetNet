package android_network.hetnet.system;

public class ApplicationList{
  private String processName = "";
  private long rxBytes    = -1;
  private long rxPackets  = -1;
  private long txBytes    = -1;
  private long txPackets  = -1;
  private int  cpuUsage   = -1;

  public long getRxBytes() {
    return rxBytes;
  }

  public void setRxBytes(long rxBytes) {
    if(rxBytes < 0)
      rxBytes = 0;

    if(rxBytes == 0)
      return;

    this.rxBytes = rxBytes;
  }

  public long getRxPackets() {
    return rxPackets;
  }

  public void setRxPackets(long rxPackets) {
    if(rxPackets < 0)
      rxPackets = 0;

    if(rxPackets == 0)
      return;

    this.rxPackets = rxPackets;
  }

  public long getTxBytes() {
    return txBytes;
  }

  public void setTxBytes(long txBytes) {
    if(txBytes < 0)
      txBytes = 0;

    if(txBytes == 0)
      return;

    this.txBytes = txBytes;
  }

  public long getTxPackets() {
    return txPackets;
  }

  public void setTxPackets(long txPackets) {
    if(txPackets < 0)
      txPackets = 0;

    if(txPackets == 0)
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
}