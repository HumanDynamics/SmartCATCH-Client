package edu.mit.media.hellopds;

import edu.mit.media.openpds.client.PersonalDataStore;
import edu.mit.media.openpds.client.PreferencesWrapper;
import edu.mit.media.openpds.client.RegistryClient;
import edu.mit.media.openpds.client.UserLoginTask;
import edu.mit.media.openpds.client.UserRegistrationTask;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    
	PersonalDataStore mPds;
	Context context;
	SharedPreferences sharedPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Variables
		context = getApplicationContext();
		mPds = null;
		final TextView textView = (TextView) findViewById(R.id.mainFragmentTextView);
		
		// Login the user or register
		try {
			mPds = new PersonalDataStore(this);
			addStatsFragment();
			textView.setText("User was previously authenticated");
		} catch (Exception ex) {
//			RegistryClient registryClient = new RegistryClient(
//				"http://mgh.media.mit.edu:80", // URL for registry server
//				"57f2ea5316e133b6e8d124deef4a9d", // Client Key / ID
//				"82399fa78d61a05ef43364be0ec6df", // Client Secret
//				"funf_write", // space-separated list of pre-existing scopes on registry server
//				"Basic NTdmMmVhNTMxNmUxMzNiNmU4ZDEyNGRlZWY0YTlkOjgyMzk5ZmE3OGQ2MWEwNWVmNDMzNjRiZTBlYzZkZg==");

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
					textView.setText("Registration Succeeded");
				}

				@Override
				protected void onError() {

					textView.setText("An error occurred while registering");
				}
			};

			// Below - login flow for pre-existing user
			UserLoginTask userLoginTask = new UserLoginTask(this, prefs,
					registryClient) {
				@Override
				protected void onComplete() {
					textView.setText("Login Succeeded");
					try {
						mPds = new PersonalDataStore(MainActivity.this);
						addStatsFragment();
					} catch (Exception e) {
						Log.w("HelloPDS", "Unable to construct PDS after login");
					}
				}

				@Override
				protected void onError() {
					textView.setText("An error occurred while logging in");
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
			
			//userRegistrationTask.execute("John Doe2", "another1223@test.com", "failsafe");

		}
	}

	private void addStatsFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.mainActivityLinearLayout,
						WebViewFragment.Create(
								mPds.buildAbsoluteUrl("/visualization/probe_counts"),
								"Probe Counts", this, null)).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static void onDialogGlucoseClick(String string) {
		// TODO Auto-generated method stub
		
	}

}
