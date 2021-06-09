package ru.simulation.railway.train;

import org.simplesim.core.scheduling.Time;
import org.simplesim.model.State;
import ru.simulation.railway.train.TrainListener;

public class TrainState implements State {

    public enum Activity {
        waitingForTheFirstService, waitingForTheSecondService, idle;
    }

    static TrainListener listener = new TrainListener();

    private Integer directionId;
    private Activity activity;
    private Time timeOfInjection;
    private Time startOfFirstService;
    private Time startOfSecondService;
    private Time endOfFirstService;
    private Time endOfSecondService;



    public Activity getActivity(){
        return activity;
    }

    public void setActivity(Activity value) {
        activity = value;
        if (value == Activity.idle)
            listener.notifyListener(this);
    }

    public void setTimeOfInjection(Time timeOfInjection) {
        if (this.timeOfInjection == null)
            this.timeOfInjection = timeOfInjection;
    }

    public void setServiceStartTime(Time time){
        switch (activity) {
            case waitingForTheFirstService -> startOfFirstService = time;
            case waitingForTheSecondService -> startOfSecondService = time;
        }
    }

    public void setServiceEndTime(Time time){
        switch (activity) {
            case waitingForTheFirstService -> endOfFirstService = time;
            case waitingForTheSecondService -> endOfSecondService = time;
        }
    }

    public void setDirectionId(int id){
        if (directionId == null)
            directionId = id;
    }

    public int getDirectionId(){
        return directionId;
    }

    /*public void setStartOfFirstService(Time startOfFirstService) {
        if (this.startOfFirstService == null)
            this.startOfFirstService = startOfFirstService;
    }

    public void setStartOfSecondService(Time startOfSecondService) {
        if (this.startOfSecondService == null)
            this.startOfSecondService = startOfSecondService;
    }

    public void setEndOfFirstService(Time endOfFirstService) {
        if (this.endOfFirstService == null)
            this.endOfFirstService = endOfFirstService;
    }

    public void setEndOfSecondService(Time endOfSecondService) {
        if (this.endOfSecondService == null)
            this.endOfSecondService = endOfSecondService;
    }*/

    public Time getStartOfFirstService() {
        return startOfFirstService;
    }

    public Time getEndOfFirstService() {
        return endOfFirstService;
    }

    public Time getStartOfSecondService() {
        return startOfSecondService;
    }

    public Time getEndOfSecondService() {
        return endOfSecondService;
    }

    public Time getTimeOfInjection() {
        return timeOfInjection;
    }
}
