package edu.ub.pis2014.pis_09.kirolari;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import edu.ub.pis2014.pis_09.activitat.MenuPrincipal;
import edu.ub.pis2014.pis_09.adapter.NavDrawerItem;
import edu.ub.pis2014.pis_09.adapter.NavDrawerListAdapter;
import edu.ub.pis2014.pis_09.amics.Amics;
import edu.ub.pis2014.pis_09.amics.Cuidam;
import edu.ub.pis2014.pis_09.esdeveniments.Esdeveniments;
import edu.ub.pis2014.pis_09.planning.Historial;
import edu.ub.pis2014.pis_09.planning.NouPlanning;

public class KirolariActivity extends FragmentActivity {
	public static final String PREFS_NAME = "Settings";
	int mode = Activity.MODE_PRIVATE;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private Kirolari control;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kirolari);

		control = (Kirolari) getApplicationContext();
		mTitle = mDrawerTitle = getTitle();
		
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.opcions);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.opcions_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Menu Principal
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Calendari
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Historial
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Nou Planning
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Cuida'm
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// Amics
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		// Esdeveniments
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch(item.getItemId()) {
	        case R.id.action_settings:
	            Intent intent = new Intent(KirolariActivity.this, Settings.class);
	            startActivity(intent);
	            break;
	
	        default:
	            return super.onOptionsItemSelected(item);
		}

        return false;
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		Bundle args;
    	FragmentManager fragmentManager;
    	Intent i;
		switch (position) {
		case 0:		// Menu Principal
			Fragment fragment_menuPrincipal = new MenuPrincipal();
			args = new Bundle();
		    args.putInt(MenuPrincipal.ARG_OPTION_NUMBER, position);
		    fragment_menuPrincipal.setArguments(args);
		    
		    fragmentManager = getSupportFragmentManager();
		    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_menuPrincipal).addToBackStack(null).commit();
		  // update selected item and title, then close the drawer
		    mDrawerList.setItemChecked(position, true);
		    setTitle(navMenuTitles[position]);
		    mDrawerLayout.closeDrawer(mDrawerList);
			break;
		
		case 1:		// Calendari	
			try{
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity"));
				startActivity(intent);
			}
			catch(Exception e){
				try{
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity"));
					startActivity(intent);
				}
				catch(Exception e1){
					// alert Dialog
	                AlertDialog.Builder alertDialog = new AlertDialog.Builder(KirolariActivity.this);
					 
	 		       // Setting Dialog Message
	 		        alertDialog.setMessage(getString(R.string.text_error_calendar));
	 		 
	 		        // Setting Positive Button
	 		        alertDialog.setPositiveButton(getString(R.string.si),
	 		        		new DialogInterface.OnClickListener() {
	 				            public void onClick(DialogInterface dialog,int which) {
	 				            	//No fem res, nomes informem
	 				            }
	 		        		});
	 		        // Showing Alert Message
	 		        alertDialog.show();
				}
				
			}
			break;
		
		case 2:		// Historial
			if(control.estaConectat()){
				Fragment fragment_historial = new Historial();
				args = new Bundle();
			    args.putInt(Historial.ARG_OPTION_NUMBER, position);
			    fragment_historial.setArguments(args);
			    
			    fragmentManager = getSupportFragmentManager();
			    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_historial).addToBackStack(null).commit();
			     
			  //update selected item and title, then close the drawer
			    mDrawerList.setItemChecked(position, true);
			    setTitle(navMenuTitles[position]);
			    mDrawerLayout.closeDrawer(mDrawerList);
			}else{
				noInternet();
			}
				
			break;
			
		//Nou Planning
		case 3:
			if(control.estaConectat()){
				Fragment fragment_nou_planning = new NouPlanning();
	   			args = new Bundle();
	   		    args.putInt(NouPlanning.ARG_OPTION_NUMBER, position);
	   		    fragment_nou_planning.setArguments(args);
	   		    
	   		    fragmentManager = getSupportFragmentManager();
	   		    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_nou_planning).addToBackStack(null).commit();
	   		     
	   		    //update selected item and title, then close the drawer
	   		    mDrawerList.setItemChecked(position, true);
	   		    setTitle(navMenuTitles[position]);
	   		    mDrawerLayout.closeDrawer(mDrawerList);
			}else{
				noInternet();
			}
			break;
			
		case 4:		//Cuida'm
			Fragment fragment_cuidam = new Cuidam();
   			args = new Bundle();
   		    args.putInt(Cuidam.ARG_OPTION_NUMBER, position);
   		    fragment_cuidam.setArguments(args);
   		    
   		    fragmentManager = getSupportFragmentManager();
   		    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_cuidam).addToBackStack(null).commit();
   		     
   		  //update selected item and title, then close the drawer
   		    mDrawerList.setItemChecked(position, true);
   		    setTitle(navMenuTitles[position]);
   		    mDrawerLayout.closeDrawer(mDrawerList);
			break;
		
		case 5:		// Amics
			if(control.estaConectat()){
				Fragment fragment_amics = new Amics();
	   			args = new Bundle();
	   		    args.putInt(Amics.ARG_OPTION_NUMBER,position);
	   		    fragment_amics.setArguments(args);
	   		    
	   		    fragmentManager = getSupportFragmentManager();
	   		    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment_amics).addToBackStack(null).commit();
	   		     
	   		  //update selected item and title, then close the drawer
	   		    mDrawerList.setItemChecked(position, true);
	   		    setTitle(navMenuTitles[position]);
	   		    mDrawerLayout.closeDrawer(mDrawerList);
			}else{
				noInternet();
			}
			break;
		
		case 6:		//Esdeveniments
			if(control.estaConectat()){
				i = new Intent(KirolariActivity.this,Esdeveniments.class);
				startActivity(i);
			}else{
				noInternet();
			}
			break;
		default:
			break;
		}
		
	}

	private void noInternet() {
		CharSequence text = getString(R.string.text_error_internet);
    	Toast.makeText(KirolariActivity.this, text, Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
}
