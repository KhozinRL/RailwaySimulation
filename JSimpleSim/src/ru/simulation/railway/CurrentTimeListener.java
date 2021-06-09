package ru.simulation.railway;

import org.simplesim.core.notification.Listener;
import org.simplesim.core.scheduling.Time;
import org.simplesim.simulator.AbstractSimulator;

//Singleton
public class CurrentTimeListener implements Listener<AbstractSimulator> {

    private static Time currentTime;
    private static CurrentTimeListener currentTimeListener = new CurrentTimeListener();

    private CurrentTimeListener(){}

    @Override
    public void notifyListener(AbstractSimulator source) {
        currentTime = source.getSimulationTime();
    }

    static public Time getCurrentTime(){
        return currentTime;
    }

    public static CurrentTimeListener getCurrentTimeListener() {
        return currentTimeListener;
    }
}
