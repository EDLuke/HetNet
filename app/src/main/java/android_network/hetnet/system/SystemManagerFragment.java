package android_network.hetnet.system;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.R;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;

import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

public class SystemManagerFragment extends Fragment {
  private OnFragmentInteractionListener mListener;

  public SystemManagerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment SystemManagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SystemManagerFragment newInstance() {
    SystemManagerFragment fragment = new SystemManagerFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //register to event bus
    EventBus.getDefault().register(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_system_manager, container, false);
    return view;
  }

  // TODO: Figure out what does this do
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
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
    if (event.getEventOriginator().equals(SYSTEM_LIST_FETCHER)) {
      SystemList list = (SystemList)(event.getEventList());
      //TODO: parse SystemList
    }
  }

}
