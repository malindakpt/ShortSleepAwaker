package com.example.gps4;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;


public class connection extends Activity{
	  InputStream is = null;
	  StringBuilder sb=null;

	public String CgetGroupNames(){
		 try{
			
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getGroupNames.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		
			 /////////////////////////////////////
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");
	            String line="0";
	            
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            
	            return sb.toString().split("%%")[0];
	            
	        }catch(Exception e){
	        	
	        	
	        	return null;
	        }

	}
	
	public  Boolean CcreateGroup(String groupName,String password,int type,String dist,String phone,Double x,Double y){
		 try{
			 
			 if(groupName.equals("Group Name")) return false;
			 
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/createGroup.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("group_Name", groupName));
			    nameValuePairs.add(new BasicNameValuePair("password",password));
			    nameValuePairs.add(new BasicNameValuePair("type",""+type));//convert to String
			    nameValuePairs.add(new BasicNameValuePair("dist",dist));
			    
			    nameValuePairs.add(new BasicNameValuePair("phone", phone));
			    nameValuePairs.add(new BasicNameValuePair("x",x.toString()));//convert to String
			    nameValuePairs.add(new BasicNameValuePair("y",y.toString()));
			   
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
			
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpResponse response = httpclient.execute(httppost);
	         /////////////////////////////////////
	         HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            sb = new StringBuilder();
	            sb.append(reader.readLine() + "\n");
	            String line="0";
	            
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	                     
			 /////////////////////////////////////
	        
	            if( sb.toString().split("%%")[0].equals("true"))  return true;
	            else return false;

	        }catch(Exception e){
	          return false;    
	        }
		 
	}
	public  Boolean CupdateMember(String phone,Double x,Double y){
		 try{
			 	HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/updateMember.php");
			 	/////////////////////////////////////////
			 
			 	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("phone", phone));
			    nameValuePairs.add(new BasicNameValuePair("x",x.toString()));//convert to String
			    nameValuePairs.add(new BasicNameValuePair("y",y.toString()));
			   
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
				
				HttpClient httpclient = new DefaultHttpClient();
		        HttpResponse response = httpclient.execute(httppost);
				 /////////////////////////////////////
		        return true;
			 
	        }catch(Exception e){
	            return false;
	        }
	}
	public  String CgetMyGroupName(String phone)
	{
		 InputStream is = null;
		 StringBuilder sb=null;
		try{ 
		 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getMyGroupName.php");
		
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("phone",phone));
		 
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
		 /////////////////////////////////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            return sb.toString().split("%%")[0];
          
            
        }catch(Exception e){
        	return null;
        }
	}
	public String CgetMemberNumbers(String group_Name)
	{
		 InputStream is = null;
		 StringBuilder sb=null;
		try{ 
		 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getMemberNumbers.php");
		
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
		 
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
		 /////////////////////////////////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            return sb.toString().split("%%")[0];
          
            
        }catch(Exception e){
        	return null;
        }
	}
	
	public String CgetMemberCOrdinates(String group_Name)
	{
		 InputStream is = null;
		 StringBuilder sb=null;
		try{ 
		 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getGroupMembersCordinates.php");
		
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
		 
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
		 /////////////////////////////////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            return sb.toString().split("%%")[0];
          
            
        }catch(Exception e){
        	return null;
        }
	}
	public  String CgetPassword(String group_Name)
	{
		 InputStream is = null;
		 StringBuilder sb=null;
		 
		try{ 
			HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getPassword.php");
		
		 	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
		 
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
		 /////////////////////////////////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            return sb.toString().split("%%")[0];
          
            
        }catch(Exception e){
        	return null;
        }
	}
	
	
	
	public String CgetGroupMembers(String group_Name)
	{
		 InputStream is = null;
		 StringBuilder sb=null;
		try{ 
		 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/getGroupMembers.php");
		
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
		 
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
		 /////////////////////////////////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            
            return sb.toString().split("%%")[0];
          
            
        }catch(Exception e){
        	return null;
        }
	}
	
	public Boolean CaddMember(String phone,String group_Name,Double x,Double y){
		 try{
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/addMember.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("phone", phone));
			    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
			    nameValuePairs.add(new BasicNameValuePair("x",x.toString()));//convert to String
			    nameValuePairs.add(new BasicNameValuePair("y",y.toString()));
			   
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
			
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpResponse response = httpclient.execute(httppost);
			 /////////////////////////////////////
	        return true;
			 
	        }catch(Exception e){
	             return false;     
	        }
	}
	
	public Boolean CsetDistance(String group_Name,String dist){
		 try{
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/setDistance.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			   
			    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
			    nameValuePairs.add(new BasicNameValuePair("dist",dist));//convert to String
			 
			   
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
			
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpResponse response = httpclient.execute(httppost);
			 /////////////////////////////////////
	        return true;
			 
	        }catch(Exception e){
	             return false;     
	        }
	}
	public void CremoveMember(String phone,String group_Name){
		 try{
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/removeMember.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("phone", phone));
			    nameValuePairs.add(new BasicNameValuePair("group_Name",group_Name));
		  
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
			
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpResponse response = httpclient.execute(httppost);
			 /////////////////////////////////////
	        }catch(Exception e){
	               
	        }
	}
	
	public void CdeleteGroup( String group_Name){
		 try{
			 HttpPost httppost = new HttpPost("http://www.malindakpt.webege.com/deleteGroup.php");
			 	/////////////////////////////////////////
			 
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("group_Name", group_Name));
			  
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//added extra
			
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpResponse response = httpclient.execute(httppost);
			 /////////////////////////////////////
	        }catch(Exception e){
	               
	        }
	}
	
	private void CsetMobileDataEnabled(Context context, boolean enabled) {
		try{
		final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   final Class conmanClass = Class.forName(conman.getClass().getName());
		   Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		   iConnectivityManagerField.setAccessible(true);
		   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
		   final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		   setMobileDataEnabledMethod.setAccessible(true);

		   setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}catch(Exception e){}
	}

}
