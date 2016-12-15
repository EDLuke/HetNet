package android_network.hetnet.ui.TabFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import android_network.hetnet.LocationManagerFragment;
import android_network.hetnet.NetworkManagerFragment;
import android_network.hetnet.R;
import android_network.hetnet.SystemManagerFragment;

public class TabFragment extends Fragment {
  private static final String LOG_TAG = "TabFragment";

  public static TabLayout m_tabLayout;
  public static ViewPager m_viewPager;

  private TabFragmentAdapter m_fragmentAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    /**
     *Inflate tab_layout and setup Views.
     */
    View x = inflater.inflate(R.layout.tab_layout, null);
    m_tabLayout = (TabLayout) x.findViewById(R.id.tabs);
    m_viewPager = (ViewPager) x.findViewById(R.id.viewpager);
    m_fragmentAdapter = new TabFragmentAdapter(getChildFragmentManager());
    m_fragmentAdapter.add(new SystemManagerFragment());
    m_fragmentAdapter.add(new NetworkManagerFragment());
    m_fragmentAdapter.add(new LocationManagerFragment());

    /**
     *Set an Apater for the View Pager
     */
    m_viewPager.setAdapter(m_fragmentAdapter);

    /**
     * Now , this is a workaround ,
     * The setupWithViewPager dose't works without the runnable .
     * Maybe a Support Library Bug .
     */
    m_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });

    m_tabLayout.setupWithViewPager(m_viewPager);
    return x;

  }

  class TabFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> m_fragment;

    public TabFragmentAdapter(FragmentManager fm) {
      super(fm);

      m_fragment = new ArrayList<Fragment>();
    }

    public void add(Fragment fragment) {
      this.m_fragment.add(fragment);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
      return m_fragment.get(position);
    }

    @Override
    public int getCount() {

      return m_fragment.size();

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

      switch (m_fragment.get(position).getClass().toString()) {
        case "class android_network.hetnet.SystemManagerFragment":
          return "System";
        case "class android_network.hetnet.NetworkManagerFragment":
          return "Network";
        case "class android_network.hetnet.LocationManagerFragment":
          return "Location";
        default:
          Log.e(LOG_TAG, "Invalid page title");
          return null;
      }
    }
  }

}
