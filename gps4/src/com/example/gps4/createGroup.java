package com.example.gps4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class createGroup  extends Activity{

	 double x,y;
	 static ImageButton bCreateGroup;
	 static EditText tGroupName,tPassword1,tPassword2,tDist;
	 static TextView txt1st,txt2nd,tKm;
	 static String myPhone="";
	 static RadioGroup radgrp;
	 static RadioButton rdb0,rdb1,rdb2;
	 static CheckBox ch;
	 static int idx;
	 static String password;
	 ProgressDialog pd;
	
	public static boolean status;
	connection con;
	
	
	
	private void init(){
		
		password="";
		
		bCreateGroup=(ImageButton) findViewById(R.id.bCreate);
		tGroupName = (EditText) findViewById(R.id.tGroupName6);
		tPassword1 = (EditText) findViewById(R.id.tPassword7);
		tPassword2 = (EditText) findViewById(R.id.tPassword8);
		tDist = (EditText) findViewById(R.id.tDistance);
		radgrp=(RadioGroup) findViewById(R.id.radioGroup1);
		rdb0=(RadioButton) findViewById(R.id.radConverge);
		rdb1=(RadioButton) findViewById(R.id.radDiverge);
		rdb2=(RadioButton) findViewById(R.id.radNone);
 
		x=getIntent().getExtras().getDouble("x");
		y=getIntent().getExtras().getDouble("y");
		myPhone=getIntent().getExtras().getString("myPhone");
		 	 
		rdb0.setTextColor(Color.BLACK);
		rdb1.setTextColor(Color.BLACK);
		rdb2.setTextColor(Color.BLACK);
		
		ch=(CheckBox) findViewById(R.id.checkBox1);
		ch.setTextColor(Color.BLACK);
		tKm=(TextView) findViewById(R.id.textView1);
		tKm.setTextColor(Color.BLACK);
		
		tPassword1.setEnabled(false);
		tPassword2.setEnabled(false);
	
	}
	
	
	public void putString(String key,String val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putString(key,val);
 		editor.commit();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group);
		con=new connection();
		init();
	
		bCreateGroup.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
 			
				if(tGroupName.equals("") || tGroupName.equals("Group Name"))
					Toast.makeText(getApplicationContext(), "Enter a group name", Toast.LENGTH_SHORT).show();
	
				else
					create();
			}
		});
		 
		ch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ch.isChecked()){
					 
					tPassword1.setEnabled(true);
					tPassword2.setEnabled(true);					
					
					tPassword1.setText("");
					tPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
					
					tPassword2.setText("");
					tPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
					
				}else {
					
					//password="";
					
					tPassword1.setEnabled(false);
					tPassword2.setEnabled(false);
					
					tPassword1.setInputType(0);
					tPassword2.setInputType(0);
					tPassword1.setText("Password");
					tPassword2.setText("Re-type Password");
				}
				
			}
		});
		
		tGroupName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tGroupName.setText("");
			}
		});
		
		radgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		      public void onCheckedChanged(RadioGroup arg0, int id) {
		        switch (id) {
				     
				        case R.id.radConverge:
				        	tDist.setEnabled(true);
				        	 
				        break;
				        
				        case R.id.radDiverge:
				        	tDist.setEnabled(true);
				        	 
				        break;
				        
				        case R.id.radNone:
				        	tDist.setEnabled(false);
				        
				        break;
				        
				        default:
				          
				        break;
		        }
		      }
		});
		


		
	 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void create(){
		if(tPassword1.getText().toString().equals(tPassword2.getText().toString()) || !ch.isChecked()){
			
			int radioButtonID = radgrp.getCheckedRadioButtonId();
			View radioButton = radgrp.findViewById(radioButtonID);
			idx = radgrp.indexOfChild(radioButton);

		//////////////////////////////updater/////////////////
		 
			pd =  ProgressDialog.show(this,null,"Verifing Name \nwait few seconds.",true,false,new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					saveGroupDetails();
				}
			});
			 	
			if(ch.isChecked())password=tPassword1.getText().toString();
			else				password="";
			
 			new AScreateGroups(this).execute();
		////////////////////////////////////////////////////
		
		
		}
			else {
			Toast.makeText(getApplicationContext(), "Password mismatch",Toast.LENGTH_SHORT).show();				
		}
		 
	}
	
	public void saveGroupDetails(){
		if(status){
			
			Toast.makeText(getApplicationContext(), "Join "+tGroupName.getText().toString()+" to see members",Toast.LENGTH_SHORT).show();	
			finish();
		}
		else{
			Toast.makeText(getApplicationContext(), "Group name is already exist !",Toast.LENGTH_SHORT).show();
		}
		
	}
	public void syncON(){
		//putBoolean("sync",true);
	}
	public void syncOFF(){
		//putBoolean("sync",false);
	}
	public void putBoolean(String key,Boolean val){
 		SharedPreferences   settings = getSharedPreferences("MyPrefsFile", 0);
 		SharedPreferences.Editor editor = settings.edit();
 		editor.putBoolean(key,val);
 		 
 		editor.commit();
	}

}
