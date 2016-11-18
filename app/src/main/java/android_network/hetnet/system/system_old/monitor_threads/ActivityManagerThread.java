package android_network.hetnet.system.system_old.monitor_threads;

import android.app.ActivityManager;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import android_network.hetnet.system.system_old.event.ActivityManagerInfoEvent;


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

