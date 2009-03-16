/****************************************************************************************
 *  TestGeodetic2DBounds.java
 *
 *  Created: Mar 29, 2007
 *
 *  @author Paul Silvey
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

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.mitre.itf.geodesy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TestGeodetic2DBounds extends TestCase {
    private final Logger log = LoggerFactory.getLogger(TestGeodetic2DBounds.class);

    private void evaluateBBox(Geodetic2DBounds bbox, double radius) {
        FrameOfReference f = new FrameOfReference();
        Ellipsoid e = f.getEllipsoid();

        double diag1a, diag1b, diag2, diff, err;
        Geodetic2DPoint sw, ne;

        sw = new Geodetic2DPoint(bbox.getWestLon(), bbox.getSouthLat());
        ne = new Geodetic2DPoint(bbox.getEastLon(), bbox.getNorthLat());
        diag1a = e.orthodromicDistance(sw, ne);
        log.info("Diagonal by distance method 1: " + diag1a);
        Geodetic2DArc arc = new Geodetic2DArc(sw, ne);
        diag1b = arc.getDistanceInMeters();
        log.info("Diagonal by distance method 2: " + diag1b);

        diag2 = 2.0 * Math.sqrt((radius * radius) + (radius * radius));
        diff = Math.abs(diag2 - diag1a);
        err = Math.max((diff / diag2), (diff / diag1a));
        // Look for large errors
        if (Math.abs(err) >= 0.00) {
            log.warn(bbox.toString());
            log.warn("Error in Diagonal = " + (100.0 * err) + " Percent");
            log.warn("Diagonal = " + diag1a + ", expecting " + diag2);
            log.warn("--------------");
        }
    }

    /**
     * This method tests the point-radius bounding box constructor
     */
    public void testPointRadius() {
        Random r = new Random(123L);
        Geodetic2DPoint pt;
        double radius;
        Geodetic2DBounds bbox;

        for (int i = 0; i < 1; i++) {
            try {
                pt = TestGeoPoint.randomGeodetic2DPoint(r);
                radius = 1.0 * r.nextInt(1000000);      // stress test at 1,000 km.

                bbox = new Geodetic2DBounds(pt, radius);
                evaluateBBox(bbox, radius);

            } catch (IllegalArgumentException ex) {
                log.error(ex.toString());
            }
        }
        System.out.println();
    }

    public void testGeodetic2DArc() {
        Geodetic2DPoint pt;

        pt = new Geodetic2DPoint(new Longitude("12,34,56E"), new Latitude("45,34,23N"));
        double distance = 123456.0;
        Angle azimuth = new Angle(33.0, Angle.DEGREES);
        Geodetic2DArc arc = new Geodetic2DArc(pt, distance, azimuth);
        log.info(arc.getPoint1().toString(5));
        log.info(arc.getPoint2().toString(5));
    }

    public void testBBox() {
        // bbox1: (161° 54' 44" E, 85° 41' 54" S) .. (99° 8' 8" E, 79° 39' 57" N)
        // bbox2: (91° 4' 4" E, 89° 57' 12" S) .. (0° 13' 54" W, 87° 50' 13" N)
        // together??: (161° 54' 44" E, 89° 57' 12" S) .. (99° 8' 8" E, 87° 50' 13" N)

        Geodetic2DPoint west = new Geodetic2DPoint("(161° 54' 44\" E, 85° 41' 54\" S)");
        Geodetic2DPoint east = new Geodetic2DPoint("(99° 8' 8\" E, 79° 39' 57\" N)");
        Geodetic2DBounds bbox1 = new Geodetic2DBounds(west, east);

        Geodetic2DPoint c = bbox1.getCenter();
        assertTrue(east.getLatitude().inDegrees() >= c.getLatitude().inDegrees());
        assertTrue(west.getLongitude().inRadians() >= c.getLongitude().inRadians());

        west = new Geodetic2DPoint("(91° 4' 4\" E, 89° 57' 12\" S)");
        east = new Geodetic2DPoint("(0° 13' 54\" W, 87° 50' 13\" N)");
        Geodetic2DBounds bbox2 = new Geodetic2DBounds(west, east);

        bbox1.include(bbox2);
        System.out.println();
        System.out.println(bbox1);
        
        Geodetic2DPoint outside = new Geodetic2DPoint(new Longitude(172, 54, 44),
                new Latitude(89, 41, 54));
        assertFalse(bbox1.contains(outside));
    }

    public void testRandomBBox() {
        Random r = new Random();
        FrameOfReference f = new FrameOfReference();
        GeoPoint pt = TestGeoPoint.randomGeodetic2DPoint(r);
        Geodetic2DBounds bbox1 = new Geodetic2DBounds(pt.toGeodetic3D(f));
        for (int i = 0; i < 10; i++) {
            pt = TestGeoPoint.randomGeodetic2DPoint(r);
            System.out.print(pt + " -> ");
            bbox1.include(pt.toGeodetic3D(f));
            System.out.println(bbox1);
        }
        System.out.println();
        Geodetic2DBounds bbox2 = new Geodetic2DBounds(pt.toGeodetic3D(f));
        for (int i = 0; i < 10; i++) {
            pt = TestGeoPoint.randomGeodetic2DPoint(r);
            System.out.print(pt + " -> ");
            bbox2.include(pt.toGeodetic3D(f));
            System.out.println(bbox2);
        }
        System.out.println();
        bbox1.include(bbox2);
        System.out.println(bbox1);
    }

    /**
     * Test bounds that wraps Longitude at the International Date Line (IDL)
     */
    public void testIDL() {
        Geodetic2DPoint west = new Geodetic2DPoint(new Longitude(-Math.PI),
                new Latitude(0)); // -180 0
        Geodetic2DPoint east = new Geodetic2DPoint(new Longitude(30, Angle.DEGREES),
                new Latitude(60, Angle.DEGREES));
        Geodetic2DBounds bbox = new Geodetic2DBounds(west, east);
        Geodetic2DPoint c = bbox.getCenter();
        System.out.println(c + " -> " + bbox);
        assertTrue(east.getLatitude().inDegrees() >= c.getLatitude().inDegrees());
        assertTrue(bbox.contains(west));
    }

    /**
     * Main method for running class tests.
     *
     * @param args standard command line arguments - ignored.
     */
    public static void main(String[] args) {
        TestSuite suite = new TestSuite(TestGeodetic2DBounds.class);
        new TestRunner().doRun(suite);
    }
}
