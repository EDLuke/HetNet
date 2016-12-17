package android_network.hetnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddPolicyActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_policy);
  }

  public void showPolicyEnginePage(View view) {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  public void showAddPolicyOptions(View view) {
    TextView policyOptionText = (TextView) findViewById(R.id.policy_option_text);
    RadioButton wifiOnlyOption = (RadioButton) findViewById(R.id.wifi_only_option);
    RadioButton cellularOnlyOption = (RadioButton) findViewById(R.id.cellular_only_option);
    Button addPolicyButton = (Button) findViewById(R.id.add_policy_button);

    policyOptionText.setVisibility(View.VISIBLE);
    wifiOnlyOption.setVisibility(View.VISIBLE);
    cellularOnlyOption.setVisibility(View.VISIBLE);
    addPolicyButton.setVisibility(View.VISIBLE);

    Spinner policyLevels = (Spinner) findViewById(R.id.policy_group_options);
    String policyLevel = String.valueOf(policyLevels.getSelectedItem());
    RelativeLayout applicationType = (RelativeLayout) findViewById(R.id.application_type);
    RelativeLayout application = (RelativeLayout) findViewById(R.id.application);

    if (policyLevel.equals("Application Type Level Policy")) {
      applicationType.setVisibility(View.VISIBLE);
      application.setVisibility(View.INVISIBLE);
    } else if (policyLevel.equals("Application Specific Policy")) {
      application.setVisibility(View.VISIBLE);
      applicationType.setVisibility(View.INVISIBLE);
    }
  }

  public void onCheckboxClicked(View view) {

  }
}