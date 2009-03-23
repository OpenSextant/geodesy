/****************************************************************************************
 *  TestGeodetic3DBounds.java
 *
 *  Created: Mar 29, 2007
 *
 *  @author Jason Mathews
 *
 *  (C) Copyright MITRE Corporation 2006
 *
 *  The program is provided "as is" without any warranty express or implied, including 
 *  the warranty of non-infringement and the implied warranties of merchantibility and 
 *  fitness for a particular purpose.  The Copyright owner will not be liable for any 
 *  damages suffered by you as a result of using the Program.  In no event will the 
 *  Copyright owner be liable for any special, indirect or consequential damages or 
 *  lost profits even if the Copyright owner has been advised of the possibility of 
 *  their occurrence.
 *
 ***************************************************************************************/
package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.mitre.itf.geodesy.*;

import java.util.Random;

public class TestGeodetic3DBounds extends TestCase {

    public void testBBox() {
        // bbox1: (161° 54' 44" E, 85° 41' 54" S) .. (99° 8' 8" E, 79° 39' 57" N)
        // bbox2: (91° 4' 4" E, 89° 57' 12" S) .. (0° 13' 54" W, 87° 50' 13" N)
        // together??: (161° 54' 44" E, 89° 57' 12" S) .. (99° 8' 8" E, 87° 50' 13" N)

        double minElev = 100;
        double maxElev = 110;

        Geodetic3DPoint west = new Geodetic3DPoint(new Longitude(161, 54, 44),
                new Latitude(-85, 41, 54), minElev);
        Geodetic3DPoint east = new Geodetic3DPoint(new Longitude(99, 8, 8),
            new Latitude(79, 39, 57), maxElev);
        Geodetic3DBounds bbox1 = new Geodetic3DBounds(west, east);

        assertTrue(bbox1.contains(east));

        Geodetic3DPoint cPt = bbox1.getCenter();
        assertTrue(cPt.getElevation() >= minElev);
        assertTrue(cPt.getElevation() <= maxElev);

        // expand the volume of the bounding box
        minElev -= 10;
        maxElev += 10;
        Geodetic3DPoint west2 = new Geodetic3DPoint(new Longitude(91, 4, 4),
                new Latitude(-89, 57, 12), maxElev);
        Geodetic3DPoint east2 = new Geodetic3DPoint(new Longitude(-1, 13, 54),
                new Latitude(87, 50, 13), minElev);
        Geodetic3DBounds bbox2 = new Geodetic3DBounds(west2, east2);

        bbox1.include(bbox2);

        cPt = bbox1.getCenter();
        assertTrue(cPt.getElevation() >= minElev);
        assertTrue(cPt.getElevation() <= maxElev);

        assertTrue(bbox1.contains(bbox2));
        assertTrue(bbox1.contains(east2));

        Geodetic3DPoint outside = new Geodetic3DPoint(new Longitude(172, 54, 44),
                new Latitude(89, 41, 54), minElev);
        assertFalse(bbox1.contains(outside));

        Geodetic3DPoint ptTooHigh = new Geodetic3DPoint(cPt.getLongitude(), cPt.getLatitude(), maxElev * 2);
        assertFalse(bbox1.contains(ptTooHigh));        
    }    

    public void testRandomBBox() {
        Random r = new Random();
        FrameOfReference f = new FrameOfReference();
        Geodetic3DPoint pt = TestGeoPoint.randomGeodetic3DPoint(r);
        Geodetic3DBounds bbox1 = new Geodetic3DBounds(pt.toGeodetic3D(f));
        for (int i = 0; i < 10; i++) {
            pt = TestGeoPoint.randomGeodetic3DPoint(r);
            bbox1.include(pt.toGeodetic3D(f));
        }
        assertTrue(bbox1.contains(pt));
        Geodetic3DBounds bbox2 = new Geodetic3DBounds(pt.toGeodetic3D(f));
        for (int i = 0; i < 10; i++) {
            pt = TestGeoPoint.randomGeodetic3DPoint(r);
            bbox2.include(pt.toGeodetic3D(f));
        }
        assertTrue(bbox2.contains(pt));
        bbox1.include(bbox2);
        assertTrue(bbox1.contains(bbox2));
        assertTrue(bbox1.contains(pt)); // sometimes this test fails: see comment beloiw
        /*
         try {
            assertTrue(bbox1.contains(pt));
        } catch(Throwable e) {
            // once in a while there is an null assertion error here.
            e.printStackTrace();
            System.out.println(bbox1);
            System.out.println("center=" + bbox1.getCenter());
            System.out.println(pt);
            fail();
        }
        somehow the east/west lon get switched or we're wrapping the world
        example: west lon = -61 east lin = -81
            (61° 53' 58" W, 89° 37' 35" S) .. (81° 9' 34" W, 89° 33' 41" N) .. (1802.0m, 997780.0m)
            center=(108° 28' 14" E, 0° 1' 57" S) @ 499791m
            (64° 49' 50" W, 80° 39' 21" S) @ 853675m
         */
    }

    /**
     * Main method for running class tests.
     *
     * @param args standard command line arguments - ignored.
     */
    public static void main(String[] args) {
        TestSuite suite = new TestSuite(TestGeodetic3DBounds.class);
        new TestRunner().doRun(suite);
    }
}
