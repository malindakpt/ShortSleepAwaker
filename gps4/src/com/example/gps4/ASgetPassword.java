package com.example.gps4;
import android.os.AsyncTask;
 
class ASgetPassword extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	MainActivity MW;
	String group_Name;
	 

	ASgetPassword(MainActivity MW,String group)    {
       this.group_Name=group;
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
	    	MW.nextGroupString=con.CgetPassword(group_Name);
	    	MainActivity.pd.cancel();
    	MW.syncON();
        return null;
    }   
} 