/*
 * JSimpleSim is a framework to build multi-agent systems in a quick and easy way. This software is published as open
 * source and licensed under the terms of GNU GPLv3. Contributors: - Rene Kuhlemann - development and initial
 * implementation
 */
package org.simplesim.core.scheduling;

/**
 * Provides time and calendar functionality to scale and navigate the simulation's time axis.
 * <p>
 * Ticks are stored as {@code long} primitive. A time scale is provided, standardized with one second set to one. The
 * scale may be changed because within the simulator, only the {@code Time} wrapper is used.
 * <p>
 * This class is immutable and thus tread safe.
 */
public final class Time implements Comparable<Time> {

	// basic coords of the time system
	public static final int SECONDS_PER_MINUTE=60;
	public static final int MINUTES_PER_HOUR=60;
	public static final int HOURS_PER_DAY=24;
	public static final int DAYS_PER_WEEK=7;
	public static final int DAYS_PER_MONTH=30;
	public static final int MONTHS_PER_YEAR=12;
	public static final int DAYS_PER_YEAR=DAYS_PER_MONTH*MONTHS_PER_YEAR; // =360

	// as a result, duration of hours, days, months...
	public static final int TICKS_PER_SECOND=1;
	public static final int TICKS_PER_MINUTE=TICKS_PER_SECOND*SECONDS_PER_MINUTE;
	public static final int TICKS_PER_HOUR=MINUTES_PER_HOUR*TICKS_PER_MINUTE;
	public static final int TICKS_PER_DAY=HOURS_PER_DAY*TICKS_PER_HOUR;
	public static final int TICKS_PER_WEEK=DAYS_PER_WEEK*TICKS_PER_DAY;
	public static final int TICKS_PER_MONTH=DAYS_PER_MONTH*TICKS_PER_DAY;
	public static final int TICKS_PER_YEAR=MONTHS_PER_YEAR*TICKS_PER_MONTH;

	public static final Time ZERO=new Time(0);
	public static final Time SECOND=new Time(TICKS_PER_SECOND);
	public static final Time MINUTE=new Time(TICKS_PER_MINUTE);
	public static final Time HOUR=new Time(TICKS_PER_HOUR);
	public static final Time DAY=new Time(TICKS_PER_DAY);
	public static final Time MONTH=new Time(TICKS_PER_MONTH);
	public static final Time YEAR=new Time(TICKS_PER_YEAR);
	public static final Time INFINITY=new Time(Long.MAX_VALUE);

	/**
	 * This is an immutable class, so ticks are final (multiple references to a changing time instance are prone to hard
	 * to find bugs)
	 */
	private final long ticks;

	public Time(long value) {
		ticks=value;
	}

	// copy constructor
	public Time(Time time) {
		this(time.ticks);
	}

	public Time(int year, int month, int day, int hour, int min, int sec) {
		this((year*TICKS_PER_YEAR)+(month*TICKS_PER_MONTH)+(day*TICKS_PER_DAY)+(hour*TICKS_PER_HOUR)
				+(min*TICKS_PER_MINUTE)+(sec*TICKS_PER_SECOND));
	}

	public long getTicks() {
		return ticks;
	}

	public Time add(long value) {
		return new Time(getTicks()+value);
	}

	public Time add(Time time) {
		return add(time.getTicks());
	}

	public Time sub(long value) {
		return new Time(getTicks()-value);
	}

	public Time sub(Time time) {
		return sub(time.getTicks());
	}

	public int seconds() {
		return (int) ((getTicks()%TICKS_PER_MINUTE)/TICKS_PER_SECOND);
	}

	public int minutes() {
		return (int) ((getTicks()%TICKS_PER_HOUR)/TICKS_PER_MINUTE);
	}

	public int hours() {
		return (int) ((getTicks()%TICKS_PER_DAY)/TICKS_PER_HOUR);
	}

	public int days() {
		return (int) ((getTicks()%TICKS_PER_MONTH)/TICKS_PER_DAY);
	}

	public int months() {
		return (int) ((getTicks()%TICKS_PER_YEAR)/TICKS_PER_MONTH);
	}

	public int years() {
		return (int) (getTicks()/TICKS_PER_YEAR);
	}

	@Override
	public String toString() {
		return String.format("[%02d.%02d.%02d %02d:%02d.%02d]",years(),months(),days(),hours(),minutes(),seconds());
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj==null)||!(obj instanceof Time)) return false;
		return this.getTicks()==((Time) obj).getTicks();
	}

	@Override
	public int hashCode() {
		return (int) getTicks();
	}

	@Override
	public int compareTo(Time other) {
		if (this.getTicks()<other.getTicks()) return -1;
		else if (this.getTicks()>other.getTicks()) return 1;
		else return 0;
	}

	public static double TicksToMinutes(long ticks){
		return Math.round(ticks*1.0/60 * 100) / 100.0;
	}

}
