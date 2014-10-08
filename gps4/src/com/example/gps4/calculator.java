package com.example.gps4;

public class calculator {
	
	static Float getDis(Float lat1,Float lon1,Float lat2,Float lon2) {
		Float R = 6371F; // Radius of the earth in km
		Float  dLat = deg2rad(lat2-lat1);  // deg2rad below
		Float  dLon = deg2rad(lon2-lon1); 
		Float  a =   (float) (Math.sin(dLat/2) * Math.sin(dLat/2) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *  Math.sin(dLon/2) * Math.sin(dLon/2))  ; 
		Float  c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))); 
		Float  d = R * c; // Distance in km
		return d;
		}
	
	static Float deg2rad(Float deg) {
		  return (float) (deg * (Math.PI/180));
	}
	
	public static String addNames(String s1,String s2){
		if(s1!=null && s2!=null){
			if(s1.equals(""))	return s2;
			else				return s1+","+s2;
		}
		else
			return "";
	}
}
