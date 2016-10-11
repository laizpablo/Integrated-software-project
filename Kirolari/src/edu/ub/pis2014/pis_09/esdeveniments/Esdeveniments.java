package edu.ub.pis2014.pis_09.esdeveniments;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.ub.pis2014.pis_09.adapter.Esdeveniments_TabsAdapter;
import edu.ub.pis2014.pis_09.kirolari.R;

public class Esdeveniments extends FragmentActivity implements ActionBar.TabListener {
	
	SectionsPagerAdapter mSectionsPagerAdapter;

	//The ViewPager that will host the section contents.
	private ViewPager viewPager;
	private Esdeveniments_TabsAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs={"Meus","Crear","Cercar"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events); 
        String titol = getResources().getString(R.string.esdeveniments);
        setTitle(titol);

        //Initialitzation
  		viewPager=(ViewPager) findViewById(R.id.pager_events);
  		actionBar= getActionBar();
  		mAdapter = new Esdeveniments_TabsAdapter(getSupportFragmentManager());
  		
  		viewPager.setAdapter(mAdapter);
  		actionBar.setHomeButtonEnabled(true);
  		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
  		
  		for (String tab_name: tabs) {
  			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
  		}
  		
  		/**
  		 * on swiping the viewpager make respective tab selected
  		 */
  		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
  			
  			@Override
  			public void onPageSelected(int position) {
  				// on changing the page make respected tab selected
  				actionBar.setSelectedNavigationItem(position);
  			}
  			
  			@Override
  			public void onPageScrolled(int arg0, float arg1, int arg2) {
  			}
  			
  			@Override
  			public void onPageScrollStateChanged(int arg0) {
  			}
  		});
  		
    }


	@Override
	public void onTabSelected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.section_tab_events).toUpperCase(l);
			case 1:
				return getString(R.string.section_tab_crear).toUpperCase(l);
			case 2:
				return getString(R.string.section_tab_buscar).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		// The fragment argument representing the section number for this fragment.
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_events_dummy,container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	@Override
	public void onBackPressed(){
		finish();
	}

}
