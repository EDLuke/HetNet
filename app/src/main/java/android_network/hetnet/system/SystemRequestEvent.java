package android_network.hetnet.system;

/**
 * SystemRequestEvent
 * Request SystemLsitFetcher for SystemList
 */

public class SystemRequestEvent {
    private String m_eventOriginator;

    public SystemRequestEvent(String eventOriginator) {
        this.m_eventOriginator = eventOriginator;
    }

    public String getEventOriginator() {
        return m_eventOriginator;
    }

    public void setEventOriginator(String eventOriginator) {
        this.m_eventOriginator = eventOriginator;
    }
}
