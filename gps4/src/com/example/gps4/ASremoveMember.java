package com.example.gps4;
import android.os.AsyncTask;
 
class ASremoveMember extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	MainActivity MW;
	String number;
	 

	ASremoveMember(MainActivity MW,String number)    {
       this.number=number;
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
	    con.CremoveMember(number, MW.getString("myGroupName"));
	    MainActivity.pd.cancel();
    	MW.syncON();
    	
    	
        return null;
    }   
} 