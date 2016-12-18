package android_network.hetnet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android_network.hetnet.cloud.HttpService;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.Network;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;

import static android_network.hetnet.common.Constants.POLICY_ENGINE;

public class LocationManagerFragment extends Fragment {

  private OnFragmentInteractionListener mListener;
  TableLayout LocationNetworks;
  Context mycontext;
  TextView m_locationLogs;
  Spinner ranger;
  Button submitLN;
  EditText namefield;
  DataStoreObject data;

  List<String> SSID;

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment LocationManagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static LocationManagerFragment newInstance() {
    LocationManagerFragment fragment = new LocationManagerFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //register to event bus
    EventBus.getDefault().register(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_location_manager, container, false);
    //Hook up with UI
    m_locationLogs = (TextView) (view.findViewById(R.id.location_logs));
    LocationNetworks = (TableLayout) view.findViewById(R.id.LocationNetworks);
    ranger = (Spinner) view.findViewById(R.id.rangeselector);
    String[] rangeset = {"30", "50", "100"};
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mycontext, android.R.layout.simple_spinner_item, rangeset);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ranger.setAdapter(dataAdapter);
    submitLN = (Button) view.findViewById(R.id.submitLocationNetworks);
    namefield = (EditText) view.findViewById(R.id.LocationName);
    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mycontext = context;
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
        + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UITriggerEvent event) {
    if (event.getEventOriginator().equals(POLICY_ENGINE)) {
      data = ((PolicyEngineData) (event.getEvent())).getDataStoreObject();
      String dataString = String.format("Longtitude: %.2f\tLatitude:%.2f\t\nTime:%s\n", data.getLongitude(), data.getLatitude(), event.getTimeOfEvent().toString());
      m_locationLogs.setText(dataString);
      submitLN.setOnClickListener(submitlisten);
      // remove duplication
      List<Network> tempnet = data.getListOfNetworks();
      HashSet<String>SSIDname = new HashSet<>();
      for(Network net : tempnet){
        SSIDname.add(net.getNetworkSSID());
      }
      this.tableFormer(new ArrayList<String>(SSIDname));
    }
  }

  private CompoundButton.OnCheckedChangeListener checklisten = new CompoundButton.OnCheckedChangeListener() {
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
        // The toggle is enabled
        SSID.add((String) buttonView.getTag());
      } else {
        // The toggle is disabled
        SSID.remove(buttonView.getTag());
      }
    }
  };

  private Button.OnClickListener submitlisten = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      String LocationName = namefield.getText().toString();
      String LocationRange = ranger.getSelectedItem().toString();
      Map<String, Object> temp = new HashMap<>();
      temp.put("Name", LocationName);
      temp.put("Longtitude", data.getLongitude());
      temp.put("Latitude", data.getLatitude());
      temp.put("NetworkList", SSID);
      temp.put("Range", LocationRange);
      System.out.println(temp.toString());
      JSONObject myjson = new JSONObject(temp);
      System.out.println(myjson.toString());
    }
  };

  public void tableFormer(List<String> Networks) {
    int i = 0;
    SSID = new ArrayList<>();
    for (String network : Networks) {
      TableRow row = new TableRow(mycontext);
      TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
      row.setLayoutParams(lp);
      final TextView netstring = new TextView(mycontext);
      netstring.setText(network);
      final ToggleButton toggle = new ToggleButton(mycontext);
      toggle.setTag(network);
      toggle.setTextOn("Added");
      toggle.setOnCheckedChangeListener(checklisten);
      row.addView(netstring);
      row.addView(toggle);
      LocationNetworks.addView(row, i);
      i++;
    }
  }


}
