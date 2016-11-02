package android_network.hetnet.system.monitor_threads;

import android.content.Context;
import android.os.BatteryManager;
import android.widget.TextView;

/**
 * Device Power Thread
 */
public class DevicePowerThread extends Thread {
  TextView m_ui_element;
  Context m_context;

  public DevicePowerThread(Context context, TextView ui_element) {
    m_ui_element = ui_element;
    m_context = context;

  }

  public void run() {
    m_ui_element.post(new Runnable() {
      @Override
      public void run() {
        BatteryManager mBatteryManager = (BatteryManager) (m_context.getSystemService(Context.BATTERY_SERVICE));
        Long energy = mBatteryManager.getLongProperty(BatteryManager.BATTERY_STATUS_CHARGING);
        m_ui_element.setText(energy.toString());
      }
    });
  }
}
