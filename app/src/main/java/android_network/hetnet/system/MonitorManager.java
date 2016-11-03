package android_network.hetnet.system;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class MonitorManager {
  /**
   * Log Tag
   */
  private static final String TAG = "MonitorManager";
  private Queue<Thread> m_thread_queue;
  private boolean m_running;
  //private MonitorService m_service;

  public MonitorManager() {
    m_thread_queue = new LinkedList<Thread>();
    m_running = true;

    //TODO: Migrate to Service
    //m_service = new Intent(this, MonitorService.class);
  }

  public void insertNewThread(Thread thread) {
    m_thread_queue.add(thread);
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
}