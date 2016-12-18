package android_network.hetnet.system;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.Application;

import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;

public class SystemList implements Serializable {

  /*Device List*/
  //CPU Usage
  private float m_cpuUsage;

  //Battery Percentage
  private float m_batteryPct;

  /*Application List
  * UID / Application List */
  private HashMap<Integer, ApplicationList> m_applicationList;

  /*Getters and setters*/
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

  public HashMap<Integer, ApplicationList> getApplicationList() {
    return m_applicationList;
  }

  public void setApplicationList(HashMap<Integer, ApplicationList> m_applicationList) {
    this.m_applicationList = m_applicationList;
  }

  //Current decision: send second largest CPU usage application to the cloud
  public static Application getForegroundApplication(SystemList systemList) {
    HashMap<Integer, ApplicationList> applicationList = systemList.getApplicationList();

    String maxEntry = "";
    int maxCpu = -1;

    for (HashMap.Entry<Integer, ApplicationList> entry : applicationList.entrySet()) {
      if (maxEntry == null || entry.getValue().getCpuUsage() > maxCpu) {
        maxEntry = entry.getValue().getProcessName();
        maxCpu = entry.getValue().getCpuUsage();
      }
    }

    EventBus.getDefault().post(new UITriggerEvent(SYSTEM_EVENT_TRACKER, "Send " + maxEntry, Calendar.getInstance().getTime()));

    return new Application(maxEntry, "Generic");
  }
}
