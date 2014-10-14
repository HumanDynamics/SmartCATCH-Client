package edu.mit.media.hellopds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class DialogActivity extends Activity {

	String title, questionType, description, posButton, negButton;
	int numItems, numRepeats, repeatTime, delayCount;
	String[] items;
	FragmentTransaction manage;
	static GlucoseFragment glucose;
	static OnClickListener d;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);

		delayCount = 0;
		
		Intent intent = getIntent();
		//delayCount = intent.getIntExtra("dialog_delay_count", 0);
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		posButton = intent.getStringExtra("posButton");
		negButton = intent.getStringExtra("negButton");
		numRepeats = intent.getIntExtra("numRepeats", 0);
		repeatTime = intent.getIntExtra("repeatTime", 0);
		items = intent.getStringArrayExtra("items");
		
		manage = getFragmentManager().beginTransaction();
		glucose = new GlucoseFragment();
		glucose.show(manage, null);
	}
	
	@SuppressLint("ValidFragment")
	public class GlucoseFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        
	        String[] array = new String[items.length + 1];
	        for(int i = 0; i < items.length; i++) {
	        	array[i + 1] = items[i];
	        }
	        if(delayCount == 0) { array[0] = "Not yet measured (Delay)"; }
	        else if(delayCount == numRepeats - 1) { array[0] = "Not yet measured (Final delay)"; }
	        else if(delayCount == numRepeats) { array[0] = "Did not measure"; }
	        
	        builder.setItems(array, d = new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0) {
						delayCount++;
						
						finish();
					}
					else {
						MainActivity.onDialogGlucoseClick(items[which]);
						finish();
					}
					
				}
			})
	        	   .setTitle(title);
	        // Create the AlertDialog object and return it
	        return builder.create();
			
		}
	
		public void onCancel(DialogInterface dialog) {
			//MainActivity.onDialogDelayClick();
			delayCount++;
			if(delayCount <= numRepeats) {
				try {
					Thread.sleep(repeatTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.show(manage, null);
			}
			else {
				finish();
			}
		}
	
	}

	public static void cancel() {
		glucose.onCancel((DialogInterface) d);
	}

}
