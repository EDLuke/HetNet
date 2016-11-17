package android_network.hetnet.common.trigger_events;

import java.util.Date;

/**
 * Created by abroy240484 on 11/17/16.
 */

public class UITriggerEvent {
  public String eventOriginator;
  public String eventName;
  public Date timeOfEvent;

  public UITriggerEvent(String eventOriginator, String eventName, Date timeOfEvent) {
    this.eventOriginator = eventOriginator;
    this.eventName = eventName;
    this.timeOfEvent = timeOfEvent;
  }
}
