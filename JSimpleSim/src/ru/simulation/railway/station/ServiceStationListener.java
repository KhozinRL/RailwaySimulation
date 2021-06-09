package ru.simulation.railway.station;

import org.simplesim.core.notification.Listener;
import ru.simulation.railway.CurrentTimeListener;

public class ServiceStationListener implements Listener<ServiceStationState> {
    @Override
    public void notifyListener(ServiceStationState source) {
        source.getStatistics().updateStatistics(CurrentTimeListener.getCurrentTime());
    }
}
