/************************************************************************************
 * TestISO8601DateTime.java 12/22/10 11:08 AM psilvey
 *
 * (C) Copyright psilvey 2010
 *
 *
 * The program is provided "as is" without any warranty express or implied, including
 * the warranty of non-infringement and the implied warranties of merchantability and
 * fitness for a particular purpose.  The Copyright owner will not be liable for any
 * damages suffered by you as a result of using the Program. In no event will the
 * Copyright owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the possibility of
 * their occurrence.
 *
 ***********************************************************************************/
package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.mitre.itf.geodesy.ISO8601DateTimeInterval;
import org.mitre.itf.geodesy.ISO8601DateTimePoint;

import java.util.Random;

public class TestISO8601DateTime extends TestCase {
    private final static Random rand = new Random();

    private final static String[] partials = {
                "1970",
                "1970-01",
                "1970-01-01",
                "1970-01-01T00",
                "1970-01-01T00:00",
                "1970-01-01T00:00:00",
                "1970-01-01T00:00:00.000",
                "1970-01-01T00:00:00.000Z"
        };

    @Test
    public static void testTimePoint() throws Exception {
        ISO8601DateTimePoint t1;
        ISO8601DateTimePoint t2;

        for (int trials = 0; trials < 1000; trials++) {
            // Create a random point in time (within about 8000 years of the Epoch)
            long rTime = (long) (100.0 * 365.25 * 24.0 *
                    60.0 * 60.0 * 1000.0 * rand.nextDouble());
            t1 = new ISO8601DateTimePoint(rTime);
            String isoDateTime = t1.toString();
            t2 = new ISO8601DateTimePoint(isoDateTime);
            assertEquals(t1, t2);
            assertEquals(isoDateTime, t2.toString());
        }

        ISO8601DateTimePoint lastTime = null;
        for (String isoStr : partials) {
            t1 = new ISO8601DateTimePoint(isoStr);
            // all times should be equivalent
            if (lastTime != null) assertEquals(lastTime, t1);
            lastTime = t1;
            System.out.println(isoStr + " means " + t1.toString());
        }
    }

    @Test
    public static void testTimeInterval() throws Exception {
        ISO8601DateTimeInterval t1;
        ISO8601DateTimeInterval t2;

        for (int trials = 0; trials < 1000; trials++) {
            // Create random points in time (within about 8000 years of the Epoch)
            long rt1 = (long) (100.0 * 365.25 * 24.0 *
                    60.0 * 60.0 * 1000.0 * rand.nextDouble());
            long rt2 = (long) (100.0 * 365.25 * 24.0 *
                    60.0 * 60.0 * 1000.0 * rand.nextDouble());
            // Make sure they are in the correct temporal order
            if (rt1 > rt2) {
                long temp = rt1;
                rt1 = rt2;
                rt2 = temp;
            }

            t1 = new ISO8601DateTimeInterval(rt1, rt2);
            String isoDateTime = t1.toString();
            t2 = new ISO8601DateTimeInterval(isoDateTime);
            assertEquals(t1, t2);
            assertEquals(isoDateTime, t2.toString());
        }

        for (String isoStr : partials) {
            t1 = new ISO8601DateTimeInterval(isoStr);
            System.out.println(isoStr + " means " + t1.toString());
        }
    }

    /**
     * Main method for running class tests.
     *
     * @param args standard command line arguments - ignored.
     */
    public static void main(String[] args) {
        JUnitCore.runClasses(TestISO8601DateTime.class);
    }
}
