package com.example.gps4;
import android.os.AsyncTask;
 
class ASchangeDistancce extends AsyncTask<Void, Void, Void>    {

	connection con;
	MainActivity MW;
	String dis;
	
	ASchangeDistancce(MainActivity MW,String dis )    {      
       con=new connection();
       this.MW=MW;
       this.dis=dis;
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
        con.CsetDistance(MW.getString("myGroupName"),dis);
	    MainActivity.pd.cancel(); 	
	    MW.syncON();
            return null;
    }   

} 