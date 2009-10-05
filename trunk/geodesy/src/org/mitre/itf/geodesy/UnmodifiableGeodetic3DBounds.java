package org.mitre.itf.geodesy;

/**
 * Provides an unmodifiable view of the specified bounding box.  This class
 * allows modules to provide users with "read-only" access to internal
 * bounds.  Attempts to modify the bounds result in an
 * <tt>UnsupportedOperationException</tt>.<p>
 *
 * @author Jason Mathews, MITRE Corp.
 * Date: Oct 5, 2009 1:57:23 PM
 */
public class UnmodifiableGeodetic3DBounds extends Geodetic3DBounds {

	// TODO: Geodetic3DBounds still has its fields (minElev + maxElev) public
	// which allow direct modification of its internal state. Should make these private.
	
	public UnmodifiableGeodetic3DBounds(Geodetic3DBounds bbox) {
		super(bbox);
	}

	public void include(Geodetic3DPoint newPoint) {
		throw new UnsupportedOperationException();
	}

	public void include(Geodetic3DBounds bbox) {
		throw new UnsupportedOperationException();
	}

	public void include(Geodetic2DPoint newPoint) {
		throw new UnsupportedOperationException();
	}

	public void include(Geodetic2DBounds bbox) {
		throw new UnsupportedOperationException();
	}

	public void setWestLon(final Longitude westLon) {
		throw new UnsupportedOperationException();
	}

	public void setEastLon(final Longitude eastLon) {
		throw new UnsupportedOperationException();
	}

	public void setSouthLat(final Latitude southLat) {
		throw new UnsupportedOperationException();
	}

	public void setNorthLat(final Latitude northLat) {
		throw new UnsupportedOperationException();
	}
}
