package android_network.hetnet.system.event;

import android.app.ActivityManager;

import java.util.List;

/**
 * ActivityManagerInfoEvent
 */
public class ActivityManagerInfoEvent extends ThreadInfoEvent {
  public final List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

  public ActivityManagerInfoEvent(String threadName, String message, List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos) {
    super(threadName, message);

    this.m_runningAppProcessInfos = runningAppProcessInfos;
  }
}
