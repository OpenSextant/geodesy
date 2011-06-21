package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import org.mitre.itf.geodesy.*;

/**
 * User: MATHEWS
 * Date: 6/21/11 10:41 AM
 */
public class TestUPS extends TestCase {

	public void testEquals() {
		Geodetic2DPoint g1 = new Geodetic2DPoint(
				new Longitude(-79, 23, 13.7),
				new Latitude(88, 38, 33.24));
		UPS u1 = new UPS(g1);

		UPS u2 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
		assertEquals(u1, u2);
		assertEquals(u1.hashCode(), u2.hashCode());

		UPS u3 = new UPS(u1.getEllipsoid(), u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
		assertEquals(u1, u3);
		assertEquals(u1.hashCode(), u3.hashCode());

		UPS u4 = new UPS(u1.getEllipsoid(), g1);
		assertEquals(u1, u4);
		assertEquals(u1.hashCode(), u4.hashCode());

		UPS u5 = new UPS(u1.getEllipsoid(), g1.getLongitude(), g1.getLatitude());
		assertEquals(u1, u5);
		assertEquals(u1.hashCode(), u5.hashCode());

		UPS u6 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing() + 500);
		assertFalse(u1.equals(u6));

		UPS u7 = null;
		assertFalse(u1.equals(u7));
	}

	public void testProjections() {
        Geodetic2DPoint g1, g2;
        UPS u1, u2;
        int fractDig = 2; // Fractional digits to print in toString conversions

        // utm.toGeodetic(17, 'N', 630084.30, 4833438.55) => (79° 23' 13.70" W, 43° 38' 33.24" N)
        //  MGRS? 17TPJ 30084 33439
        g1 = new Geodetic2DPoint(new Longitude(-79, 23, 13.7),
				new Latitude(88, 38, 33.24));
        u1 = new UPS(g1);
        u2 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
        g2 = u2.getGeodetic();
        assertTrue(g1.toString(fractDig).equals(g2.toString(fractDig)));

		/*
        u1 = new UPS('N', 630084.30, 4833438.55);
        g1 = u1.getGeodetic();
        u2 = new UPS(g1);
        assertTrue(u1.toString(fractDig).equals(u2.toString(fractDig)));
        */
	}

	public void testToString() {

		UPS u1 = new UPS('N', 621160.08, 3349893.03);
		// tp.toString(0) WGS 84 UPS Z 621160m E, 3349893m N
		// tp.toString(4) WGS 84 UPS Z 621160.0800m E, 3349893.0300m N

		String base = u1.toString();
		assertEquals(base, u1.toString(0)); // toString() same as toString(0)
		// System.out.println(base);
		int prevLen = base.length();
		String prefix = base.substring(0, base.indexOf("m E")); // WGS 84 UPS Z 621160
		// System.out.printf("prefix=[%s]%n", prefix);

		for (int i = 1; i < 6; i++) {
			String s = u1.toString(i);
			// System.out.println(s);
			int len = s.length();
			assertTrue(len >= prevLen + 2);
			// simple tests: each should start with same easting whole number
			assertEquals(prefix, s.substring(0, prefix.length()));
			// assertTrue("prefix fails to match target", s.startsWith(prefix));
			prevLen = len;
		}
	}
}
