package ru.simulation.railway.station;

import org.simplesim.core.scheduling.Time;

public class ServiceStationStatistics {

    ServiceStationState state;
    Time totalWorkingTime = Time.ZERO;
    private Time lastChangeTime;
    private long duration;
    private double sum = 0;
    private int lastNum; //last number of trains at the Service Station
    private double utilization;

    public ServiceStationStatistics(ServiceStationState state){
        this.state = state;
    }

    public void updateStatistics(Time time){
        totalWorkingTime = time;

        if (lastChangeTime == null){
            lastChangeTime = time;
            lastNum = state.getCurrentNumber();
            return;
        }

        duration = totalWorkingTime.getTicks() - lastChangeTime.getTicks();
        //Time dur = new Time(duration);
        sum += duration*(lastNum*1.0/state.getCapacity());
        utilization = sum/totalWorkingTime.getTicks()*100;
        lastChangeTime = totalWorkingTime;
        lastNum = state.getCurrentNumber();
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n");
        sb.append(state.getStation().getName());
        sb.append("\nTotal time: " + totalWorkingTime);
        sb.append(String.format("\nUtilization: %.2f %%",  utilization));

        return sb.toString();
    }
}
