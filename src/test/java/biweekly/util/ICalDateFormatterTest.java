package biweekly.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import biweekly.util.ICalDateFormatter;
import biweekly.util.ISOFormat;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * @author Michael Angstadt
 */
public class ICalDateFormatterTest {
	@Test
	public void format() {
		TimeZone tz = TimeZone.getTimeZone("Asia/Beirut");
		Calendar cal = Calendar.getInstance(tz);
		cal.clear();
		cal.set(Calendar.YEAR, 2006);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 20);
		cal.set(Calendar.SECOND, 30);
		Date date = cal.getTime();

		assertEquals("20060102", ICalDateFormatter.format(date, ISOFormat.DATE_BASIC, tz));
		assertEquals("2006-01-02", ICalDateFormatter.format(date, ISOFormat.DATE_EXTENDED, tz));
		assertEquals("20060102T102030+0200", ICalDateFormatter.format(date, ISOFormat.TIME_BASIC, tz));
		assertEquals("2006-01-02T10:20:30+02:00", ICalDateFormatter.format(date, ISOFormat.TIME_EXTENDED, tz));
		assertEquals("2006-01-02T10:20:30+0200", ICalDateFormatter.format(date, ISOFormat.HCARD_TIME_TAG, tz));
		assertEquals("20060102T082030Z", ICalDateFormatter.format(date, ISOFormat.UTC_TIME_BASIC, tz));
		assertEquals("2006-01-02T08:20:30Z", ICalDateFormatter.format(date, ISOFormat.UTC_TIME_EXTENDED, tz));
	}

	@Test
	public void formatTimeZone() {
		//positive
		TimeZone tz = TimeZone.getTimeZone("Asia/Beirut");

		String expected = "+0200";
		String actual = ICalDateFormatter.formatTimeZone(tz, false);
		assertEquals(expected, actual);

		expected = "+02:00";
		actual = ICalDateFormatter.formatTimeZone(tz, true);
		assertEquals(expected, actual);

		//negative
		tz = TimeZone.getTimeZone("America/New_York");

		expected = "-0500";
		actual = ICalDateFormatter.formatTimeZone(tz, false);
		assertEquals(expected, actual);

		expected = "-05:00";
		actual = ICalDateFormatter.formatTimeZone(tz, true);
		assertEquals(expected, actual);

		//with minutes
		tz = TimeZone.getTimeZone("America/New_York");
		tz.setRawOffset(tz.getRawOffset() - 30 * 60 * 1000);

		expected = "-0530";
		actual = ICalDateFormatter.formatTimeZone(tz, false);
		assertEquals(expected, actual);

		expected = "-05:30";
		actual = ICalDateFormatter.formatTimeZone(tz, true);
		assertEquals(expected, actual);

		//>= 10
		tz = TimeZone.getTimeZone("Australia/Sydney");

		expected = "+1000";
		actual = ICalDateFormatter.formatTimeZone(tz, false);
		assertEquals(expected, actual);

		expected = "+10:00";
		actual = ICalDateFormatter.formatTimeZone(tz, true);
		assertEquals(expected, actual);

		//zero
		tz = TimeZone.getTimeZone("UTC");

		expected = "+0000";
		actual = ICalDateFormatter.formatTimeZone(tz, false);
		assertEquals(expected, actual);

		expected = "+00:00";
		actual = ICalDateFormatter.formatTimeZone(tz, true);
		assertEquals(expected, actual);
	}

	@Test
	public void parse() throws Exception {
		Calendar c;
		Date expected, actual;

		//test date
		c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, 2012);
		c.set(Calendar.MONTH, Calendar.JULY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		expected = c.getTime();
		actual = ICalDateFormatter.parse("20120701");
		assertEquals(expected, actual);

		actual = ICalDateFormatter.parse("2012-07-01");
		assertEquals(expected, actual);

		//test date-time
		c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.clear();
		c.set(Calendar.YEAR, 2012);
		c.set(Calendar.MONTH, Calendar.JULY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 8);
		c.set(Calendar.MINUTE, 1);
		c.set(Calendar.SECOND, 30);
		expected = c.getTime();
		actual = ICalDateFormatter.parse("20120701T080130Z");
		assertEquals(expected, actual);

		actual = ICalDateFormatter.parse("2012-07-01T08:01:30Z");
		assertEquals(expected, actual);

		actual = ICalDateFormatter.parse("2012-07-01T11:01:30+03:00");
		assertEquals(expected, actual);

		actual = ICalDateFormatter.parse("2012-07-01T11:01:30+0300");
		assertEquals(expected, actual);
	}

	@Test
	public void parseTimeZone() {
		int[] expected, actual;

		expected = ICalDateFormatter.parseTimeZone("5");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("10");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+5");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+10");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-5");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-10");
		actual = new int[] { -10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("05");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+05");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-05");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("500");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+500");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-500");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("530");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+530");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-530");
		actual = new int[] { -5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("5:00");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("10:00");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+5:00");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+10:00");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-5:00");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-10:00");
		actual = new int[] { -10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("5:30");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("10:30");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+5:30");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+10:30");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-5:30");
		actual = new int[] { -5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-10:30");
		actual = new int[] { -10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("0500");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("1000");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+0500");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+1000");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-0500");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-1000");
		actual = new int[] { -10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("0530");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("1030");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+0530");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+1030");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-0530");
		actual = new int[] { -5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-1030");
		actual = new int[] { -10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("05:00");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("10:00");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+05:00");
		actual = new int[] { 5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+10:00");
		actual = new int[] { 10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-05:00");
		actual = new int[] { -5, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-10:00");
		actual = new int[] { -10, 0 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("05:30");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("10:30");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+05:30");
		actual = new int[] { 5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("+10:30");
		actual = new int[] { 10, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-05:30");
		actual = new int[] { -5, 30 };
		assertArrayEquals(expected, actual);

		expected = ICalDateFormatter.parseTimeZone("-10:30");
		actual = new int[] { -10, 30 };
		assertArrayEquals(expected, actual);
	}
}