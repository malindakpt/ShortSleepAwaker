package com.example.gps4;

import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

 
class ASupdater extends AsyncTask<Void, Void, Void>    {
	 
	 
	Location loc;
	connection con=new connection();
	String myPhone;
	background BK;
 

	ASupdater(background BK,Location loc,String phone)    {
       this.BK=BK;
	   this.loc=loc;
       myPhone=phone;
    }
//	
//	ASupdater(Location loc,String phone)    {
//	       
//		   this.loc=loc;
//	        myPhone=phone;
//	    }

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
    			Double x,y;
    			  if(loc==null){
    		    	  x=0.0;
    		    	  y=0.0;
    		      }else{
    		    	  x= loc.getLatitude();
    		    	  y= loc.getLongitude();
    		      }
    			  
    			Boolean ok=con.CupdateMember(myPhone,x,y);
     			
    			check();
    			BK.stopSelf();
    		
    	 	}	catch(Exception e){}
    	
    return null;
    }   
    
    private void check(){
		try{
			
			String names="";
			
			Float d0=Float.parseFloat(BK.getString("groupDist"));
			String type=BK.getString("groupType");
			
			String details=con.CgetMemberCOrdinates( BK.getString("myGroupName"));
			String[] members=details.split(";");
		
			Boolean updated=false;
			
			for (String s : members) {
		 
				String phone=s.split(":")[0];
				String sx=s.split(":")[1].split(",")[0];
				String sy=s.split(":")[1].split(",")[1];
				
				Float x=Float.parseFloat(sx);
				Float y=Float.parseFloat(sy);
				
				Float d2=calculator.getDis((float)loc.getLatitude(), (float)loc.getLongitude(), x, y);
				
				Float d1=BK.getFloat(phone);
				
				
				if(type.equals("0"))
				{
					if(d1!=0){
						if(d2<d0){
							if(d1>d0){
								updated=true;	
								names=addNames(names, BK.getContactName(phone));
							}else{
								//already passed the limit
							}
						}else{
							//not passed the limit
						}
						BK.putFloat(phone, d2);
					}
					else{
						BK.putFloat(phone	,7000.0F);	
					}
				}
				
				if(type.equals("1"))
				{
					if(d1!=null){
						if(d2>d0){
							if(d1<d0){
								updated=true;	
								names=addNames(names, BK.getContactName(phone));
						
							}else{
								//already passed the limit
							}
						}else{
							//not passed the limit
						}
						BK.putFloat(phone, d2);
					}
					else{
						BK.putFloat(phone	,0F);
					}
				}	
			}
			if(updated)			{
				 
				 BK.addNotification(names);
				// Toast.makeText(context, text, duration)
			}
			
		}catch(Exception e){}
	}
	private String addNames(String s1,String s2){
		if(s1.equals(""))	return s2;
		else				return s1+","+s2;
	}
    
 } 