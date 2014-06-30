package edu.mit.media.smartcatch;

import edu.mit.media.openpds.client.PersonalDataStore;
import edu.mit.media.openpds.client.PreferencesWrapper;
import edu.mit.media.openpds.client.RegistryClient;
import edu.mit.media.openpds.client.UserLoginTask;
import edu.mit.media.openpds.client.UserRegistrationTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends FragmentActivity {

	PersonalDataStore mPds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPds = null;
		
		final TextView textView = (TextView) findViewById(R.id.mainFragmentTextView);
		
		
		try {
			mPds = new PersonalDataStore(this);
			addStatsFragment();
			textView.setText("User was previously authenticated");
		} catch (Exception ex) {
			RegistryClient registryClient = new RegistryClient(
					"http://mgh.media.mit.edu:80",	//URL for registry server
					"7e28f61622ae5d11993c65cddd755c", 	// Client Key / ID
					"0687ee9e888ff04bd181e6e8583641", 	// Client Secret
					"funf_write",						// space-separated list of pre-existing scopes on registry server
					"Basic N2UyOGY2MTYyMmFlNWQxMTk5M2M2NWNkZGQ3NTVjOjA2ODdlZTllODg4ZmYwNGJkMTgxZTZlODU4MzY0MQ==");				
			
			PreferencesWrapper prefs = new PreferencesWrapper(this);
			
			// Below - login flow for pre-existing user
			UserLoginTask userLoginTask = new UserLoginTask(this,  prefs, registryClient) {
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
					
					// If an error occurred, maybe the user hasn't been registered yet?
					// For cases where auto-registration is desired, call UserRegistrationTask here
				}
			};
			userLoginTask.execute("dummy@user.com", "dummy");
			 
//			// Commented out - Login flow for new user registration
//			UserRegistrationTask userRegistrationTask = new UserRegistrationTask(this, prefs, registryClient) {
//				@Override
//				protected void onComplete() {
//					textView.setText("Registration Succeeded");
//				}
//				
//				@Override
//				protected void onError() {
//					textView.setText("An error occurred while registering");
//				}
//			};
//			userRegistrationTask.execute("FirstName LastName", "newUser@test.com", "testpassword");
		}
	}
	
	private void addStatsFragment() {
		getSupportFragmentManager().beginTransaction().add(R.id.mainActivityLinearLayout, WebViewFragment.Create(mPds.buildAbsoluteUrl("/visualization/probe_counts"), "Probe Counts", this, null)).commit();
	}

}
