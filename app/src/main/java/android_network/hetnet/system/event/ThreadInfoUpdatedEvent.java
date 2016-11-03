package android_network.hetnet.system.event;

import android.view.View;

/**
 * Created by lukez_000 on 11/02/2016.
 */
public class ThreadInfoUpdatedEvent extends ThreadInfoEvent {
  public final View   m_ui;
  public final Object m_extraMsg;

  public ThreadInfoUpdatedEvent(String threadName, String message, View view, Object extraMsg) {
    super(threadName, message);

    this.m_extraMsg = extraMsg;
    this.m_ui       = view;
  }
}
