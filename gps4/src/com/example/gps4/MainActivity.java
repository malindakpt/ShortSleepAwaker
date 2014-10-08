package com.example.gps4;


import java.lang.reflect.Field;


import java.lang.reflect.Method;
import java.util.Arrays;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle; 
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.app.AlertDialog; 
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout; 
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
 
    static Double criticalDistance=0.0;
    static int type=0;
    
    static boolean succeed=true; 
    final String noGroupME="You are not assigned to a group !";
    final String changePhoneTitle="Add/Change Phone Number";
	
	private AutoCompleteTextView textView ;
	 
	public static location locMgr;
	private  GoogleMap mMap;
	static Boolean started=false;

	private EditText inputMember;
	static TextView tAuto;

	static ProgressDialog pd = null;
 	static String[] groupMembers=null;
	static String 	groupNames	=null;
	static Boolean 	status		=false;
	static String nextGroupString;
 
	static String myMembers=null;
	Intent IN;
	
	connection con;

	
	
	public void startServices() {
		try{
			IN=new Intent(this, background.class);
			ComponentName service = startService(IN);
		}catch(Exception ee){}	
    }
	
	public void stopServices() {
		try{
			 stopService(IN);
		}catch(Exception ee){}	
    }
	
	private void firstRun(){
		SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
		if (isFirstRun)
		{
		    SharedPreferences.Editor editor = wmbPreference.edit();
		    editor.putBoolean("FIRSTRUN", false);
		    editor.commit();
		    
		    /////////////////////////////////////////////////
		    putBoolean("sync",true);
		    putString("mode", "network");
		    
		    startServices();
		    changePhoneNumber(changePhoneTitle);
		    /////////////////////////////////////////////////
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	return;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		firstRun();
 
		tAuto=(TextView) findViewById(R.id.auto);
		tAuto.setTextColor(Color.RED);
		
 	    chkSync();
	    con=new connection();
 	
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		LocationManager locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locMgr=new location(this,mMap,locationManager);
	
		if(!locMgr.setUpMapIfNeeded())
		{
			Toast.makeText(getApplicationContext(), "Location not found !",Toast.LENGTH_SHORT).show();		
		}

		if(chkConnection()){
			startServices();
		}else{
			setMobileDataEnabled(this, true);
		} 

	}
	
	private Boolean chkConnection(){
		try{
			ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);			 
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork.isConnectedOrConnecting();
			return true;
		}catch(Exception e){
			return false;
		}
		 	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void refresh(){
		 
		try{	 
			locMgr.setGroupMembers( getString("myGroupName"),groupMembers);
		}catch(Exception e){
			//Toast.makeText(getApplicationContext(), "Catch ! ",Toast.LENGTH_SHORT).show();		
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		
		switch (item.getItemId()) { 
		case R.id.it2CreateGrp: 
			Intent intent = new Intent(this, createGroup.class);
			intent.putExtra("myPhone",getString("myPhoneNo"));
			intent.putExtra("x", locMgr.getX());
			intent.putExtra("y", locMgr.getY());
			startActivity(intent);
		return true; 
		
		case R.id.it1Pref:
			 showPref();
			 return true; 
		
		case R.id.it2JoinGrp: 
			join_a_group();
 
		return true; 
		case R.id.it1ChngPhone: 
			changePhoneNumber(changePhoneTitle);
		
		return true; 
		
		case R.id.it1Refresh: 
			getGroupMembers();
		return true; 
		
		case R.id.it1ShowDetails:
			String sync=null;
			String type= getString("groupType");
			String grp=getString("myGroupName");
			
			if(getBoolean("sync")) sync="Enabled";
			else					sync="Disabled";
			
			if(type.equals("0")) type="Converging";
			else if(type.equals("1")) type="Diverging";
			else type="None";
			
			if(grp.equals("00"))	grp="No group";
						
			Toast.makeText(getApplicationContext(),"\nGroup: "+grp+"\n"+"Phone No: "+getString("myPhoneNo")+"\nDistance:"+getString("groupDist")+"\nType:"+type+"\nSync :"+sync,Toast.LENGTH_LONG).show();		
			return true;
		
		case R.id.it2AddMembers:
			addMember();
			return true; 	
			
		case R.id.it2DeleteGroup:
			askToDelete();
			return true; 	
			
		case R.id.it2RemoveMembers:
			removeMember();
			return true; 	
			
		case R.id.it2ChngDist:
			setDistance();
			return true; 
	
		default: 
			return super.onOptionsItemSelected(item); 
	 
		} 
	}
	
	private void leaveGroup(){
		
		putString("myGroupName", "00");
		putString("groupDist", "00");
		putString("groupType", "00");
		locMgr.setGroupMembers( getString("myGroupName"),null);

	}
	
	private void deleteGroup(){
		pd =  ProgressDialog.show(this,null,"Deleting group\n wait few seconds.",true,false,new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				Toast.makeText(getApplicationContext(), getString("myGroupName") +" deleted successfully",Toast.LENGTH_SHORT).show();
				leaveGroup();
			}
		});
		new ASdeleteGroup(this).execute();
	}
	
	private void askToDelete(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Delete Group");
		alert.setMessage("Are you really want to delete current group ");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int whichButton) {
			deleteGroup();
		}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int whichButton) {
			     
		}
		});

		alert.show(); 
	}
	
	private void getGroupMembers() {
		//////////////////////////////updater/////////////////
		 if(!getString("myGroupName").equals("00")){
			 	pd =  ProgressDialog.show(this,null,"Updating Members...",true,false,new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
							refresh();
					}
				});
	 	 new ASrefresh(this).execute();
	 	 //////////////////////////////////////////////////////
	
		}else{
			Toast.makeText(getApplicationContext(), "Join a group !",Toast.LENGTH_LONG).show();	
			
		}

	}
	
	private void join_a_group()
	{
		//////////////////////////////updater/////////////////

		pd =  ProgressDialog.show(this,null,"Fetching data\nwait few seconds !",true,false,new OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
				showGroupChooser("Join a Group"); 
		}
		});
		new ASgetGroupNames(this).execute();
		} 
		////////////////////////////////////////////////////// 
	
	
	
	public void showGroupChooser(String title){

		
		if(groupNames!=null){
				final String[] groupNameArray=groupNames.split(";");
		
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		                 android.R.layout.simple_dropdown_item_1line, groupNameArray);
		 		 
				textView=new AutoCompleteTextView(this);
				textView.setAdapter(adapter);
				textView.setThreshold(1);
				textView.setDropDownHeight(-2);
				 
				 
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
				alert.setTitle(title);
				alert.setMessage("Type the Group Name");
				alert.setView(textView);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int whichButton) {
					String groupName =textView.getText().toString();
					
					if(Arrays.asList(groupNameArray).contains(groupName))
						getPassword(groupName);
					else{
						showGroupChooser("Invalied Group Name");
						//Toast.makeText(getApplicationContext(),"Invalied group name",Toast.LENGTH_SHORT).show();
					}
				}
				});
		
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int whichButton) {
					     
				}
				});
		
				alert.show(); 
			}
			else{
				Toast.makeText(getApplicationContext(),"No groups are available",Toast.LENGTH_SHORT).show();
				
			}
	}
	
	
	private void getPassword(final String groupName){
	 
			pd =  ProgressDialog.show(this,null,"Validating user\nPlease wait.",true,false,new OnCancelListener() {
	
			@Override
			public void onCancel(DialogInterface dialog) {
				if(nextGroupString.split(":")[0]!=null){	
					if(nextGroupString.split(":")[0].equals("")){
						locMgr.getMyLocation();
						 
						addMemberToDB(groupName,getString("myPhoneNo"), locMgr.getX(), locMgr.getY());
					}
					else{
						authenticate(groupName,nextGroupString.split(":")[0]);
					}	
				}else{
					Toast.makeText(getApplicationContext(),"Invalied group name",Toast.LENGTH_LONG).show();
				}
			}
			});
			new ASgetPassword(this,groupName).execute();
 
	}
	
	private void isNewDevice(){
		 	
			String PREFS_NAME = "MyPrefsFile";
		    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
		 	SharedPreferences.Editor editor = settings.edit();
  
			settings = getSharedPreferences(PREFS_NAME, 0);
			String ph = settings.getString("myPhoneNo","00");
			
			if(ph.equals("00")){
				changePhoneNumber(changePhoneTitle);
			}
			else{
				
			}
	}
 
	private void setDistance(){

		
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Change distance of group");
		alert.setMessage("Enter new distance");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(3);
		alert.setView(input);
	 
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		 String value =input.getText().toString();
	 
		 askDistance(value);
		 	//con.CsetDistance(myGroupName, value);
		}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
		}
		});

		alert.show();
	}
 
	private void showPref(){

		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Change Preferences");
		////////////////////////////////////////////////
	  
			final CheckBox ch=new CheckBox(this);
			ch.setText("Activate auto-sunc");
			ch.setChecked(true);
			
			layout.addView(ch);

		final TextView tTopic = new TextView(this);
		tTopic.setText("  Location determining mode");
		tTopic.setTextSize(16);
		layout.addView(tTopic);
		
		final Spinner sp= new Spinner(this);
		
		
		sp.setHapticFeedbackEnabled(true);
		final String[] names={"Network only","GPS preffered"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, names);
		sp.setAdapter(adapter);
		
		if(getString("mode").equals("gps"))	sp.setSelection(1, true);
		
		layout.addView(sp);
		
		alert.setView(layout);
	 
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if(ch.isChecked())		 sendSync(true);
			else				     sendSync(false);
			
			int n=sp.getSelectedItemPosition();
			
			 if(n==0)	putString("mode", "network");
			 if(n==1)	putString("mode", "gps");
			//Toast.makeText(getApplicationContext()," "+n,Toast.LENGTH_SHORT).show();
			
		}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
		}
		});

		alert.show();
	}
	
	public void sendSync(Boolean b){
		
		if(b)	{
			tAuto.setVisibility(View.GONE);
		}
		else	tAuto.setVisibility(View.VISIBLE);
		
		putBoolean("sync",b);
		new ASSync(this,b).execute();
	}
	private void chkSync(){

		if(getBoolean("sync"))	tAuto.setVisibility(View.GONE);
		else	tAuto.setVisibility(View.VISIBLE);
	 
	}
	
	
	
	public void syncON(){
		//putBoolean("sync",true);
	}
	public void syncOFF(){
		//putBoolean("sync",false);
	}
	
	
	
	private void askDistance(final String dis){
		
			pd =  ProgressDialog.show(this,null,"Changing distance value\nwait few seconds",true,false,new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				putString("groupDist", dis);
				Toast.makeText(getApplicationContext(),"Distance changed successfully",Toast.LENGTH_SHORT).show();
				 
			}
			});
			new ASchangeDistancce(this,dis).execute();
	}
	
	private void changePhoneNumber(String title)
	{
	 
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(title);
		alert.setMessage("Enter your phone No.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(2);
		alert.setView(input);
	 
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		 String value =input.getText().toString();

		 if(value.equals("")){changePhoneNumber("Invalied phone number");}
		 else{
			 leaveGroup();	
			 putString("myPhoneNo", value);	 	
			 Toast.makeText(getApplicationContext(), "Your phone No. is "+value,Toast.LENGTH_SHORT).show();
		 }
		 }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
		}
		});

		 alert.show();
	}
	private void removeMember(){
		
		if(!getString("myGroupName").equals("00")){

			/////////////////////////////////////////////////
			pd =  ProgressDialog.show(this,null,"Fetching Members\nwait few seconds",true,false,new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					loadMyMembers();
				}
				});
			new ASgetMemberNames(this).execute();
		   //////////////////////////////////////////////
		}
		else{
			Toast.makeText(getApplicationContext(),"Join a Group",Toast.LENGTH_SHORT).show();			
		}
	}
	
	private void loadMyMembers(){
		 
		if(myMembers!=null){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Remove Members :");
			alert.setMessage("Pick/Type a member phone No. :");

			/////////////////////////
			final Spinner sp= new Spinner(this);

				final String[] nums=myMembers.split(";");
				
				final String[] names=new String[nums.length];
				
				for (int i=0;i<nums.length;i++) {	 
					String name= getContactName(nums[i]);
					names[i]=name;
				}
			
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, names);
				sp.setAdapter(adapter);
				alert.setView(sp);
				/////////////////////////
			
		 
				alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int whichButton) {
					try{
						int i=sp.getSelectedItemPosition();	
						removeThisMember(nums[i]);
						
						if(nums[i].equals(getSystemService("myPhoneNo")))	leaveGroup();
						 
				     }catch(Exception e){
						Toast.makeText(getApplicationContext(),  "Unable to remove member !",Toast.LENGTH_SHORT).show();					
					}
				}
				});
		
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
				     // Canceled.
				}
				});
				 alert.show();
		}
		else{
			Toast.makeText(getApplicationContext(),"No members in this group !",Toast.LENGTH_LONG).show();
			
		}
	}
	
	private void removeThisMember(String number){
	pd =  ProgressDialog.show(this,null,"Wait for removing member",true,false,new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				 Toast.makeText(getApplicationContext(),"Member removed successfully !",Toast.LENGTH_SHORT).show();			
			}
		});
		new ASremoveMember(this,number).execute();
	}
	
	private void addMember(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Add Members :");
		alert.setMessage("Pick/Type a member phone number :");
		
		inputMember = new EditText(this);
		inputMember.setInputType(2);
		alert.setView(inputMember);
	 
		alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		 String value =inputMember.getText().toString();
	 
		 if(value!="") {
			 addMemberToDB(getString("myGroupName"),value,7.0, 81.0);
			 //con.CaddMember(value, myGroupName, 0.0, 0.0);
		 }else{
			 Toast.makeText(getApplicationContext(),"Add a valied identifier !",Toast.LENGTH_SHORT).show();
				
		 }
		}
		});
		
		alert.setNeutralButton("Search", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
				 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		         intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		         startActivityForResult(intent, 3); 
			}
			});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
		}
		});
		 alert.show();
	}
	

	private void addMemberToDB(final String group_Name,String num,Double x,Double y){
		pd =  ProgressDialog.show(this,null,"Inserting member details",true,false,new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
			//	 Toast.makeText(getApplicationContext(),nextGroupString,Toast.LENGTH_SHORT).show();
				 
				putString("groupType",nextGroupString.split(":")[1]);
				putString("myGroupName", group_Name);
				putString("groupDist", nextGroupString.split(":")[2]);
	 
				Toast.makeText(getApplicationContext(),"Member added successfully ",Toast.LENGTH_SHORT).show();
			}
		});
		new ASaddMembers(this,group_Name,num,x,y).execute();
	}
	
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			String memberNumber=null;
			String memberName  = null;
			
			if (data != null) {
		        Uri uri = data.getData();

		        if (uri != null) {
		            Cursor c = null;
		            try {
		                c = getContentResolver().query(uri, new String[]{ 
		                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
		                            ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME},
		                        null, null, null);

		                if (c != null && c.moveToFirst()) {
		                     memberNumber = c.getString(0).replaceAll("-", "");
		                     memberName= c.getString(1);
		                   
		                     try{
		                     
		                    	 addMemberToDB(getString("myGroupName"),memberNumber,7.0, 81.0);
				            }catch(Exception e){
		                    	 Toast.makeText(getApplicationContext(),"Unable to add member !",Toast.LENGTH_SHORT	).show();
		                    	 
		                     }
		                     
		                     }
		            } finally {
		                if (c != null) {
		                   c.close();
		                }
		            }		            
		         }
		    }    
		}
	
	private void authenticate(final String group_Name,final String password){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Authenticate :");
		alert.setMessage("Enter the Password :");
		
		inputMember = new EditText(this);
		inputMember.setTransformationMethod(new PasswordTransformationMethod());
		 
		alert.setView(inputMember);

		alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
		
			public void onClick(DialogInterface dialog, int whichButton) {
			 String value =inputMember.getText().toString();
		 
			  if(value.equals(password))	{
				  locMgr.getMyLocation();
				  addMemberToDB(group_Name,getString("myPhoneNo"), locMgr.getX(), locMgr.getY());
				 
			  }
			  else{
					Toast.makeText(getApplicationContext(), "Wrong Password ! "+getString("myGroupName"),Toast.LENGTH_SHORT).show();					
			  }			
			}
		}
		);
		
 
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		    
		}
		});
		alert.show();
 
	}
	
	private void setMobileDataEnabled(Context context, boolean enabled) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Enable Data Access");
		alert.setMessage("Allow data traffic");
	 
		alert.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int whichButton) {
				enableData();
				putBoolean("sync", true);
				sendSync(true);
		}
		});

		alert.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int whichButton) {
				 sendSync(false);
			     finish();
		}
		});

		alert.show(); 
		
		
	}
	
	private void enableData(){
		try{
			final ConnectivityManager conman = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
			   final Class conmanClass = Class.forName(conman.getClass().getName());
			   Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			   iConnectivityManagerField.setAccessible(true);
			   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
			   final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			   setMobileDataEnabledMethod.setAccessible(true);

			   setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
			}catch(Exception e){}
	}
	
	public void putFloat(String key,Float val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putFloat(key, val);
 		
 		 
 		editor.commit();
	}
	public Float getFloat(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getFloat(key, 500F);
	}
	
	public String getString(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getString(key,"00");
	}
	public void putString(String key,String val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putString(key,val);
 		 
 		editor.commit();
	}
	
	public Boolean getBoolean(String key){
		String PREFS_NAME = "MyPrefsFile";
	    SharedPreferences   settings = getSharedPreferences(PREFS_NAME, 0);
	 	SharedPreferences.Editor editor = settings.edit();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		return settings.getBoolean(key,false);
	}
	
	public void putBoolean(String key,Boolean val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putBoolean(key,val);
  		editor.commit();
	}

		

	public String getContactName(String phoneNumber) 
    {  
        Uri uri;
        String[] projection;

        if (Build.VERSION.SDK_INT >= 5)
        {
            uri = Uri.parse("content://com.android.contacts/phone_lookup");
            projection = new String[] { "display_name" };
        }
        else
        { 
            uri = Uri.parse("content://contacts/phones/filter");
            projection = new String[] { "name" }; 
        } 

        uri = Uri.withAppendedPath(uri, Uri.encode(phoneNumber)); 
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null); 

        String contactName = "";

        if (cursor.moveToFirst()) 
        { 
            contactName = cursor.getString(0);
        } 

        cursor.close();
        cursor = null;

        if(!contactName.equals(""))		return contactName;
        else if(phoneNumber.equals(getString("myPhoneNo")))	return "Me";
        else return phoneNumber;
       
    }

}
