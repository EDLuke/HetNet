package android_network.hetnet.system.event;

import java.util.Calendar;

import android_network.hetnet.trigger_events.TriggerEvent;

/**
 * DevicePowerTriggerEvent
 */
public class DevicePowerTriggerEvent extends TriggerEvent {
  public final float m_batteryPct;

  public DevicePowerTriggerEvent(String threadName, String message, float batteryPct) {
    super(threadName, message, Calendar.getInstance().getTime());

    this.m_batteryPct = batteryPct;
  }
}
