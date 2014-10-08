package com.example.gps4;

import android.os.AsyncTask;

public class wakeup extends AsyncTask<Void, Void, Void>{

	@Override
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	   
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute(); 
	    }

	    @Override
	    protected Void doInBackground(Void... arg0) {
	  
	    	try{
	    		Thread.sleep(10000);
	    		MainActivity.pd.cancel();
	    	}catch(Exception e){}
	    	return null;
	    }   


}
