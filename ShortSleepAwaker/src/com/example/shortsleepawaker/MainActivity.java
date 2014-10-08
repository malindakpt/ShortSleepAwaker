package com.example.shortsleepawaker;


import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
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
	
	final String STATUS="status";
	final String FIRST_RUN="FIRST_RUN";
	final String SENSITIVITY="SENSITIVITY";
	final String TIME_PERIOD="TIME_PERIOD";
	
	private SeekBar skTime,skSens;
	private TextView txtTime,txtSens;
	
	
	public static NotificationManager notificationManager;
	public static Notification myNotification;
	
	private SensorManager sensorManager;
	  private boolean color = false;
	  private View view;
	  private long lastUpdate;
	  public static TextView textView1,textView2,txtLastMove;
	 
	  public static int statred=0;
	  public Sleeper separate_thread;	  
	  public static Boolean sleeping=true;  
	  public static int count=0;
	  Boolean active=false;
	  
	  public static Background bk;
	  
	  ImageButton button;
	  
	  public void initNotification(){
			 myNotification = new Notification(); 
			 if(getBoolean(STATUS)){
				 myNotification.icon = R.drawable.s2;
			 }else{
				 myNotification.icon = R.drawable.s0; 
			 } 
			 myNotification.when = System.currentTimeMillis(); 
			 Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
			 PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,  notificationIntent, 0);
			 myNotification.setLatestEventInfo(getApplicationContext(),"Wake Up Me. . .","WUM", contentIntent); 
			 myNotification.flags |= Notification.FLAG_AUTO_CANCEL; 
			 myNotification.flags |=(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 myNotification.tickerText = "Wake Up Me. . ."; 
	 	 
			 if(bk!=null)
				 bk.addNotification();
	  }
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		skTime=(SeekBar) findViewById(R.id.skTime);
		skSens=(SeekBar) findViewById(R.id.skSens);
		skTime.setMax(55);
		
		skSens.setMax(4);
		
		txtTime=(TextView) findViewById(R.id.txtTimes);
		txtSens=(TextView) findViewById(R.id.txtSens);
		
		
		
		skSens.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int x=skSens.getProgress()+1;
				txtSens.setText("Sensivity : "+x);
				putInt(SENSITIVITY, skSens.getProgress()+1);
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
				putInt(TIME_PERIOD, x  );
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { 
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { 
			}
		});
		
		
	//	if(!getBoolean(FIRST_RUN)){ 
			Intent inService=new Intent(this, Background.class);
	        startService(inService);
	        putBoolean(STATUS	, true);
	        putBoolean(FIRST_RUN, true); 
	        initNotification(); 
	//	}
 
		txtSens.setText("Sensitivity : "+getInt(SENSITIVITY));
		txtTime.setText("Check Me Every "+getInt(TIME_PERIOD)+" Seconds");
		
		button=(ImageButton) findViewById(R.id.but1); 
		button.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) { 
			 	if(event.getAction()!=1){ 
			 		button.setImageResource(R.drawable.s1); 
			 	}else{  
			 		if(getBoolean(STATUS)){//release
			 			button.setImageResource(R.drawable.s0); 
			 			putBoolean(STATUS, false); 
					 
			 			initNotification();
			 		}else{
			 			button.setImageResource(R.drawable.s2);
			 			putBoolean(STATUS, true); 
			 		 	initNotification();
			 		} 
			 	} 
				return false;
			}
		});
    	  
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lastUpdate = System.currentTimeMillis();
		
		 
		if(getBoolean(STATUS)){
 			button.setImageResource(R.drawable.s2);  
 		}else{
 			button.setImageResource(R.drawable.s0); 
 		} 
		
		skSens.setProgress(getInt(SENSITIVITY));
		skTime.setProgress(getInt(TIME_PERIOD)-5);
		
		txtLastMove=(TextView) findViewById(R.id.txtLastMove);
 		txtLastMove.setText("Last Movement  "+getTime());
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
	 
	public void putFloat(String key,Float val){
	 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
	 		SharedPreferences.Editor editor = settings.edit();
	 		editor.putFloat(key, val); 
	 		editor.commit();
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
 
	public Boolean getBoolean(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0); 
		return settings.getBoolean(key,false);
	}
	
}
