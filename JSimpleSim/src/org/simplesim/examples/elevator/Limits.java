/*
 * JSimpleSim is a framework to build multi-agent systems in a quick and easy way. This software is published as open
 * source and licensed under the terms of GNU GPLv3.
 * 
 * Contributors: - Rene Kuhlemann - development and initial implementation
 */
package org.simplesim.examples.elevator;

import org.simplesim.core.scheduling.Time;

/**
 * 
 *
 */
public final class Limits {
	
	public final static int LOBBY=0;
	public final static int MAX_FLOOR=13;
	public static final int CAPACITY=16; // maximum passenger capacity
	public static final int VISITORS=600;
	
	public final static Time START_DAY=new Time(7*Time.TICKS_PER_HOUR);
	public final static Time START_WORK=new Time(9*Time.TICKS_PER_HOUR);
	public final static Time END_WORK=new Time(17*Time.TICKS_PER_HOUR);
	public final static Time END_DAY=new Time(20*Time.TICKS_PER_HOUR);
		
	public final static int SPEED=2*Time.TICKS_PER_SECOND; // travel time to get from one floor to the next one
	public final static int DOOR_TIME=2*3*Time.TICKS_PER_SECOND; // time to open AND close the doors
	public final static int CHANGE_TIME=2*Time.TICKS_PER_SECOND; // time per visitor entering OR leaving the elevator
	public static final int MAX_STAY_TIME=3*Time.TICKS_PER_HOUR; // max. stay on a floor
	
	public static final Time IDLE_TIME=new Time(30*Time.TICKS_PER_SECOND); // time to check for requests when idle
	
}
