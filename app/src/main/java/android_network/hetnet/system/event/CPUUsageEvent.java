package android_network.hetnet.system.event;

import java.io.IOException;
import java.io.RandomAccessFile;

import android_network.hetnet.system.event.ThreadInfoEvent;

/**
 * Created by gabe on 11/9/16.
 */

public class CPUUsageEvent extends ThreadInfoEvent {

        String m_thread_name;
        float cpuUsage;

    public CPUUsageEvent(String threadName, String message, float cpu) {
        super(threadName, message);
        this.m_thread_name = threadName;
        this.cpuUsage = cpu;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }



}
