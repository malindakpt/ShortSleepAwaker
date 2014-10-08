package com.example.gps4;

import com.google.android.gms.maps.GoogleMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

 
class ASrefresh extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	MainActivity MW;
	String myGroup;

    ASrefresh(MainActivity MW)    {
       this.myGroup=myGroup;  
       con=new connection();
       this.MW=MW;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //tv.setText(myData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // tv.setText("Started Running....");
    }

    @Override
    protected Void doInBackground(Void... arg0) {
    	// Boolean b=con.CupdateMember(MainActivity.myPhoneNo, MainActivity.locMgr.getX(), MainActivity.locMgr.getY());
    	new wakeup().execute();
    	MW.groupMembers=con.CgetGroupMembers( MW.getString("myGroupName")).split(";");
    	MainActivity.pd.cancel();
        return null;
    }   
} 