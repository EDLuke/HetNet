package android_network.hetnet.system.system_old.event;

import java.util.Calendar;

import android_network.hetnet.common.trigger_events.TriggerEvent;

/**
 * Created by gabe on 11/9/16.
 */

public class CPUUsageEvent extends TriggerEvent {

  String m_thread_name;
  float cpuUsage;

  public CPUUsageEvent(String threadName, String message, float cpu) {
    super(threadName, message, Calendar.getInstance().getTime());
    this.m_thread_name = threadName;
    this.cpuUsage = cpu;
  }

  public float getCpuUsage() {
    return cpuUsage;
  }


}
