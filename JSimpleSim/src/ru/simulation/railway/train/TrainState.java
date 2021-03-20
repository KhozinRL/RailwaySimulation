package ru.simulation.railway.train;

import org.simplesim.model.State;

public class TrainState implements State {
    public enum Activity {
        waiting, inProcess;
    }

    private Activity activity = Activity.waiting;

    public Activity getActivity(){
        return activity;
    }

    public void setActivity(Activity value){
        activity = value;
    }
}
