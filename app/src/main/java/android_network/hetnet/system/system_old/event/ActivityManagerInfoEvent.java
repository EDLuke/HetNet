package android_network.hetnet.system.system_old.event;

import android.app.ActivityManager;

import java.util.Calendar;
import java.util.List;

import android_network.hetnet.common.trigger_events.TriggerEvent;

/**
 * ActivityManagerInfoEvent
 */
public class ActivityManagerInfoEvent extends TriggerEvent {
  public final List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

  public ActivityManagerInfoEvent(String threadName, String message, List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos) {
    super(threadName, message, Calendar.getInstance().getTime());

    this.m_runningAppProcessInfos = runningAppProcessInfos;
  }
}