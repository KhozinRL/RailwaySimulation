package ru.simulation.railway;

import org.simplesim.core.scheduling.Time;

public class TimeConstants {
    public static final Time START_SIMULATION = new Time(0,0,0,0,0,0);
    public static Time END_SIMULATION;
    public static final Time IDLE_TIME=new Time(30*Time.TICKS_PER_SECOND); // time to check for requests when idle
    public static final Time UPDATE_TIME = new Time(0,0,0,0,1,0);

    public static void setEndSimulation(Time time){
        if (END_SIMULATION == null)
            END_SIMULATION = time;
    }
}
