package edu.mit.media.hellopds;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity {

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private TextView mErrorText;
	private EditText mUserIdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		// Set up views
		mEmailView = (EditText) findViewById(R.id.email);
		mErrorText = (TextView) findViewById(R.id.error_text);
		mPasswordView = (EditText) findViewById(R.id.password);
		mUserIdView = (EditText) findViewById(R.id.input_number);
		
		// Change error text
		mErrorText.setText(" ");
		
		// Set up button
		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegistration();
			}
		});

		

	}
	
	private void attemptRegistration() {
		String password = mPasswordView.getText().toString();
		String email = mEmailView.getText().toString();
		String user = mUserIdView.getText().toString();
		
		
		if(checkPassword(password) == true && checkEmail(email) == true && checkUser(user) == true) {
			//TODO pass info back to main method, probably by encrypting and saving to sharedpref
			Context context = getApplicationContext();
			SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
			
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("USER_EMAIL", email);
			editor.putString("USER_PASSWORD", password);
			editor.putBoolean("NEEDS_TO_REGISTER", true);
			editor.putString("USER_ID", user);
			editor.commit();
			Log.e("message", "Saved user data");
//			Intent i = new Intent(context, MainActivity.class);
			Intent i = new Intent(context, SplashActivity.class);
			startActivity(i);
		}
		
		
	}
	
	private boolean checkPassword(String password) {
		boolean isValid = true;
		
		if(password.length() < 4) {
			isValid = false;
			mErrorText.setText("Error: Password too short");
		}
		if(password.contains(" ")) {
			isValid = false;
			mErrorText.setText("Error: Password contains invalid character");
		}
		
		return isValid;
	}
	
	private boolean checkEmail(String email) {
		boolean isValid = true;
		
		if(!email.contains("@")) {
			isValid = false;
			mErrorText.setText("Error: Invalid email");
		}
		if(!email.contains(".")) {
			isValid = false;
			mErrorText.setText("Error: Invalid email");
		}
		
		return isValid;
	}

	private boolean checkUser(String user) {
		if(user != null) {
			return true;
		}
		else {
			return false;
		}
	}
}
