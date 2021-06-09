package ru.simulation.railway.train;

import org.simplesim.core.notification.Listener;

public class TrainListener implements Listener<TrainState> {

    @Override
    public void notifyListener(TrainState state) {
        try{
            TrainsStatistics.processTrain(state);
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
