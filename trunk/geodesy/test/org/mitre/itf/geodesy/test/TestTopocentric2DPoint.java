package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import org.mitre.itf.geodesy.FrameOfReference;
import org.mitre.itf.geodesy.Geodetic3DPoint;
import org.mitre.itf.geodesy.Topocentric2DPoint;
import org.mitre.itf.geodesy.Topocentric3DPoint;

/**
 * User: MATHEWS
 * Date: 6/20/11 2:20 PM
 */
public class TestTopocentric2DPoint extends TestCase {

	public void testCreate() {
		double easting = 630084;
		double northing = 4833439;
		Topocentric2DPoint tp = new Topocentric2DPoint(easting, northing);
		Topocentric3DPoint point = tp.toTopocentric3D();
		assertNotNull(point); // (630084m East, 4833439m North) @ 0m
		assertEquals(tp.getEasting(), point.getEasting(), 10e-8);
		assertEquals(tp.getNorthing(), point.getNorthing(), 10e-8);
		FrameOfReference f = new FrameOfReference();
		Geodetic3DPoint pt = tp.toGeodetic3D(f); // (5� 38' 31" E, 37� 10' 6" N) @ 1657072m
		assertNotNull(pt);
	}

	public void testEquals() {
		double easting = 630084;
		double northing = 4833439;
		Topocentric2DPoint tp = new Topocentric2DPoint(easting, northing);
		Topocentric2DPoint tp2 = new Topocentric2DPoint(easting, northing);
		assertEquals(tp, tp2);
		assertTrue(tp.hashCode() == tp2.hashCode());

		tp2.setEasting(easting + 123);
		tp2.setNorthing(northing  - 123);
		assertFalse(tp.equals(tp2));
	}

	public void testToString() {
		double easting = 630084.12345;
		double northing = 4833439.6789;
		Topocentric2DPoint tp = new Topocentric2DPoint(easting, northing);
        int prevLen = 0;

		// tp.toString(0) (630084m East, 4833439m North)
		// tp.toString(8) (630084.00000000m East, 4833439.00000000m North)

        String base = tp.toString();
        String prefix = base.substring(0, base.indexOf('m'));   // e.g. "100m
        // String suffix = base.substring(base.length() - 2);      // e.g. "m)"
		// System.out.println(prefix + " " + suffix);

		for(int i=0; i < 6; i++) {
            String s = tp.toString(i);
            int len = s.length();
            assertTrue(len >= prevLen + 2);
            // simple tests: each should start with same easting whole number
            assertTrue(s.startsWith(prefix));
            // assertTrue(s.endsWith(suffix));
            prevLen = len;
		}
	}
}
