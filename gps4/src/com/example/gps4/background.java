package com.example.gps4;


import java.lang.reflect.Field;


import java.lang.reflect.Method;
import java.util.Calendar;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;


public class background extends Service implements LocationListener {

	 Location loc;
	 connection con=new connection();
	 AlarmManager alarmManager ;
	 PendingIntent pendingIntent;
 
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {

		if( getBoolean("sync")){
				alarm(20);
					try{
						 loc=getMyLocation();	
						// String phone=getString("myPhoneNo");
						// new ASupdater(this, loc,phone).execute();

					}	catch(Exception e){}
		}
		else  {
		
		}
	}
	
    public void alarm(int d) {

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent=PendingIntent.getService(this, 0, new Intent(this, background.class),0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND,d);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

	public static String addNames(String s1,String s2){
		if(s1!=null && s2!=null){
			if(s1.equals(""))	return s2;
			else				return s1+","+s2;
		}
		else
			return "";
	}
	
	public void addNotification(String text){
		Context context =getApplicationContext();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
			
			////////////////////////////////////////////////
			Notification myNotification = new Notification();
			myNotification.icon = R.drawable.map;
			myNotification.tickerText = "A member passed the limit";
			myNotification.when = System.currentTimeMillis();
			////////////////////////////////////////////////////
			Intent notificationIntent = new Intent(context, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,  notificationIntent, 0);
			//////////////////////////////////////////////////////
		
			  
			myNotification.setLatestEventInfo(context, "Group Tracker",text+" passed the limit", contentIntent);
			/////////////////////////////////////////////////////////
			myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			
			notificationManager.notify(2, myNotification);
			 
			playNotificationSound();
			vibrate();
			
	}
	private Boolean chkConnection(){
		try{
			ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork.isConnectedOrConnecting();
			return true;
		}catch(Exception e){
			return false;
		}
		 	
	}
	
	public void syncON(){
		//putBoolean("sync",true);
	}
	public void syncOFF(){
		//putBoolean("sync",false);
	}
	
	
	private void enableData(){
		
		if( getBoolean("sync")){
			try{
				final ConnectivityManager conman = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
				   final Class conmanClass = Class.forName(conman.getClass().getName());
				   Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
				   iConnectivityManagerField.setAccessible(true);
				   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
				   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
				   final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
				   setMobileDataEnabledMethod.setAccessible(true);
	
				   setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
				}catch(Exception e){}
		}
	}
	


	
	public  Location getMyLocation()
	{
		
		LocationManager locationManager= (LocationManager)getSystemService(this.LOCATION_SERVICE);
		Location location=null;
		////////////////////////////////////////////////////////////////
		locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 2000,2, this);
		///////////////////////////////////////////////////////////////
//		String mode=getString("mode");
//		
//		if(mode.equals("network")){
//			location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//		}else{
//			try{
//				location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//			}catch(Exception e){}
//			
//			if(location==null)	location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//		}
//		
//		location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		return location;
	}
	
	private void vibrate()
	{
		 
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	 
		int dot = 200;      // Length of a Morse Code "dot" in milliseconds
		int dash = 500;     // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200;    // Length of Gap Between dots/dashes
		int medium_gap = 500;   // Length of Gap Between Letters
		int long_gap = 1000;    // Length of Gap Between Words
		long[] pattern = {
		    0,  // Start immediately
		    dot, short_gap, dot, short_gap, dot,    // s
		    medium_gap,long_gap
//		    dash, short_gap, dash, short_gap, dash, // o
//		    medium_gap,
//		    dot, short_gap, dot, short_gap, dot,    // s
//		    long_gap
		};
		v.vibrate(pattern, -1);

	}
	
	private void playNotificationSound(){
		try {
	        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
	        r.play();
	    } catch (Exception e) {}
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
		
		return settings.getFloat(key, 500F);
	}
	
	public String getString(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getString(key,"00");
	}
	public void putString(String key,String val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putString(key,val);
 		 
 		editor.commit();
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

		

	public String getContactName(String phoneNumber) 
    {  
        Uri uri;
        String[] projection;

        if (Build.VERSION.SDK_INT >= 5)
        {
            uri = Uri.parse("content://com.android.contacts/phone_lookup");
            projection = new String[] { "display_name" };
        }
        else
        { 
            uri = Uri.parse("content://contacts/phones/filter");
            projection = new String[] { "name" }; 
        } 

        uri = Uri.withAppendedPath(uri, Uri.encode(phoneNumber)); 
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null); 

        String contactName = "";

        if (cursor.moveToFirst()) 
        { 
            contactName = cursor.getString(0);
        } 

        cursor.close();
        cursor = null;

        if(!contactName.equals(""))		return contactName;
        else if(phoneNumber.equals(getString("myPhoneNo")))	return "Me";
        else return phoneNumber;
       
    }


	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		//
		 String phone=getString("myPhoneNo");
		 new ASupdater(this, loc,phone).execute();
		//Toast.makeText(getApplicationContext(),loc.getLatitude()+ ","+loc.getLongitude(), Toast.LENGTH_LONG).show();
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


	
}
