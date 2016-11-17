package android_network.hetnet.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import android_network.hetnet.R;
import android_network.hetnet.system.adapter.RunningApplicationListAdapter;
import android_network.hetnet.system.event.ThreadInfoUpdatedEvent;
import android_network.hetnet.system.monitor_threads.ActivityManagerThread;
import android_network.hetnet.system.monitor_threads.CPUUsageThread;
import android_network.hetnet.system.monitor_threads.DevicePowerThread;

public class SystemManagerActivity extends Activity {
  /**
   * Log Tag
   */
  private static final String TAG = "SystemManager";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_system_manager);

    //Register to the eventbus
    EventBus.getDefault().register(this);

    MonitorManager monitorManager = new MonitorManager();

    //Decouple ui elements with individual threads
    //Have them managed by Monitor Manager instead
    ActivityManagerThread thread_am = new ActivityManagerThread(getApplicationContext());
    DevicePowerThread powerThread = new DevicePowerThread(getApplicationContext());
    CPUUsageThread cpuThread = new CPUUsageThread(getApplicationContext());

    monitorManager.insertNewThread(thread_am, "ActivityManagerThread", findViewById(R.id.listview_ps));
    monitorManager.insertNewThread(powerThread, "DevicePowerThread", findViewById(R.id.textview_devicepower));
    monitorManager.insertNewThread(cpuThread, "CPU_USAGE_THREAD", findViewById(R.id.cpu_usage));
    monitorManager.startMonitor();
  }

  @Override
  protected void onStop() {
    //Unregister from the event bus
    EventBus.getDefault().unregister(this);
    super.onStop();
  }


  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(ThreadInfoUpdatedEvent event) {
    String threadName = event.m_thread_name;
    String message = event.m_message;
    View ui_element = event.m_ui;
    Object extraMsg = event.m_extraMsg;

    switch (threadName) {
      case "ActivityManagerThread":
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = (List<ActivityManager.RunningAppProcessInfo>) extraMsg;
        RunningApplicationListAdapter adapter = new RunningApplicationListAdapter(getApplicationContext(), runningAppProcessInfos);
        ((ListView) ui_element).setAdapter(adapter);
        break;
      case "DevicePowerThread":
        float batteryPct = (float) extraMsg;
        ((TextView) ui_element).setText(String.format("Current Battery Level: %s", batteryPct));
        break;
      case "CPU_USAGE_THREAD":

        float cpuUsagePercentageAsDecimal = (float) extraMsg;
//        Log.d("DEBUG", "CPU_Thread Runs " + String.valueOf(cpuUsagePercentageAsDecimal));
        //Multiply decimal by 100 to get percentage, cast to an int which causes the loss of some percision
//        int percent = (int) (cpuUsagePercentageAsDecimal * 100) / 100;
//        Log.d("Gen Percent", String.valueOf(percent));
        ((TextView) ui_element).setText(String.format("CPU: " + String.valueOf(cpuUsagePercentageAsDecimal)));
        break;

      default:
        Log.e(TAG, "Invalid thread name");
    }
  }
}