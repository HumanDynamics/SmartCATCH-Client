package edu.mit.media.hellopds;

import edu.mit.media.openpds.client.PersonalDataStore;
import edu.mit.media.openpds.client.PreferencesWrapper;
import edu.mit.media.openpds.client.RegistryClient;
import edu.mit.media.openpds.client.UserLoginTask;
import edu.mit.media.openpds.client.UserRegistrationTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class SplashActivity extends FragmentActivity {

	private ListView mDrawerList;
	private String[] mNavList = {"Quick Look", "My Results", "History", "Questions", "Settings"};
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	PersonalDataStore mPds;
	Context context;
	SharedPreferences sharedPref;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// Get the action
		Intent intent = getIntent();
		String action = intent.getStringExtra("action");
		
		getActionBar().setTitle("");
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mNavList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.drawable.ic_launcher, R.string.app_name, R.string.app_name) {
        		R.drawable.menu, R.string.empty_string, R.string.empty_string) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setBackground(new ColorDrawable(0x1f3f56));
        
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.logo_blue));
		ImageView v = (ImageView)findViewById(android.R.id.home);
		v.setPadding(30, 0, 0, 0);
//		View view = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
//		ImageView iView = (ImageView) findViewById(R.id.imageView1);
//		view.setBackgroundColor(Color.rgb(120, 242, 182)); //TODO: approximate, replace values
        
		
		mPds = null;
		// Check if we need to register
		context = getApplicationContext();
		sharedPref = context.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
		
		Log.e("message", "Started activity");
		
		// Enter user details and store them locally - doesn't really register
		if(sharedPref.getString("USER_ID", null) == null) {
			Log.e("message", "Needs to register");
			Intent i = new Intent(context, RegistrationActivity.class);
			startActivity(i);
		}
		
		// TODO: ???
