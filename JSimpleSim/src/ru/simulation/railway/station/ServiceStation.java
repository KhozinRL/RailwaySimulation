package ru.simulation.railway.station;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.simplesim.core.messaging.RoutedMessage;
import org.simplesim.core.scheduling.Time;
import org.simplesim.model.RoutingAgent;
import ru.simulation.railway.TimeConstants;
import ru.simulation.railway.train.TrainRequest;
import java.util.Collections;
import java.util.LinkedList;

public class ServiceStation extends RoutingAgent<ServiceStationState, ServiceStation.Event> {

    public enum Event{
        READY, REMOVE_TRAIN
    }

    public static int next_id = 1; //id counter
    private final int id; //Identifier of service station
    private final ServiceStationState state = getState();
    private final AbstractRealDistribution distribution;

    public ServiceStation(AbstractRealDistribution distribution, int capacity){
        super(new ServiceStationState(capacity));
        state.setStation(this);
        this.distribution = distribution;
        id = next_id;
        next_id++;
        getEventQueue().enqueue(Event.READY, TimeConstants.START_SIMULATION);
    }

    @Override
    protected Time doEvent(Time time) {
        switch (getEventQueue().dequeue()) {
            case READY -> {
                processMessages();
                processRequestsQueue(time);
            }
            case REMOVE_TRAIN -> removeTrain(time);
        }

        return getTimeOfNextEvent();
    }

    private void processRequestsQueue(Time time){
        if (!state.getRequestQueue().isEmpty()) {
            processRequest(time);
        }
        else
            getEventQueue().enqueue(Event.READY, time.add(TimeConstants.IDLE_TIME));
    }

    public void processMessages(){
        LinkedList<TrainRequest> queue = state.getRequestQueue();

        while (getInport().hasMessages()){
            TrainRequest request = getInport().poll().getContent();
            queue.addFirst(request);
            getState().getStatistics().addToWaitingRequests(request);
        }
        Collections.sort(queue);
    }

    private TrainRequest pollRequest(){
        TrainRequest request = state.getRequestQueue().poll();

        state.getRequestsInProgress().add(request);
        state.incrementCurrentNumber();
        state.getStatistics().addToServed(request);
        state.getStatistics().allRequests.add(request);
        return request;
    }

    private Time updateRequest(TrainRequest request, Time time){
        request.setServiceStartTime(time);
        Time endTime = time.add(calculateServiceTime());
        request.setServiceEndTime(endTime);
        return endTime;
    }


    public void processRequest(Time time){
        if (state.getCurrentNumber() < state.getCapacity()) {
            TrainRequest request = pollRequest();
            Time endTime = updateRequest(request, time);
            sendReply(request);
            getEventQueue().enqueue(Event.READY, time);
            getEventQueue().enqueue(Event.REMOVE_TRAIN, endTime);
        }
    }

    public void removeTrain(Time time){
        TrainRequest request = state.getRequestsInProgress().remove();
        state.decrementCurrentNumber();
        getEventQueue().enqueue(Event.READY, time);
    }

    public Time calculateServiceTime(){
        double sample = distribution.sample();
        long ticks = (long)(Time.TICKS_PER_MINUTE * sample);
        return new Time(ticks);
    }

    public void sendReply(TrainRequest reply) {
        final RoutedMessage msg=new RoutedMessage(this.getAddress(), reply.getTrain().getAddress(),reply);
        getOutport().write(msg);
    }

    @Override
    public String getName() {
        return String.format("Service station №%d ", id);
    }
}
