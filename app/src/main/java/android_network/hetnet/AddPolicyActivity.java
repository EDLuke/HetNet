package android_network.hetnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
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
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.policy_options);

    policyOptionText.setVisibility(View.VISIBLE);
    wifiOnlyOption.setVisibility(View.VISIBLE);
    cellularOnlyOption.setVisibility(View.VISIBLE);
    addPolicyButton.setVisibility(View.VISIBLE);

    radioGroup.clearCheck();

    Spinner policyLevels = (Spinner) findViewById(R.id.policy_group_options);
    String policyLevel = String.valueOf(policyLevels.getSelectedItem());
    RelativeLayout applicationType = (RelativeLayout) findViewById(R.id.application_type);
    RelativeLayout application = (RelativeLayout) findViewById(R.id.application);

    switch (policyLevel) {
      case "General Level Policy":
        application.setVisibility(View.INVISIBLE);
        applicationType.setVisibility(View.INVISIBLE);
        break;
      case "Application Type Level Policy":
        application.setVisibility(View.INVISIBLE);
        applicationType.setVisibility(View.VISIBLE);
        break;
      default:
        applicationType.setVisibility(View.INVISIBLE);
        application.setVisibility(View.VISIBLE);
        break;
    }
  }

  public void onCheckboxClicked(View view) {

  }

  public void addPolicy(View view) {
    Spinner policyLevels = (Spinner) findViewById(R.id.policy_group_options);
    String policyLevel = String.valueOf(policyLevels.getSelectedItem());
    TableLayout generalPolicyTable = (TableLayout) findViewById(R.id.policy_table_general);
    TableLayout appTypePolicyTable = (TableLayout) findViewById(R.id.policy_table_app_type);
    TableLayout appSpecificPolicyTable = (TableLayout) findViewById(R.id.policy_table_app_specific);

    switch (policyLevel) {
      case "General Level Policy":
        generalPolicyTable.setVisibility(View.VISIBLE);
        break;
      case "Application Type Level Policy":
        generalPolicyTable.setVisibility(View.INVISIBLE);
        appTypePolicyTable.setVisibility(View.VISIBLE);
        break;
      default:
        generalPolicyTable.setVisibility(View.INVISIBLE);
        appTypePolicyTable.setVisibility(View.INVISIBLE);
        appSpecificPolicyTable.setVisibility(View.VISIBLE);
        break;
    }
  }
}