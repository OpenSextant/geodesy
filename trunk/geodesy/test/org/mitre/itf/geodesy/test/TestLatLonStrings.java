/*
 * TestLatLonStrings.java
 *
 * Created on January 17, 2008, 5:15 PM
 *
 * $Id$
 */
package org.mitre.itf.geodesy.test;

import org.mitre.itf.geodesy.LatLonParser;
import org.mitre.itf.geodesy.Geodetic2DPoint;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

public class TestLatLonStrings extends TestCase {

	String[] testStringsEtrex = {
    		/* DD-MM.MMMH,DDD-MM.MMMH) */
			
			"36-22.123,048-05.881",

			"36-22.123N,048-05.881E",
			"36-22.123n,048-05.881e",
			"36-22.123N,048-05.881W",
			"36-22.123n,048-05.881w",
			
			"36-22.123S,048-05.881E",
			"36-22.123s,048-05.881e",
			"36-22.123S,048-05.881W",
			"36-22.123s,048-05.881w",
			
			"36-22.123456s,048-05.8811111111111111111111111111111w",
			"36-22.1233s,048-05.88166666666666666666666666666666w"
	};
	
    String[] testStrings = {
 
    		/*Lat/Lon hddd.ddddd	*/ "S26.25333 E27.92333",
    		/*Lat/Lon hddd°mm.mmm*/	"N33 56.539 W118 24.471",
    		/*Lat/Lon hddd°mm'ss.s" */ "N30 56 12.3 W118 24 12.3",
    		"77°02'06.00\"38°53'20.76\"",
    		"W77°02'06.00\"38°53'20.76\"",
    		"77°02'06.00\"38°53'20.76\"N",
    		"77°02'06.00\"N38°53'20.76\"",    		
    		
    		"-314/61239",
    		"+38 -77",
    		"38+ 77-",
    		"+38 77-",
    		"38+ -77",
    		"38 77",
    		"+38 77",
    		"38 77-",
    		"38+ 77",
    		"38 -77",
    		
    		"W77°02'06.00\"38°53'20.76\"N",
    		"-38.889097 77.035000",
    		"38.889097 -77.035000",
    		"-0770206.00+385320.76",
    		"385320.76N 0770206.00W",
    		"+385320.76 -0770206.00",
    		"0770206.00W N385320.76",
    		"N385320.76 W0770206.00",
    		"385320.76N0770206.00W",

    		
      		".76E 0750206004797S",
      		"1.76E 0750206004797S",
      		"16.76E 0750206004797S",
      		"165.76E 0750206004797S",
    	  	"1653.76E 0750206004797S",
    		"16532.76E 0750206004797S",
    		"165320.76E 0750206004797S",
    		"1653201.76E 0750206004797S",
       		"385320S 0770206E",
       		"385320.76N 0770206.00W",
       		"385320.76S 0770206.004797E",
       		"3853207623S 0770206004797E",
         		
    		// ERROR Cases
    		"38.889097 -+",
    		"++38.889097",
    		"-+-38.889097",

    		// DD
    		"38d53m20.76sN 77d2m6.00sW",
    		"38.889097 -77.035000",
    		"+38.889097 -77.035000",
    		"W77.035000 38.889097N",
    		"W77.035000 N38.889097",    		
    		"77.035000W 38.889097N",
    		"77.035000W N38.889097",
    		"N38.889097 77.035000W",
    		"N38.889097 W77.035000",
    		"38.889097N 77.035000W",
    		"38.889097N W77.035000",
    		"38.889097S 77.035000W",
    		"38.889097S 77.035000E",
    		"38.889097N77.035000W",
    		"38.889097°-77.035000°",
    		"38.889097,-77.035000",
    		"38.889097/-77.035000",
    		"38.889097|-77.035000",
    		"38.8891023 -77.0350654",
    		"38.89 -77.0350",
    		"38 -77",

    		// DMS
    		"385320.76N 0770206.00W",
    		"+385320.76 -0770206.00",
    		"0770206.00W N385320.76",
    		"N385320.76 W0770206.00",
    		"385320.76N0770206.00W",
    		"W77°02'06.00\"38°53'20.76\"N",
    		"385320.76 N;0770206.00 W",

    		"38d53m20.76sS 77d2m6.00sW",
    		"38d53m20.76sN W77d2m6.00s",
    		"38 53 20.76N 77 2 6.00W",
    		"38:53:20.76N:077:02:06.00W",
    		"38/53/20.76N/77/2/6.00W",
    		"W77,2,6,N38,53,20.76",
    		"-0770206.00+385320.76",
    		"385320.7623N 0770206W",
    		"385320.76N 0770206.004797W",
    		"385320N 0770206W",
    		"3853207623N 0770206004797W",
    		"38532076N077020600W"
    };
    
    /**
     * Test the LatLonParser class with test strings
     */
    public void testLatLonParserEtrex() {
       	System.out.print("\n\n LatLonParser Test (Etrex unique format)\n\n");
    	String latLon = new String();
    	LatLonParser parser = new LatLonParser();
    	for (String testStringEtrex : testStringsEtrex) {
    		try {
    			latLon = parser.parseEtrexString(testStringEtrex);
    			System.out.print(testStringEtrex + " ----> " + latLon + "\n");
    		} catch (IllegalArgumentException e) {
    			System.err.println(testStringEtrex + "Illegal Argument Exception");
    		}
    	}
 
    }
    
    /**
     * Test the LatLonParser class with test strings
     */
    public void testLatLonParser() {
       	System.out.print("\n\n LatLonParser Test \n\n");
    	String latLon = new String();
    	LatLonParser parser = new LatLonParser();
    	for (String testString : testStrings) {
    		try {
    			latLon = parser.parseString(testString);
    			System.out.print(testString + " ----> " + latLon + "\n");
    		} catch (IllegalArgumentException e) {
    			System.err.println(testString  + "Illegal Argument Exception");
    		}
    	}
 
    }
  
    /**
     * Test the LatLonParser class and Geodetic2DPoint class with test strings
     */
    public void testLatLonParserGeodetic2DPoint() {
    	
    	System.out.print("\n\n LatLonParserGeodetic2DPoint Test \n\n");
    	String latLon = new String();
    	
    	LatLonParser parser = new LatLonParser();
    	Geodetic2DPoint point;
    	
       	for (String testString : testStrings) {   		
			try {
				latLon = parser.parseString(testString);
			} catch (IllegalArgumentException e) {
    			System.out.print("\nError: parse exception with  " + testString + "\n");
    			continue;
    		}
			
			try {
				point = new Geodetic2DPoint(latLon);
			} catch (Exception e) {
				System.out.print("\nError: Geodetic2DPoint with " + latLon + "\n");
				continue;
			}
			
			System.out.print(testString + " ----> " + 
					latLon + " ----> " +
					point.toString() + "\n");
    	}

    }
  
	/**
	 * Main method for running class tests.
	 *
	 * @param args standard command line arguments - ignored.
	 */
	public static void main(String[] args) {
		TestSuite suite = new TestSuite(TestLatLonStrings.class);
		new TestRunner().doRun(suite);
	}
}