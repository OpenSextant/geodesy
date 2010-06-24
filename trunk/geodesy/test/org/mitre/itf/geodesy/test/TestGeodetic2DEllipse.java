/****************************************************************************************
 *  TestGeodetic2DEllipse.java
 *
 *  Created: Jun 24, 2010 9:14:59 AM
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
import org.mitre.itf.geodesy.Angle;
import org.mitre.itf.geodesy.Geodetic2DEllipse;
import org.mitre.itf.geodesy.Geodetic2DPoint;

public class TestGeodetic2DEllipse extends TestCase {

	private static final double EPSILON = 1E-5;

	public void testCreation() {
		Geodetic2DEllipse ellipse = new Geodetic2DEllipse();
		Geodetic2DPoint cp = ellipse.getCenter();
		assertNotNull(cp);

		Geodetic2DPoint boston =
                new Geodetic2DPoint("42\u00B0 22' 11.77\" N, 71\u00B0 1' 40.30\" W");

		Geodetic2DEllipse geo = new Geodetic2DEllipse(boston, 4000, 2500, new Angle(45, Angle.DEGREES));
		assertEquals(boston,  geo.getCenter());
		assertEquals(4000.0, geo.getSemiMajorAxis(), EPSILON);
		assertEquals(2500.0, geo.getSemiMinorAxis(), EPSILON);
		assertEquals(45.0, geo.getOrientation().inDegrees(), EPSILON);

		Geodetic2DEllipse geo2 = new Geodetic2DEllipse(boston, 4000.0, 2500.0, geo.getOrientation());
		assertEquals(geo, geo2);
		assertEquals(geo.hashCode(), geo2.hashCode());
		assertFalse(ellipse.equals(geo));
	}
	
}
