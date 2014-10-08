package com.example.gps4;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class location extends FragmentActivity {

	private static  Double x,y;
	private static  GoogleMap mMap;
	connection con;
	//static Float radius=0F;
 
 static LocationManager locationManager ;
	MainActivity MW;
  
 

	
	public location(MainActivity MW,GoogleMap mMap2,LocationManager locationManager2)
	{
		con=new connection();
		this.mMap=mMap2;
		this.locationManager=locationManager2;
		this.MW=MW;
	}
 
	public Double getX(){return x;}
	public Double getY(){return y;}
	
	public Boolean setUpMapIfNeeded() {
	
			mMap.setMyLocationEnabled(true);
	    	Location loc = getMyLocation();
	
	    	if(loc!=null)
	    	{
	    		try{  mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(),loc.getLongitude())));	        	
	    		}
	    		catch(Exception e){}
	    		return true;
	    	}
	    	else{
	    		return false;
	    	}

	}

	
	public Boolean setGroupMembers(String myGroupName,String[] ss)
	{
		mMap.clear(); 
		
		if(ss!=null){
	
				if(! MW.getString("groupType").equals("2")){
					float rad=Float.parseFloat( MW.getString("groupDist"));
					drawCircle(rad);
				}
				
				for (String string : ss) {
					String[] temp=string.split(":");
					
					Double lat=Double.parseDouble(temp[1]);
					Double lng=Double.parseDouble(temp[2]);
					
					if(! MW.getString("myPhoneNo").equals(temp[0])) {
						addMarker(lat, lng, MW.getContactName(temp[0]));
					}else{
						addMyMarker(lat, lng, MW.getContactName(temp[0]));
					}						
				}
				
				return true;
		}
		else{
			return false;
		}
	
	}
	
	public  Location getMyLocation()
	{
		//LocationManager locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location location=null;
		
		String mode=MW.getString("mode");
		
		if(mode.equals("network")){
			location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		}else{
			try{
								location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
			}catch(Exception e){}
			
			if(location==null)	location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		}
		
		if(location!=null){
			x=location.getLatitude();
    		y=location.getLongitude();
		}
		return location;
	}
	
	
	public void addMarker(Location loc,String title)
	{
		MarkerOptions opt=new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())).title(title);
		opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
	 
		mMap.addMarker(opt);
	
	}

	public static void addMyMarker(Double lat,Double lng,String title)
	{
		MarkerOptions opt=new MarkerOptions().position(new LatLng(lat,lng)).title(title);
		opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	 
		
		mMap.addMarker(opt);
	
	}
	public static void addMarker(Double lat,Double lng,String title)
	{
		MarkerOptions opt=new MarkerOptions().position(new LatLng(lat,lng)).title(title);
		opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
	 
		
		mMap.addMarker(opt);
	
	}
	public static void addMarker2(Double lat,Double lng,String title)
	{
		MarkerOptions opt=new MarkerOptions().position(new LatLng(lat,lng)).title(title);
		opt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		 
		
		mMap.addMarker(opt);
	
	}
	public void drawCircle(Float rad)
	{
		Location loc=getMyLocation();
		
		CircleOptions opt=new CircleOptions();
		opt.center(new LatLng(loc.getLatitude(),loc.getLongitude()));
		opt.radius(1000*rad);
		opt.strokeWidth(3);
		opt.strokeColor(Color.RED);
		
		mMap.addCircle(opt);
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
	
}
