package android_network.hetnet.system;

import android.app.ActivityManager;

<<<<<<< HEAD
=======
import java.util.Calendar;
>>>>>>> parent of 4b1379c... changes
import java.util.HashMap;
import java.util.List;

import android_network.hetnet.data.Application;
<<<<<<< HEAD
=======
import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;
>>>>>>> parent of 4b1379c... changes

public class SystemList {

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
<<<<<<< HEAD
    return new Application("Test", "Generic");
=======
    HashMap<Integer, ApplicationList> applicationList = systemList.getApplicationList();

    String maxEntry = "";
    int    maxCpu   = -1;

    for (HashMap.Entry<Integer, ApplicationList> entry : applicationList.entrySet()) {
      if (maxEntry == null || entry.getValue().getCpuUsage() > maxCpu) {
        maxEntry = entry.getValue().getProcessName();
        maxCpu   = entry.getValue().getCpuUsage();
      }
    }

    EventBus.getDefault().post(new UITriggerEvent(SYSTEM_EVENT_TRACKER, "Send " + maxEntry, Calendar.getInstance().getTime()));

    return new Application(maxEntry, "Generic");
>>>>>>> parent of 4b1379c... changes
  }
}
