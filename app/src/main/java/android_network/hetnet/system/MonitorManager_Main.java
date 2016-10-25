package android_network.hetnet.system;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Monitor Manager
 *
 */
public class MonitorManager_Main {
  /** Log Tag */
  private static final String TAG = "MonitorManager";

  private Queue<Thread> m_thread_queue;
  private boolean       m_running;

  public MonitorManager_Main(){
    m_thread_queue = new LinkedList<>();
    m_running = true;
  }

  public void insertNewThread(Thread thread){
    m_thread_queue.add(thread);
  }

  /* Run all the threads in a rotational fashion*/
  public void startMonitor(){
    Thread monitorThread = new Thread(){
      @Override
      public void run(){
        try{
          while(m_running){
            Thread currentThread = m_thread_queue.remove();
            currentThread.run();
            currentThread.join();
            m_thread_queue.add(currentThread);

//            sleep(10000);
            m_running = false;
          }
        }catch(InterruptedException e){
          Log.e(TAG, "Thread interrupted");
        }
      }
    };

    monitorThread.run();
  }
}
