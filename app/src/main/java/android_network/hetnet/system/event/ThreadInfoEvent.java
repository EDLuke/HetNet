package android_network.hetnet.system.event;

/**
 * Created by lukez_000 on 11/02/2016.
 */
public class ThreadInfoEvent {
  public final String m_thread_name;
  public final String m_message;

  public ThreadInfoEvent(String threadName, String message) {
    this.m_thread_name = threadName;
    this.m_message = message;
  }
}
