package android_network.hetnet.system.monitor_threads;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import android_network.hetnet.system.event.DevicePowerThreadInfoEvent;

/**
 * Device Power Thread
 */
public class DevicePowerThread extends Thread {
    final String m_threadName = "DevicePowerThread";

    Context m_context;
    IntentFilter m_iFilter;
    Intent m_iBatteryStatus;

    public DevicePowerThread(Context context) {
        m_context = context;

        m_iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        m_iBatteryStatus = m_context.registerReceiver(null, m_iFilter);
    }

    public void run() {
//    BatteryManager batteryManager = (BatteryManager) (m_context.getSystemService(Context.BATTERY_SERVICE));
        int level = m_iBatteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = m_iBatteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        //Post to event bus
        EventBus.getDefault().post(new DevicePowerThreadInfoEvent(m_threadName, "Success", batteryPct));
    }

    public boolean isCharging(BatteryManager bm) {
        //Requires API >= 21
        Long energy = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            energy = bm.getLongProperty(BatteryManager.BATTERY_STATUS_CHARGING);
        }
        //The number is positive when the device is discharging and negative when the device is charging
        return (energy < 0);


    }
}
