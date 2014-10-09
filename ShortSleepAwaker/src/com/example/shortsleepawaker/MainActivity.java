package com.example.shortsleepawaker;


import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  {
	

	
	private SeekBar skTime,skSens;
	private static TextView txtTime,txtSens,txtLastMove;
	private ImageButton imgButtonStatus;	 
	public static Notification myNotification; 
	public static Background bk;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		initUI();
		addActioins();
	 
		Intent inService=new Intent(this, Background.class);
	    startService(inService); 
	    initNotification(); 

		imgButtonStatus.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) { 
			 	if(event.getAction()!=1){ //press the button
			 		imgButtonStatus.setImageResource(R.drawable.s1); 
			 	}else{  //release the button
			 		if(getBoolean(Constants.STATUS)){ 
			 			imgButtonStatus.setImageResource(R.drawable.s0); 
			 			putBoolean(Constants.STATUS, false); 
					 
			 			initNotification();
			 		}else{
			 			imgButtonStatus.setImageResource(R.drawable.s2);
			 			putBoolean(Constants.STATUS, true); 
			 		 	initNotification();
			 		} 
			 	} 
				return false;
			}
		});
    	 
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_about:
	        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	        	alertDialog.setTitle("How to use ");
	        	alertDialog.setMessage("NO MOVEMENTS >> SLEEPING\nApplication is listening for movements of the device and, if no movents are detected then vibration alert is given to the user");
	        	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        	// here you can add functions
	        	}
	        	});
	        	alertDialog.setIcon(R.drawable.s4);
	        	alertDialog.show();
	            return true;
	         
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public static void setLastMoveTime(){
		txtLastMove. setText("Last Movement  "+getTime());
	}
	public void initNotification(){ 
			 myNotification = new Notification(); 
			 if(getBoolean(Constants.STATUS)){
				 myNotification.icon = R.drawable.s2;
			 }else{
				 myNotification.icon = R.drawable.s0; 
			 } 
			 myNotification.when = System.currentTimeMillis(); 
			 Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
			 PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,  notificationIntent, 0);
			 myNotification.setLatestEventInfo(getApplicationContext(),"Snooze Wakeup","Wakeup Me . . .", contentIntent); 
			 myNotification.flags |= Notification.FLAG_AUTO_CANCEL; 
			 myNotification.flags |=(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 myNotification.tickerText = "Snooze Wakeup"; 
	 	 
			 if(bk!=null)
				 bk.UpdateNotification();
	  }
	
	private void initUI(){
		skTime=(SeekBar) findViewById(R.id.skTime);
		skSens=(SeekBar) findViewById(R.id.skSens);
		txtTime=(TextView) findViewById(R.id.txtTimes);
		txtSens=(TextView) findViewById(R.id.txtSens);
		imgButtonStatus=(ImageButton) findViewById(R.id.but1); 
		txtLastMove=(TextView) findViewById(R.id.txtLastMove);
		
		skTime.setMax(55);		
		skSens.setMax(4);
		
		txtSens.setText("Sensitivity : "+getInt(Constants.SENSITIVITY));
		txtTime.setText("Check Me Every "+getInt(Constants.TIME_PERIOD)+" Seconds");
		
		skSens.setProgress(getInt(Constants.SENSITIVITY)-1);
		skTime.setProgress(getInt(Constants.TIME_PERIOD)-5);
		
		txtLastMove.setText("Last Movement  "+getTime());
		
		if(getBoolean(Constants.STATUS)){
			imgButtonStatus.setImageResource(R.drawable.s2);  
 		}else{
 			imgButtonStatus.setImageResource(R.drawable.s0); 
 		} 
	}
	
	private void addActioins(){
		skSens.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int x=skSens.getProgress()+1;
				txtSens.setText("Sensivity : "+x);
				putInt(Constants.SENSITIVITY, skSens.getProgress()+1);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		skTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int x=skTime.getProgress()+5;
				txtTime.setText("Check Me Every "+x+" Seconds");
				putInt(Constants.TIME_PERIOD, x  );
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { 
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { 
			}
		});
	
	}
	 
	 
	
	public int getInt(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getInt(key, 5);
	}
	 public void putInt(String key,int val){
	 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
	 		SharedPreferences.Editor editor = settings.edit();
	 		editor.putInt(key,val);
	  		editor.commit();
	 }

	 public void putBoolean(String key,Boolean val){
	 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
	 		SharedPreferences.Editor editor = settings.edit();
	 		editor.putBoolean(key,val);
	  		editor.commit();
	 }
	
	 //return the time with native firmat
	 public static String getTime(){
		 	Calendar c = Calendar.getInstance(); 
		 	int s=c.get(Calendar.SECOND);
		 	int m=c.get(Calendar.MINUTE);
		 
			String min="";
			String sec="";
			
			if(m<10)
				min="0"+m;
			else
				min=""+m;
			
			if(s<10)
				sec="0"+s;
			else
				sec=""+s;
				
			String time=c.get(Calendar.HOUR)+":"+min+" : "+sec+" sec";
			return time;
	 }
  
	public Boolean getBoolean(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0); 
		return settings.getBoolean(key,true);
	}
 
}
