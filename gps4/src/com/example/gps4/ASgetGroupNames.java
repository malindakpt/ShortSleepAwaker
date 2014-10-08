package com.example.gps4;
import android.os.AsyncTask;
 
class ASgetGroupNames extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	MainActivity MW;

	ASgetGroupNames(MainActivity MW)    {
         
       con=new connection();
       this.MW=MW;
    }

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
    	new wakeup().execute();
    	MW.syncOFF();
	    MW.groupNames=con.CgetGroupNames();
	    MainActivity.pd.cancel();
    	MW.syncON();
        return null;
    }   
} 