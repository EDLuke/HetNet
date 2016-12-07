package android_network.hetnet.system;

import android.app.ActivityManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android_network.hetnet.common.EventList;
import android_network.hetnet.common.trigger_events.UITriggerEvent;

import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;

/**
 * SystemList
 * All system related info used for Policy Engine to make the decision
 */

public class SystemList extends EventList {
  //Running Application Info
  private List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

  //CPU Usage
  private float m_cpuUsage;

  //CPU Usage per application
  //processName / CPU usage
  //TODO:improve this part
  private HashMap<String, Integer> m_cpuUsage_app;

  //Battery Percentage
  private float m_batteryPct;

  public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfos() {
    return m_runningAppProcessInfos;
  }

  public void setRunningAppProcessInfos(List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos) {
    this.m_runningAppProcessInfos = m_runningAppProcessInfos;
  }

  public float getCpuUsage() {
    return m_cpuUsage;
  }

  public void setCpuUsage(float m_cpuUsage) {
    this.m_cpuUsage = m_cpuUsage;
  }

  public float getBatteryPct() {
    return m_batteryPct;
  }

  public void setBatteryPct(float m_batteryPct) {
    this.m_batteryPct = m_batteryPct;
  }

  public HashMap<String, Integer> getCpuUsage_app() {
    return m_cpuUsage_app;
  }

  public void setCpuUsage_app(HashMap<String, Integer> m_cpuUsage_app) {
    this.m_cpuUsage_app = m_cpuUsage_app;
  }

  //Current decision: send second largest CPU usage application to the cloud
  public static String makeDecisionSystem(SystemList systemList) {
    List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = systemList.getRunningAppProcessInfos();
    HashMap<String, Integer> cpuUsageApplication = systemList.getCpuUsage_app();


    HashMap.Entry<String, Integer> maxEntry = null;

    for (HashMap.Entry<String, Integer> entry : cpuUsageApplication.entrySet()) {
      if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
        maxEntry = entry;
      }
    }

    EventBus.getDefault().post(new UITriggerEvent(SYSTEM_EVENT_TRACKER, "Send " + maxEntry.getKey(), null, Calendar.getInstance().getTime()));

    return maxEntry.getKey();
  }

  @Override
  public String toString(){
    //TODO: finish toString()
    return "";
  }
}
