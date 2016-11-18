package android_network.hetnet.system.system_old.event;

/**
 * Created by lukez_000 on 11/02/2016.
 */
public class ThreadInfoEvent {
  public String m_thread_name;
  public String m_message;

  public ThreadInfoEvent(String threadName, String message) {
    this.m_thread_name = threadName;
    this.m_message = message;
  }
}
