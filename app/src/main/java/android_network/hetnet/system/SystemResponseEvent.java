package android_network.hetnet.system;

import java.util.Date;

/**
 * SystemResponseEvent
 * Contains current SystemList
 */

public class SystemResponseEvent {
  private String m_eventOriginator;
  private SystemList m_systemList;
  private Date m_timeOfEvent;

  public SystemResponseEvent(String eventOriginator, SystemList systemList, Date timeOfEvent) {
    this.setEventOriginator(eventOriginator);
    this.setSystemList(systemList);
    this.setTimeOfEvent(timeOfEvent);
  }

  public String getEventOriginator() {
    return m_eventOriginator;
  }

  public void setEventOriginator(String m_eventOriginator) {
    this.m_eventOriginator = m_eventOriginator;
  }

  public SystemList getSystemList() {
    return m_systemList;
  }

  public void setSystemList(SystemList m_systemList) {
    this.m_systemList = m_systemList;
  }

  public Date getTimeOfEvent() {
    return m_timeOfEvent;
  }

  public void setTimeOfEvent(Date m_timeOfEvent) {
    this.m_timeOfEvent = m_timeOfEvent;
  }
}
