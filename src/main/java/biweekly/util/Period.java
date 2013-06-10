package biweekly.util;

import java.util.Date;

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
 * A period of time.
 * @author Michael Angstadt
 */
public class Period {
	private final Date startDate;
	private final Date endDate;
	private final Duration duration;

	/**
	 * Creates a new time period.
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public Period(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		duration = null;
	}

	/**
	 * Creates a new time period.
	 * @param startDate the start date
	 * @param duration the length of time after the start date
	 */
	public Period(Date startDate, Duration duration) {
		this.startDate = startDate;
		this.duration = duration;
		endDate = null;
	}

	/**
	 * Copies an existing time period.
	 * @param period the period to copy
	 */
	public Period(Period period) {
		this.startDate = period.startDate;
		this.endDate = period.endDate;
		this.duration = period.duration;
	}

	/**
	 * Gets the start date.
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Gets the end date. This will be null if a duration was defined.
	 * @return the end date or null if not set
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Gets the length of time after the start date. This will be null if an end
	 * date was defined.
	 * @return the duration or null if not set
	 */
	public Duration getDuration() {
		return duration;
	}
}
