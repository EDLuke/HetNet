package android_network.hetnet.system.event;

/**
 * DevicePowerThreadInfoEvent
 */
public class DevicePowerThreadInfoEvent extends ThreadInfoEvent{
  public final float m_batteryPct;

  public DevicePowerThreadInfoEvent(String threadName, String message, float batteryPct){
    super(threadName, message);

    this.m_batteryPct = batteryPct;
  }
}
