package edu.mit.media.hellopds;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DialogHelper extends Service {

	String title, questionType, description, posButton, negButton, s1, s2, s3;
	int numItems, numRepeats, repeatTime;
	String[] items;

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

	@Override
	public IBinder onBind(Intent intent) {
		s1 = intent.getStringExtra("string1");
		s2 = intent.getStringExtra("string2");
		s3 = intent.getStringExtra("string3");
		
		/*
		 * s1 = title, question type, optional description
		 * s2 = data for type + server response (changes based on type)
		 * s3 = buttons, # of repeats, time between repeats
		 * 
		 * Types of questions + addition instructions
		 * List - a list of options, think multiple choice (choose one of a,b,c,d... etc)
		 * 		*** Be sure to include no more than 9 options
		 * 		*** Be sure you numItems equals the number of items you define
		 * 
		 * Slider - Slides a bar across the screen, think like volume bar. Values range from lowNum to highNum
		 * 		*** Be sure that lowNum is less than highNum
		 * 
		 * InputText - A blank text field where users can input text
		 *      *** Consider including the optional hint text
		 *      
		 * InputInt - Just like InputText, but input is restricted to numbers (and decimals)
		 * 
		 * 
		 * Strings
		 * s1 = <startTitle>Example Question<endTitle><startType>Picker<endType><startDescription><endDescription>
		 * s3 = <startNegButton>Delay<endNegButton><startPosButton>Submit<endPosButton><startNumRepeats>3<endNumRepeats><startTimeRepeat>5000<endTimeRepeat>
		 *
		 *String 2
		 *	List
		 *		s2 = <startNumItems>3<endNumItems><startI1>Low<endI1><startI2>Medium<endI2><startI3>High<endI3>
		 *	Slider // Make sure itemName is SINGULAR and can be plural by adding "s"
		 *		s2 = <startStartNum>0<endStartNum><startEndNum>10<endEndNum><startItemName>Cigar<endItemName>
		 *	Input (both types)
		 *		s2 = <startHint>Hint<endHint>
		 */
		s1 = "<startTitle>Example Question<endTitle><startType>Picker<endType><startDescription><endDescription>";
		s2 = "<startNumItems>3<endNumItems><startI1>Low<endI1><startI2>Medium<endI2><startI3>High<endI3>";
		s3 = "<startNegButton>Delay<endNegButton><startPosButton>Submit<endPosButton><startNumRepeats>3<endNumRepeats><startTimeRepeat>5000<endTimeRepeat>";
		
		decodeS1(s1);
		decodeListS2(s2);
		decodeS3(s3);
		
		Intent newIntent = new Intent(this, DialogActivity.class);
		newIntent.putExtra("title", title);
		newIntent.putExtra("description", description);
		newIntent.putExtra("posButton", posButton);
		newIntent.putExtra("negButton", negButton);
		newIntent.putExtra("numRepeats", numRepeats);
		newIntent.putExtra("repeatTime", repeatTime);
		newIntent.putExtra("items", items);
		startActivity(newIntent);
		
		stopSelf();
		
		return null;
	}
}
