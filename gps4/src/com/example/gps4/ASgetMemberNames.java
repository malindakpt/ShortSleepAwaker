package com.example.gps4;
import android.os.AsyncTask;
 
class ASgetMemberNames extends AsyncTask<Void, Void, Void>    {

	connection con;
	MainActivity MW;

	ASgetMemberNames(MainActivity MW )    {      
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
	    	MainActivity.myMembers=con.CgetMemberNumbers(MW.getString("myGroupName"));
	    	MainActivity.pd.cancel();	
	    MW.syncON();
	    
            return null;
    }   
} 