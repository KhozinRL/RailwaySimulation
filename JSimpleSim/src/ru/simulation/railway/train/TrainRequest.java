package ru.simulation.railway.train;

import org.simplesim.core.scheduling.Time;

public class TrainRequest implements Comparable<TrainRequest>{

    Train train;
    Time requestTime;

    public TrainRequest(Train v, Time time){
        train = v;
        requestTime = time;
    }

    public Train getTrain(){
        return train;
    }
    public void setServiceStartTime(Time serviceStartTime) {
        /*if (train.getState().getActivity() == TrainState.Activity.waitingForFirstService){
            train.getState().setStartOfFirstService(serviceStartTime);
        } else if (train.getState().getActivity() == TrainState.Activity.waitingForSecondService){
            train.getState().setStartOfSecondService(serviceStartTime);
        }*/
        train.getState().setServiceStartTime(serviceStartTime);

    }

    public void setServiceEndTime(Time serviceEndTime) {
        /*if (train.getState().getActivity() == TrainState.Activity.waitingForFirstService){
            train.getState().setEndOfFirstService(serviceEndTime);
        } else if (train.getState().getActivity() == TrainState.Activity.waitingForSecondService){
            train.getState().setEndOfSecondService(serviceEndTime);
        }*/

        train.getState().setServiceEndTime(serviceEndTime);
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
        sb.append("\nTime of injection: " + train.getState().getTimeOfInjection());
        sb.append("\nRequest time: " + requestTime);
        sb.append("\nStart first service time: " + trainState.getStartOfFirstService());
        sb.append("\nEnd first service time: " + trainState.getEndOfFirstService());
        sb.append("\nStart second service time: " + trainState.getStartOfSecondService());
        sb.append("\nEnd second service time: " + trainState.getEndOfSecondService());

        return sb.toString();
    }
}
