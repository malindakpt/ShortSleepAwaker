package com.example.gps4;

import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class changePreferences extends FragmentActivity{
	
	
	static RadioButton rdb0,rdb1,rdb2;
	static CheckBox ch;
	static RadioGroup rg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefe);
		
		rdb0 =(RadioButton) findViewById(R.id.radio0);
		rdb1 =(RadioButton) findViewById(R.id.radio1);
		rdb2 =(RadioButton) findViewById(R.id.radio2);
		rg   =(RadioGroup)  findViewById(R.id.radioGroup1);
		
		ch=(CheckBox) findViewById(R.id.checkBox1);
		
		rdb0.setTextColor(Color.BLACK);
		rdb1.setTextColor(Color.BLACK);
		rdb2.setTextColor(Color.BLACK);
		ch.setTextColor(Color.BLACK);
		rg.setBackgroundColor(Color.WHITE);
		
	 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
