package ru.simulation.railway.train;

import org.simplesim.core.scheduling.Time;
import org.simplesim.model.RoutingAgent;

public class Train extends RoutingAgent<TrainState, Train.Event> {

    public enum Event{
        REQUEST_SERVICE, SERVED1, SERVED2;
    }

    public Train(Time time){
        super(new TrainState());
        getEventQueue().enqueue(Event.REQUEST_SERVICE, time);
    }

    @Override
    protected Time doEvent(Time time) {
        return null;
    }
}
