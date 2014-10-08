package com.example.shortsleepawaker;

import java.util.Calendar;

import android.os.AsyncTask;
import android.os.Vibrator;

public class Sleeper extends AsyncTask<Void, Integer, Void> {

	Background bk;
	final String STATUS="status";
	
	public Sleeper(Background bk){
		this.bk=bk;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		long now = 0;
		Float last=0F;
		 
			if(bk.getBoolean(STATUS)){
			try{
				//addNotification();
				Calendar c = Calendar.getInstance(); 
				int seconds = c.get(Calendar.SECOND)+c.get(Calendar.MINUTE)*60 +c.get(Calendar.HOUR)*3600;

				now=System.currentTimeMillis();
				last=bk.getFloat("last");
			 
				if(Math.abs(seconds-last)>5 ){
					 bk.vibrator.vibrate(200);
			  	}  
			}	
			catch(Exception e){ 
				bk.putFloat("last",(float) now);
			}
			bk.alarm(5);
		} 
		return null;
	}
	
}
