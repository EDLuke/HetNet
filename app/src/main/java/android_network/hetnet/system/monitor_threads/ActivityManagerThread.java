package android_network.hetnet.system.monitor_threads;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import android_network.hetnet.system.adapter.RunningApplicationListAdapter;
import android_network.hetnet.system.event.ActivityManagerInfoEvent;


/**
 * Activity Manager Thread
 */
public class ActivityManagerThread extends Thread {
  final String m_threadName = "ActivityManagerThread";

  Context m_context;

  public ActivityManagerThread(Context context) {
    m_context = context;
  }

  public void run() {
    ActivityManager am = (ActivityManager) (m_context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));

    //Post to event bus
    EventBus.getDefault().post(new ActivityManagerInfoEvent(m_threadName, "Success", am.getRunningAppProcesses()));
  }
}

