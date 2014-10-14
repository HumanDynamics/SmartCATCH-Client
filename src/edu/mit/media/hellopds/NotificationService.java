package edu.mit.media.hellopds;

import java.util.HashMap;
import java.util.Map;

import edu.mit.media.funf.util.LogUtil;
import edu.mit.media.openpds.client.PersonalDataStore;
import edu.mit.media.openpds.client.R;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class NotificationService extends IntentService {

//	String title, questionType, description, posButton, negButton, s1, s2, s3;
//	int numItems, numRepeats, repeatTime;
//	String[] items;
	private WakeLock mWakeLock;
    
	public NotificationService() {
		super("NotificationService");
	}
	
//	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onHandleIntent(Intent intent) {		
		//Intent openAppIntent = new Intent(this, MainActivity.class);		
		//PendingIntent openPendingIntent = PendingIntent.getActivity(this, 0, openAppIntent, PendingIntent.FLAG_CANCEL_CURRENT)
		
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		mWakeLock.acquire();
		try {
			PersonalDataStore pds = new PersonalDataStore(this);
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Map<Integer, Notification> notifications = pds.getNotifications();
			
			if (notifications != null) {
				for (Integer notificationType : notifications.keySet()) {
					Notification n = constructNotification(notifications.get(notificationType));
					notificationManager.notify(notificationType, n);
				}
			}
		} catch (Exception ex) {
			Log.e("NotificationService", ex.getMessage());
		}
		finally {
			if (mWakeLock.isHeld()) {
				mWakeLock.release();	
			}
		}
	}

	private Notification constructNotification(Notification notification) {
		NotificationDecoder nDecoder = new NotificationDecoder();
		
		nDecoder.decode(notification);
		
		Intent newIntent = new Intent(this, DialogActivity.class);
		newIntent.putExtra("title", nDecoder.title);
		newIntent.putExtra("description", nDecoder.description);
		newIntent.putExtra("posButton", nDecoder.posButton);
		newIntent.putExtra("negButton", nDecoder.negButton);
		newIntent.putExtra("numRepeats", nDecoder.numRepeats);
		newIntent.putExtra("repeatTime", nDecoder.repeatTime);
		newIntent.putExtra("items", nDecoder.items);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, newIntent, 0);
		
		Notification n  = new Notification.Builder(this)
        .setContentTitle(nDecoder.nTitle)
        .setContentText(nDecoder.nContent)
        .setSmallIcon(R.drawable.ic_launcher).setVibrate(new long[] { 0, 100, 50, 100, 50, 100 })
//        .setSmallIcon(R.drawable.icon)
        .setContentIntent(pIntent).build();
        
//        .setExtras(newIntent.getExtras())
//        .setAutoCancel(true)
//        .addAction(R.drawable.ic_launcher, "Call", pIntent)
//        .addAction(R.drawable.ic_launcher, "More", pIntent)
//        .addAction(R.drawable.ic_launcher, "And more", pIntent).build();

		return n;
	}
	
}
