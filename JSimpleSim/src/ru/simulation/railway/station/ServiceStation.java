package ru.simulation.railway.station;

import org.simplesim.core.messaging.RoutedMessage;
import org.simplesim.core.scheduling.Time;
import org.simplesim.model.RoutingAgent;
import ru.simulation.railway.TimeConstants;
import ru.simulation.railway.train.TrainRequest;

import java.util.Collections;
import java.util.LinkedList;

public class ServiceStation extends RoutingAgent<ServiceStationState, ServiceStation.Event> {

    public enum Event{
        READY, REMOVE_TRAIN;
    }

    private static int next_id = 1; //id counter
    private final int id; //Identifier of service station
    private final ServiceStationState state = getState();

    public ServiceStation(int capacity){
        super(new ServiceStationState(capacity));
        state.setStation(this);
        id = next_id;
        next_id++;
        getEventQueue().enqueue(Event.READY, TimeConstants.START_SIMULATION);
    }

    @Override
    protected Time doEvent(Time time) {
        //System.out.println("Current number: " + current_number);
        switch (getEventQueue().dequeue()){
            case READY:
                processMessages();
                processRequestsQueue(time);
                break;
            case REMOVE_TRAIN:
                removeTrain(time);
                break;
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
            queue.addFirst(getInport().poll().getContent());
        }
        Collections.sort(queue);
        //System.out.println("Queue len: " + queue.size());
    }


    public void processRequest(Time time){
        //System.out.println("Got a new service request at " + time);
        if (state.getCurrentNumber() < state.getCapacity()) {
            TrainRequest request = state.getRequestQueue().poll();
            state.getRequestsInProgress().add(request);
            state.incrementCurrentNumber();
            //System.out.println("Current number: " + current_number);
            request.setServiceStartTime(time);
            Time endTime = time.add(calcServiceTime());
            request.setServiceEndTime(endTime);
            sendReply(request);
            getEventQueue().enqueue(Event.READY, time);
            getEventQueue().enqueue(Event.REMOVE_TRAIN, endTime);
        }
    }

    public void removeTrain(Time time){
        //System.out.println(requestsInProgress.peek().train.trainID + "removed at" + time);
        TrainRequest request = state.getRequestsInProgress().remove();
        state.decrementCurrentNumber();
        //System.out.println("Current number: " + current_number);
        getEventQueue().enqueue(Event.READY, time);
    }

    public Time calcServiceTime(){
        return new Time(0,0,0,3,0,0);
    }

    public void sendReply(TrainRequest reply) {
        //final Request request=new Request(this,,destination,time);
        final RoutedMessage msg=new RoutedMessage(this.getAddress(), reply.getTrain().getAddress(),reply);
        getOutport().write(msg);
    }

    @Override
    public String getName() {
        return String.format("Service station №%d", id);
    }
}
