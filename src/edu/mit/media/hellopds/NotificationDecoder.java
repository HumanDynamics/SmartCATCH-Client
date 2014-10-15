package edu.mit.media.hellopds;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;

public class NotificationDecoder {

	String nTitle, nContent, nAction, title, questionType, description, posButton, negButton, s1, s2, s3;
	int numItems, numRepeats, repeatTime;
	String[] items;
	
	private String[] decodeNotification(Notification n) {
		// TMP
		String[] res = new String[3];
		
		res[0] = "<startTitle>Example Question<endTitle><startType>Picker<endType><startDescription><endDescription>";
		res[1] = "<startNumItems>3<endNumItems><startI1>Low<endI1><startI2>Medium<endI2><startI3>High<endI3>";
		res[2] = "<startNegButton>Delay<endNegButton><startPosButton>Submit<endPosButton><startNumRepeats>3<endNumRepeats><startTimeRepeat>5000<endTimeRepeat>";
		
		nTitle = "New question?";
		nContent = "New question content.";
		
		
//		JSONObject jObject = new JSONObject(result);
//		res[0] = result.getString("s1");
//		res[1] = result.getString("s1");
//		res[2] = result.getString("s1");
		
		return res;
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void decode(Notification notification) {
//		String[] s = decodeNotification(notification);
//		decodeS1(s[0]);
//		decodeListS2(s[1]);
//		decodeS3(s[2]);
		
//		nTitle = notification.extras.getString("android.title");
//		nContent = notification.extras.getString("android.text");
		
		nTitle = notification.extras.getString("android.text");
		nAction = notification.extras.getString("android.title");
		// TODO: nAction ...
	}
	
	public void decodeS1(String s1) {
		int pos1, pos2;
		
		//Title
		pos1 = s1.indexOf("<startTitle>") + 12;
		pos2 = s1.indexOf("<endTitle>");
		title = s1.substring(pos1, pos2);
		
		//Type
		pos1 = s1.indexOf("<startType>") + 11;
		pos2 = s1.indexOf("<endType>");
		questionType = s1.substring(pos1, pos2);
		
		//Description
		pos1 = s1.indexOf("<startDescription>") + 18;
		pos2 = s1.indexOf("<endDescription>");
		if(pos1 == pos2) {
			description = null;
		}
		else {
			description = s1.substring(pos1, pos2);
		}
	}

	public void decodeListS2(String s2) {
		int pos1, pos2;
		
		//Number of items
		pos1 = s2.indexOf("<startNumItems>") + 15;
		pos2 = s2.indexOf("<endNumItems>");
		numItems = Integer.parseInt(s2.substring(pos1, pos2));
		items = new String[numItems];
		
		//Items
		for(int i = 0; i < 3; i++) {
			pos1 = s2.indexOf("<startI" + (i + 1) + ">") + 9;
			pos2 = s2.indexOf("<endI" + (i + 1) + ">");
			items[i] = s2.substring(pos1, pos2);
		}
	}

	public void decodeS3(String s3) {
		int pos1, pos2;
		
		//Negative Button
		pos1 = s3.indexOf("<startNegButton>") + 16;
		pos2 = s3.indexOf("<endNegButton>");
		negButton = s3.substring(pos1, pos2);
		
		//Positive Button
		pos1 = s3.indexOf("<startPosButton>") + 16;
		pos2 = s3.indexOf("<endPosButton>");
		posButton = s3.substring(pos1, pos2);
		
		//Number of repeats
		pos1 = s3.indexOf("<startNumRepeats>") + 17;
		pos2 = s3.indexOf("<endNumRepeats>");
		numRepeats = Integer.parseInt(s3.substring(pos1, pos2));
		
		//Time between repeats
		pos1 = s3.indexOf("<startTimeRepeat>") + 17;
		pos2 = s3.indexOf("<endTimeRepeat>");
		repeatTime = Integer.parseInt(s3.substring(pos1, pos2));
	}
	
	
}
