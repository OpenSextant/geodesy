package org.mitre.itf.geodesy.test;

import junit.framework.TestCase;
import org.mitre.itf.geodesy.SafeDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class TestSimpleDate extends TestCase {

	private volatile boolean running = true;
	//private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private final SafeDateFormat df = new SafeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private Thread current;
	private final Random rand = new Random();

	public void testThreads() {
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		current = Thread.currentThread();
		// System.out.format("now = %x%n", System.currentTimeMillis());
		for (int i=0; i < 30; i++) {
			Thread t = new Thread(new Runner(i));
			t.setDaemon(true);
			t.start();
		}

		// run thread tests for 10 seconds
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		// System.out.println("running = " + running);
		assertTrue("format returned non-consistent result", running);
		running = false;
	}

	private class Runner implements Runnable {

		private int id;

		public Runner(int id) {
			this.id = id;
		}

		public void run() {
			long ts = rand.nextLong() & 0x14FFFFFFFFFL;
			Date date = new Date(ts);
			String output;
			synchronized (df) {
				output = df.format(date);
			}
			int count = 0;
			while (running) {
				count++;
				String result = df.format(date);
				if (!output.equals(result)) {
					System.out.format("T%d:%d Failed actual=%s expected=%s%n", id, count, result, output);
					running = false;
					current.interrupt();
				}
				try {
					Date parsedDate = df.parse(output);
					if (!date.equals(parsedDate)) {
						System.out.format("T%d:%d Failed to parse date: actual=%s expected=%s%n", id, count, date, parsedDate );
						running = false;
						current.interrupt();
					}
				} catch (ParseException e) {
					System.out.format("T%d:%d Failed to parse date: %s%n", id, count, e.toString() );
					running = false;
					current.interrupt();
				}
			}
		}
	}

}
