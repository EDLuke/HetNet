package android_network.hetnet.system.monitor_threads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Device Power Thread
 */




public class DevicePowerThread extends Thread {

    public static final String LOGTAG = "POWER_MONITOR_THREAD";
    TextView m_ui_charging_element, m_ui_percentage_element;
    Context m_context;



    //BroadcastReceiver to capture system broadcasts containing the current battery level
    private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }
            //Display the battery level on the UI element, at this point
            m_ui_percentage_element.setText(String.valueOf(level) + "%");
        }
    };



    public DevicePowerThread(Context context, TextView isChargingUIElement, TextView batteryPercentageUIElement) {
        m_ui_charging_element = isChargingUIElement;
        m_ui_percentage_element = batteryPercentageUIElement;
        m_context = context;

    }

    public void run() {
        /* UI elements are changed in this way using a thread so that battery info can be continuously
        monitored on a thread other than the UI thread. If this was done on the UI thread, it could
        potentially introduce display lag */
        m_ui_charging_element.post(new Runnable() {
            @Override
            public void run() {
                BatteryManager mBatteryManager = (BatteryManager) (m_context.getSystemService(Context.BATTERY_SERVICE));
                Long energy = mBatteryManager.getLongProperty(BatteryManager.BATTERY_STATUS_CHARGING);
                if (isCharging(mBatteryManager)) {
                    m_ui_charging_element.setText("The device is charging");
                } else {
                    m_ui_charging_element.setText("The device is discharging");
                }
//                Log.d(DevicePowerThread.LOGTAG, mBatteryManager.EXTRA_LEVEL);
            }
        });
        //Listen for ACTION_BATTERY_CHANGED broadcasts
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        m_context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }


    public boolean isCharging(BatteryManager bm) {
        //Requires API >= 21
        Long energy = bm.getLongProperty(BatteryManager.BATTERY_STATUS_CHARGING);
        //The number is positive when the device is discharging and negative when the device is charging
        return (energy < 0) ? true : false;

    }

}