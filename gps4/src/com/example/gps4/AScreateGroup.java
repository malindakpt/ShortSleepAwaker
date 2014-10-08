package com.example.gps4;
import android.os.AsyncTask;
 
class AScreateGroups extends AsyncTask<Void, Void, Void>    {

	connection con;
	createGroup CG;

	AScreateGroups(createGroup CG)    {
       con=new connection();
       this.CG=CG;
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
    	CG.syncOFF();
    	CG.status =con.CcreateGroup(CG.tGroupName.getText().toString(),CG.password, CG.idx,CG.tDist.getText().toString(),CG.myPhone,CG.x,CG.y);	
    	CG.pd.cancel();
    	CG.syncON();
        return null;
    }   
} 