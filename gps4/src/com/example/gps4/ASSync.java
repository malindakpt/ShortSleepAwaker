package com.example.gps4;
 
import android.os.AsyncTask;
 
 
class ASSync extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	Boolean b;
	MainActivity MW;

	 ASSync (MainActivity MW,Boolean b)    {
       this.b=b;
       this.MW=MW;
       con=new connection(); 
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
	    	if(b)	 MW.startServices();
	    	else	 MW.stopServices();
    	MW.syncON();
    	
    	return null;
    }   
 
	
} 