package com.example.gps4;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
 

public class test extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	public test(Class<MainActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUp() throws Exception {
		//setUp() is run before a test case is started. 
		//This is where the solo object is created.
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		//tearDown() is run after a test case has finished. 
		//finishOpenedActivities() will finish all the activities that have been opened during the test execution.
		solo.finishOpenedActivities();
	}

	public void testAddNote() throws Exception {
		
 	 

	}
	 
	public void testEditNote() throws Exception {
	 

	}
	
	public void testRemoveNote() throws Exception {
		  
	}
	
	public void testRemoveNote2() throws Exception {
	 
	}

}
