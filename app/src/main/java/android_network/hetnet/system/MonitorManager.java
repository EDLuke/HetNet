package android_network.hetnet.system;

import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import android_network.hetnet.system.event.ActivityManagerInfoEvent;
import android_network.hetnet.system.event.CPUUsageEvent;
import android_network.hetnet.system.event.DevicePowerThreadInfoEvent;
import android_network.hetnet.system.event.ThreadInfoEvent;
import android_network.hetnet.system.event.ThreadInfoUpdatedEvent;

public class MonitorManager {
  /**
   * Log Tag
   */
  private static final String TAG = "MonitorManager";
  private Queue<Thread> m_thread_queue;
  private HashMap<String, View> m_thread_ui_map;

  private boolean m_running;

  public MonitorManager() {
    m_thread_queue = new LinkedList<Thread>();
    m_thread_ui_map = new HashMap<String, View>();
    m_running = true;

    //Register to the eventbus
    EventBus.getDefault().register(this);
  }

  public void insertNewThread(Thread thread, String threadName, View view) {
    m_thread_queue.add(thread);
    m_thread_ui_map.put(threadName, view);
  }

  /* Run all the threads in a rotational fashion*/
  public void startMonitor() {
    Thread monitorThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while (m_running) {
            Thread currentThread = m_thread_queue.remove();
            currentThread.run();
            currentThread.join();
            m_thread_queue.add(currentThread);

            Thread.sleep(1000);
          }
        } catch (InterruptedException e) {
          Log.e(TAG, "Thread interrupted");
        }
      }
    });

    monitorThread.start();
  }


  @Subscribe
  public void onMessageEvent(ThreadInfoEvent event){
    String threadName = event.m_thread_name;
    String message    = event.m_message;
    View   ui_element = m_thread_ui_map.get(threadName);
    Object extraMsg   = null;

    switch (threadName){
      case "ActivityManagerThread":
        extraMsg = ((ActivityManagerInfoEvent)event).m_runningAppProcessInfos;
        break;
      case "DevicePowerThread":
        extraMsg = ((DevicePowerThreadInfoEvent)event).m_batteryPct;
        break;
      case "CPU_USAGE_THREAD":
        extraMsg = ((CPUUsageEvent)event).getCpuUsage();
        Log.d("CPU_DEBUG", String.valueOf(extraMsg));
        break;
      default:
        Log.e(TAG, "Incorrect thread name received in Monitor Manager");
    }

    EventBus.getDefault().post(new ThreadInfoUpdatedEvent(threadName, message, ui_element, extraMsg));

  }

}