//		view.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(context, MainActivity.class);
//				startActivity(i);
//			}
//		});
		
		// Login the user or register
		try {
			mPds = new PersonalDataStore(this);
//			addStatsFragment();
//			textView.setText("User was previously authenticated"); // TODO: use toast here.
			Log.d("HelloPDS", "User was previously authenticated");
			String actionUrl = "/visualization/smartcatch/splash"; 
			if (action != null) {
				actionUrl = "/visualization/smartcatch/questions?instance=" + action;
			}
			String url = mPds.buildAbsoluteUrl(actionUrl);
			addWebViewFragment(url);
		} catch (Exception ex) {
			regServerLoginRegister();
//			addStatsFragment();
		}
		
	}

	private void regServerLoginRegister() {

//		RegistryClient registryClient = new RegistryClient(
//		"http://mgh.media.mit.edu:80", // URL for registry server
//		"57f2ea5316e133b6e8d124deef4a9d", // Client Key / ID
//		"82399fa78d61a05ef43364be0ec6df", // Client Secret
//		"funf_write", // space-separated list of pre-existing scopes on registry server
//		"Basic NTdmMmVhNTMxNmUxMzNiNmU4ZDEyNGRlZWY0YTlkOjgyMzk5ZmE3OGQ2MWEwNWVmNDMzNjRiZTBlYzZkZg==");
	
		RegistryClient registryClient = new RegistryClient(
				"http://mgh.media.mit.edu:80",	//URL for registry server
				"7e28f61622ae5d11993c65cddd755c", 	// Client Key / ID
				"0687ee9e888ff04bd181e6e8583641", 	// Client Secret
				"funf_write",						// space-separated list of pre-existing scopes on registry server
				"Basic N2UyOGY2MTYyMmFlNWQxMTk5M2M2NWNkZGQ3NTVjOjA2ODdlZTllODg4ZmYwNGJkMTgxZTZlODU4MzY0MQ==");	

		PreferencesWrapper prefs = new PreferencesWrapper(this);

		// Below - Registration for new user
		final UserRegistrationTask userRegistrationTask = new UserRegistrationTask(
				this, prefs, registryClient) {
			@Override
			protected void onComplete() {
//				textView.setText("Registration Succeeded");
				Log.d("HelloPDS", "Registration Succeeded");
				// TODO: set status bar
			}

			@Override
			protected void onError() {

//				textView.setText("An error occurred while registering");
				Log.d("HelloPDS", "An error occurred while registering");
				// TODO: set status bar
			}
		};

		// Below - login flow for pre-existing user
		UserLoginTask userLoginTask = new UserLoginTask(this, prefs,
				registryClient) {
			@Override
			protected void onComplete() {
//				textView.setText("Login Succeeded");
				Log.d("HelloPDS", "Login Succeeded");
				// TODO: set status bar
				try {
					mPds = new PersonalDataStore(SplashActivity.this);
					String url = mPds.buildAbsoluteUrl("/visualization/smartcatch/splash");
					addWebViewFragment(url);
				} catch (Exception e) {
					Log.w("HelloPDS", "Unable to construct PDS after login");
				}
			}

			@Override
			protected void onError() {
//				textView.setText("An error occurred while logging in");
				Log.d("HelloPDS", "An error occurred while logging in");
				// TODO: set status bar
			}
		};
		
		// Register user
		if(sharedPref.getBoolean("NEEDS_TO_REGISTER", false) == true) {
			userRegistrationTask.execute(sharedPref.getString("USER_ID", null), sharedPref.getString("USER_EMAIL", null), sharedPref.getString("USER_PASSWORD", null));
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putBoolean("NEEDS_TO_REGISTER", false);
			editor.commit();
			Log.e("message", "Registered, set false");
		}
		// Login for the user
		else {
			userLoginTask.execute(sharedPref.getString("USER_EMAIL", null), sharedPref.getString("USER_PASSWORD", null));
		}
		
	}

	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        Intent i;
	        Context context = getApplicationContext();
	        String url;
	        switch(position) {
	        case 0: 
//	        	i = new Intent(context, SplashActivity.class);
//	        	startActivity(i);
				url = mPds.buildAbsoluteUrl("/visualization/smartcatch/splash");
//	        	url = "http://www.google.com";
				addWebViewFragment(url);
				mDrawerLayout.closeDrawers();
	        break;
	        case 1: 
//	        	i = new Intent(context, MainActivity.class); //TODO: Implement
//	        	startActivity(i);
				url = mPds.buildAbsoluteUrl("/visualization/smartcatch/myresults");
//	        	url = "http://www.google.com";
				addWebViewFragment(url);
				mDrawerLayout.closeDrawers();
        	break;
	        case 2: 
//	        	i = new Intent(context, MainActivity.class); //TODO: Implement
//	        	startActivity(i);
				url = mPds.buildAbsoluteUrl("/visualization/smartcatch/history");
//	        	url = "http://www.yahoo.com";
				addWebViewFragment(url);
				mDrawerLayout.closeDrawers();
        	break;
	        case 3: 
//	        	i = new Intent(context, MainActivity.class); //TODO: Implement
//	        	startActivity(i);
				url = mPds.buildAbsoluteUrl("/visualization/smartcatch/questions");
//	        	url = "http://www.cnn.com";
				addWebViewFragment(url);
				mDrawerLayout.closeDrawers();
        	break;
	        case 4: 
	        	i = new Intent(context, SettingsActivity.class); //TODO: Implement
	        	startActivity(i);
        	break;
        	default: break; //does nothing
	        }
	    }
	}

	public void onClickSettingsBar(View v) {
		 Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		 startActivity(i);
	}

	private void addWebViewFragment(String url) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.splashActivityRelativeLayout,
						WebViewFragment.Create(
//								mPds.buildAbsoluteUrl("/visualization/probe_counts"),
//								mPds.buildAbsoluteUrl("/visualization/smartcatch/myresults"),
								url,
								"Probe Counts", this, null)).commit();
	}
}


