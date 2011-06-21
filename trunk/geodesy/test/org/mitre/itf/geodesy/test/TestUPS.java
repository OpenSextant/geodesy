package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import org.mitre.itf.geodesy.*;

import java.util.Random;

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

	public void testInvalidCreation() {
		try {
			// Hemisphere 'X', should be 'N' or 'S'
			new UPS('X', 2364053.5818, 1718278.1249);
			fail("Expected to throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			// Northing valid range (0 to 4,000,000 meters)
			new UPS('N', 0, 4818278.1249);
			fail("Expected to throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			// Easting valid range (0 to 4,000,000 meters)
			new UPS('N', 4818278.1249, 0);
			fail("Expected to throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

		try {
			// out of legal range (+83.5 deg to +90 deg) for UPS Northern Hemisphere")
			new UPS(new Geodetic2DPoint(
				new Longitude(-79, 23, 13.7),
				new Latitude(0)));
			fail("Expected to throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}

	}

	public void testProjections() {
        Geodetic2DPoint g1, g2;
        UPS u1, u2;
        int fractDig = 2; // Fractional digits to print in toString conversions

		// WGS 84 UPS A 997635.0902m E, 2464957.4007m N => (65° 6' 55" W, 80° 4' 17" S)
        g1 = new Geodetic2DPoint(new Longitude(-65, 6, 55),
				new Latitude(-80, 4, 17));
        u1 = new UPS(g1);
        u2 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
        g2 = u2.getGeodetic();
        assertEquals(g1.toString(fractDig), g2.toString(fractDig));

		// WGS 84 UPS Z 2364053.5818m E, 1718278.1249m N => (52° 15' 56" E, 85° 51' 20" N)
		g1 = new Geodetic2DPoint(new Longitude(-52, 15, 56),
				new Latitude(85, 51, 20));
        u1 = new UPS(g1);
        u2 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
        g2 = u2.getGeodetic();
		assertEquals(g1.toString(fractDig), g2.toString(fractDig));

		// Do some random Geodetic point conversion round trips
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            double lonDeg = (r.nextDouble() * 360.0) - 180.0;
			// Latitude range +83.5 deg to +90 for UPS Northern Hemisphere
            double latDeg = 83.5 + (6.5 * r.nextDouble());
            g1 = new Geodetic2DPoint(
                    new Longitude(lonDeg, Angle.DEGREES), new Latitude(latDeg, Angle.DEGREES));
            u1 = new UPS(g1);
            u2 = new UPS(u1.getHemisphere(), u1.getEasting(), u1.getNorthing());
            g2 = u2.getGeodetic();
            assertEquals(g1.toString(fractDig), g2.toString(fractDig));
        }
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
