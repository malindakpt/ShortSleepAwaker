package com.example.shortsleepawaker;



import java.util.Calendar;

 

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class Background extends Service implements SensorEventListener {

	 AlarmManager alarmManager ;
	 PendingIntent pendingIntent;
	 public static Sleeper thread;
	 public static boolean runnung=false;
	 private SensorManager sensorManager;
	 public static Vibrator vibrator;
	 final String STATUS="status";
	 final String SENSITIVITY="SENSITIVITY";
		final String TIME_PERIOD="TIME_PERIOD";
		
	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}
	

	@Override
	public void onCreate() { 
		MainActivity.bk=this;
		
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); 
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		  sensorManager.registerListener(this,
			        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
			        SensorManager.SENSOR_DELAY_NORMAL);
	}
	  
	 private void getAccelerometer(SensorEvent event) {
		    float[] values = event.values;
		    // Movement
		    float x1 = getFloat("x");
		    float y1 = getFloat("y");
		    float z1 = getFloat("z");
		    
		    float x = values[0];
		    float y = values[1];
		    float z = values[2];
		    Double th=3.0-0.5*getInt(SENSITIVITY);
		    
		    if(Math.abs(x1-x) >th || Math.abs(y1-y) > th || Math.abs(z1-z) > th){ 
		    	Calendar c = Calendar.getInstance(); 
				int seconds = c.get(Calendar.SECOND)+c.get(Calendar.MINUTE)*60 +c.get(Calendar.HOUR)*3600;
			
 				putFloat("last", (float) seconds);   
 		    	
 		    	putFloat("x", x);
 		    	putFloat("y", y);
 		    	putFloat("z", z);
 		    	//Toast.makeText(getApplicationContext(), "dasd", Toast.LENGTH_LONG).show();
 		    	MainActivity.txtLastMove.setText("Last Movement  "+MainActivity.getTime());
		    }
		  }
 
	@Override
	public void onStart(Intent intent, int startId) {
   
			long now = 0;
			Float last=0F;
			 
				try{
					UpdateNotification();
					Calendar c = Calendar.getInstance(); 
					int seconds = c.get(Calendar.SECOND)+c.get(Calendar.MINUTE)*60 +c.get(Calendar.HOUR)*3600;
	
					now=System.currentTimeMillis();
					last=getFloat("last");
				 
					if(Math.abs(seconds-last)>5 ){
						if(getBoolean(STATUS)){
							vibrator.vibrate(100); 
						}
				  	}  
				}	
				catch(Exception e){ 
					putFloat("last",(float) now);
				}
				alarm(getInt(TIME_PERIOD));
	}
	
	 public void alarm(int d) {

	        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	        pendingIntent=PendingIntent.getService(this, 0, new Intent(this, Background.class),0);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.add(Calendar.SECOND,d);
	        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	    }
	
	@SuppressWarnings("deprecation")
	 public void addNotification(){
			startForeground( 33, MainActivity.myNotification);

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
	 public void UpdateNotification(){
		 try{
		if(getBoolean(STATUS))	
			startForeground( 33, MainActivity.myNotification);
		else	
			stopForeground(true);
		 }catch(Exception e){		
			 Toast.makeText(getApplicationContext(), "error " + System.currentTimeMillis()/1000, Toast.LENGTH_SHORT).show();
}
 
  }

	public Boolean getBoolean(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getBoolean(key,false);
	}
	
	public void putBoolean(String key,Boolean val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putBoolean(key,val);
  		editor.commit();
	}
	
	public void putFloat(String key,Float val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putFloat(key, val);
 		editor.commit();
	}
	public Float getFloat(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getFloat(key, 5F);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		   if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			      getAccelerometer(event);
		   }
		
	}
}
