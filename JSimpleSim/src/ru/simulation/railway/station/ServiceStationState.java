package ru.simulation.railway.station;

import org.simplesim.model.State;
import ru.simulation.railway.train.TrainRequest;
import java.util.LinkedList;
import java.util.Queue;

public class ServiceStationState implements State {

    private final int capacity;
    private int currentNumber = 0; //Number of trains at the moment
    private int totalServedNumber = 0;
    private final Queue<TrainRequest> requestsInProgress = new LinkedList<>();
    private final LinkedList<TrainRequest> requestQueue = new LinkedList<>();
    private ServiceStation station;
    private final ServiceStationStatistics statistics = new ServiceStationStatistics(this);

    public ServiceStationState(int capacity){
        this.capacity = capacity;
    }

    public void setStation(ServiceStation station){
        if (this.station == null){
            this.station = station;
        }
    }

    public int getCurrentNumber(){
        return currentNumber;
    }

    public void incrementCurrentNumber(){
        currentNumber++;
    }

    public void decrementCurrentNumber(){
        currentNumber--;
        totalServedNumber++;
    }

    public Queue<TrainRequest> getRequestsInProgress(){
        return requestsInProgress;
    }

    public LinkedList<TrainRequest> getRequestQueue(){
        return requestQueue;
    }

    public int getQueueLength(){
        return requestQueue.size();
    }

    public int getNumberInProcess(){
        return requestsInProgress.size();
    }

    public int getTotalServedNumber(){
        return totalServedNumber;
    }

    public ServiceStationStatistics getStatistics(){
        return statistics;
    }

    public int getCapacity(){
        return capacity;
    }

}
