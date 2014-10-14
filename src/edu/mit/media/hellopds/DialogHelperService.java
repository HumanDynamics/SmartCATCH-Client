package edu.mit.media.hellopds;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

public class DialogHelperService extends IntentService { 
	
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private Context context;
	
	public DialogHelperService() {
		super("DialogHelperService");
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		
		context = getApplicationContext();
		sharedPref = context.getSharedPreferences("mgh_app_prefs", Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		
		editor.putInt("int_times_delayed", sharedPref.getInt("int_times_delayed", 0) + 1);
		editor.commit();
		
		final Intent intent = new Intent(this, DialogActivity.class);
		intent.putExtra("dialog_delay_count", sharedPref.getInt("int_times_delayed", 0));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(sharedPref.getInt("int_times_delayed", 0) < 4){
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(intent);
		}
		
	}

}
