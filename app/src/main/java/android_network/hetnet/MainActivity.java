package android_network.hetnet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Creates System Manager Activity */
    public void startSystemManager(View view){
        Intent intent = new Intent(this, SystemManager_Main.class);
        startActivity(intent);
    }

    /** Creates Network Manager Activity */
    public void startNetworkManager(View view){
        Intent intent = new Intent(this, NetworkManager_Main.class);
        startActivity(intent);
    }

}
