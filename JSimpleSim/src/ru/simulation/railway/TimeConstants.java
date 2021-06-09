package ru.simulation.railway;

import org.simplesim.core.scheduling.Time;

public class TimeConstants {
    public static final Time START_SIMULATION = new Time(0,0,0,0,0,0);
    public static final Time END_SIMULATION = new Time(0,0,3,0,0,0);
    public static final Time IDLE_TIME=new Time(30*Time.TICKS_PER_SECOND); // time to check for requests when idle

}
