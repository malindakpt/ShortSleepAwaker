package com.example.gps4;
import android.os.AsyncTask;
 
class ASaddMembers extends AsyncTask<Void, Void, Void>    {
	 
	 
	connection con;
	MainActivity MW;
	String number;
	Double x,y;
	String group_Name;

	ASaddMembers(MainActivity MW,String group_Name,String number,Double x,Double y)    {
       this.number=number;
       con=new connection();
       this.MW=MW;
       this.x=x;
       this.y=y;
       this.group_Name=group_Name;
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
    	boolean b=con.CaddMember(number, group_Name, x,y);
    
    	if(b)	{
    		MW.putString("myGroupName",group_Name);
    		//MW.putString("groupDist", val)
    	}
    	MainActivity.pd.cancel();
    	MW.syncON();
    	
        return null;
    }   
} 