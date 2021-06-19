package ru.simulation.railway.train;

import org.simplesim.core.scheduling.Time;

public class TrainRequest implements Comparable<TrainRequest>{

    Train train;
    public Time requestTime;
    public Time startTime;
    public Time endTime;

    public TrainRequest(Train v, Time time){
        train = v;
        requestTime = time;
    }

    public Train getTrain(){
        return train;
    }

    public void setServiceStartTime(Time serviceStartTime) {
        train.getState().setServiceStartTime(serviceStartTime);
        startTime = serviceStartTime;
    }

    public void setServiceEndTime(Time serviceEndTime) {
        train.getState().setServiceEndTime(serviceEndTime);
        endTime = serviceEndTime;
    }

    @Override
    public int compareTo(TrainRequest o) {
        return this.requestTime.compareTo(o.requestTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TrainState trainState = train.getState();
        sb.append(String.format("Train: %d", train.trainId));
        sb.append("\nTime of injection: ").append(train.getState().getTimeOfInjection());
        sb.append("\nRequest time: ").append(requestTime);
        sb.append("\nStart first service time: ").append(trainState.getStartOfFirstService());
        sb.append("\nEnd first service time: ").append(trainState.getEndOfFirstService());
        sb.append("\nStart second service time: ").append(trainState.getStartOfSecondService());
        sb.append("\nEnd second service time: ").append(trainState.getEndOfSecondService());

        return sb.toString();
    }
}
