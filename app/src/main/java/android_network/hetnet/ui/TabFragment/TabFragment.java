package android_network.hetnet.ui.TabFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android_network.hetnet.LocationManagerFragment;
import android_network.hetnet.NetworkManagerFragment;
import android_network.hetnet.R;
import android_network.hetnet.SystemManagerFragment;

public class TabFragment extends Fragment {

  public static TabLayout tabLayout;
  public static ViewPager viewPager;
  public static int int_items = 3;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    /**
     *Inflate tab_layout and setup Views.
     */
    View x = inflater.inflate(R.layout.tab_layout, null);
    tabLayout = (TabLayout) x.findViewById(R.id.tabs);
    viewPager = (ViewPager) x.findViewById(R.id.viewpager);

    /**
     *Set an Apater for the View Pager
     */
    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

    /**
     * Now , this is a workaround ,
     * The setupWithViewPager dose't works without the runnable .
     * Maybe a Support Library Bug .
     */
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        TabLayout.Tab temp = tab;
        boolean f = false;
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
        TabLayout.Tab temp = tab;
        boolean f = false;
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        TabLayout.Tab temp = tab;
        boolean f = false;
      }
    });

    tabLayout.setupWithViewPager(viewPager);
    return x;

  }

  class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new SystemManagerFragment();
        case 1:
          return new NetworkManagerFragment();
        case 2:
          return new LocationManagerFragment();
      }
      return null;
    }

    @Override
    public int getCount() {

      return int_items;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

      switch (position) {
        case 0:
          return "System";
        case 1:
          return "Network";
        case 2:
          return "Location";
      }
      return null;
    }
  }

}
