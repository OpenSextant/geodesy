/************************************************************************************
 * ISO8601DateTimePoint.java 12/22/10 10:16 AM psilvey
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
package org.mitre.itf.geodesy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The ISO8601DateTimePoint class is a simple wrapper for a time instant standardized
 * as a number of milliseconds since midnight Jan 1, 1970 GMT. This class has a
 * String constructor and toString method that respectively parse and format dates
 * and times in the ISO 8601 format. Partial dates are parsed to the beginning of the
 * time interval they define.
 */
public class ISO8601DateTimePoint implements Comparable<ISO8601DateTimePoint> {
    protected static final String INVALID_POINT = "invalid time point";

    protected final static TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
    protected final static String dtSuffix = "-01-01T00:00:00.000GMT-00:00";
    protected final static SimpleDateFormat DF;

    static {
        DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        DF.setTimeZone(UTC_TIMEZONE);
    }

    protected long startTime;

    /**
     * Constructor using the current local time for start time
     */
    public ISO8601DateTimePoint() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Constructor using the specified start time
     *
     * @param startTime long start time for interval
     */
    public ISO8601DateTimePoint(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Constructor that takes an ISO Date Time String to be interpreted as an
     * instant in time (start of interval if partial date time specified).
     *
     * @param isoDateTimeStr String containing an ISO 8601 Date Time point or interval
     * @throws IllegalArgumentException if a parsing error occurs
     */
    public ISO8601DateTimePoint(String isoDateTimeStr) {
        try {
            String toParse = isoDateTimeStr;
            if (toParse.endsWith("Z"))
                toParse = toParse.substring(0, toParse.length() - 1);
            int eoy = toParse.indexOf("-");
            if (eoy < 0) eoy = toParse.length();
            int n = toParse.length();
            toParse = toParse.replace(" ", "T");
            toParse += dtSuffix.substring(n - eoy);
            this.startTime = DF.parse(toParse).getTime();
            // Final validation that yyyy-MM-dd is a valid day (round trip test)
            String ymdInput = toParse.substring(0, toParse.indexOf("T"));
            String ymdOutput = this.toString();
            ymdOutput = ymdOutput.substring(0, ymdOutput.indexOf("T"));
            if (!ymdInput.equals(ymdOutput)) throw new Exception();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid ISO 8601 date and time, " +
                    isoDateTimeStr);
        }
    }

    /**
     * Getter method for this point's start time (uses Long wrapper class to allow
     * extensions to return null if time is undefined)
     *
     * @return Long start time in milliseconds
     */
    public Long getStartTime() {
        return this.startTime;
    }

    /**
     * Setter method for this point's start time
     *
     * @param startTime long start time
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ISO8601DateTimePoint that = (ISO8601DateTimePoint) o;
        return this.startTime == that.startTime;
    }

    @Override
    public int hashCode() {
        return (int) (this.startTime ^ (this.startTime >>> 32));
    }

    /**
     * Returns the value 0 if this ISO8601DateTimePoint is equal to the argument
     * ISO8601DateTimePoint; a value less than 0 if this ISO8601DateTimePoint
     * starts before the argument ISO8601DateTimePoint; and a value greater
     * than 0 if this ISO8601DateTimePoint starts after the argument
     * ISO8601DateTimePoint (signed comparison of long start times).
     *
     * @param that ISO8601DateTimePoint to compare to this ISO8601DateTimePoint
     * @return 0, -1, or +1 depending if this point is <, ==, or > that
     */
    @Override
    public int compareTo(ISO8601DateTimePoint that) {
        return (this.startTime == that.startTime) ?
                0 : (this.startTime < that.startTime) ? -1 : + 1;
    }

    /**
     * Format this TmPoint object using the ISO 8601 point syntax, which can
     * be parsed using the String argument constructor method of this class.
     *
     * @return String ISO 8601 Date Time point syntax for this TmPoint
     */
    @Override
    public String toString() {
        return DF.format(new Date(this.startTime)).replace("UTC", "Z");
    }
}
