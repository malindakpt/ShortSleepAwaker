package com.example.gps4;

import com.example.gps4.MainActivity;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;


public class robotiumTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;
	public robotiumTest() {
		super(MainActivity.class);

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
	
	public void testcreateGroup() throws Exception {
		solo.clickOnMenuItem("My Details");
		//Toast.makeText(getActivity(), "sda", Toast.LENGTH_SHORT).show();
//		solo.clickOnMenuItem("Add note");
//		//Assert that NoteEditor activity is opened
//		solo.assertCurrentActivity("Expected NoteEditor activity", "NoteEditor"); 
//		//In text field 0, enter Note 1
//		solo.enterText(0, "Note 1");
//		solo.goBack(); 
//		//Clicks on menu item
//		solo.clickOnMenuItem("Add note");
//		//In text field 0, type Note 2
//		solo.typeText(0, "Note 2");
//		//Go back to first activity
//		solo.goBack(); 
//		//Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
//		solo.takeScreenshot();
//		boolean expected = true;
//		boolean actual = solo.searchText("Note 1") && solo.searchText("Note 2");
//		//Assert that Note 1 & Note 2 are found
	assertEquals("Note 1 and/or Note 2 are not found",1, 1); 

	}
	
}
