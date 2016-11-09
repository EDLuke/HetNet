package android_network.hetnet.system.monitor_threads;

import android.content.Context;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Created by gabe on 10/23/16.
 */

public class CPUUsageThread extends Thread {

    TextView mUIElement;
    String pid;
    public CPUUsageThread(Context context, TextView ui_element_cpu) {
        mUIElement = ui_element_cpu;
    }

    public void run(){
        //Call the top command run grep, pull CPU usage then set textview

        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("top | grep " + pid + "\n");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
