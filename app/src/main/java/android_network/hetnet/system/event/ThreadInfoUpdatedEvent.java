package android_network.hetnet.system.event;

import android.view.View;

/**
 * ThreadInfoUpdatedEvent
 */
public class ThreadInfoUpdatedEvent {
  public final String m_thread_name;
  public final String m_message;
  public final View   m_ui;
  public final Object m_extraMsg;

  public ThreadInfoUpdatedEvent(String threadName, String message, View view, Object extraMsg) {
    this.m_thread_name = threadName;
    this.m_message = message;
    this.m_extraMsg = extraMsg;
    this.m_ui       = view;
  }
}
