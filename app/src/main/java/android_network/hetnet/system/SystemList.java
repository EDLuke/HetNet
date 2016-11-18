package android_network.hetnet.system;

import android.app.ActivityManager;

import java.util.List;

/**
 * SystemList
 * All system related info used for Policy Engine to make the decision
 */

public class SystemList {
    //Running Application Info
    private List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

    //CPU Usage
    private float m_cpuUsage;

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
}
