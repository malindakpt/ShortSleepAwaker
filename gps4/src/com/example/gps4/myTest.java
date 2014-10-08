package com.example.gps4;

import android.text.style.BackgroundColorSpan;
import junit.framework.TestCase;

public class myTest extends TestCase {
	 
	
	protected void setUp() throws Exception {
		super.setUp();
 
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDis() {
	 
		 double d=calculator.getDis(6.9344F, 79.8428F, 7.2964F, 80.6350F);
		 assertEquals(96.23356628417969, d);
		 
		 d=calculator.getDis(0F, 0F, 0F, 0F);
		 assertEquals(0.0, d);
	}

	public void testDeg2rad() {
		
		double f= calculator.deg2rad(180F);
		assertEquals(3.1415927410125732,f);
		
		 f= calculator.deg2rad(0F);
		assertEquals(0.0,f);
	 
	}
	
	public void testAddNames(){
		assertEquals("ab,cd",calculator.addNames("ab", "cd") );
		assertEquals("cd",calculator.addNames("", "cd") );
		assertEquals("",calculator.addNames(null, "cd") );
		assertEquals("",calculator.addNames("ab", null) );
	}

}
