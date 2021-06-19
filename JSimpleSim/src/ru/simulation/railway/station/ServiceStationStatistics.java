package ru.simulation.railway.station;

import org.simplesim.core.scheduling.Time;
import org.simplesim.model.RoutingAgent;
import ru.simulation.railway.TimeConstants;
import ru.simulation.railway.train.TrainRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.io.FileUtils;

public class ServiceStationStatistics extends RoutingAgent<ServiceStationState, ServiceStationStatistics.Event> {

    public enum Event {
        WRITE, FINALLY
    }

    ServiceStationState serviceStationState;

    Time totalTime = Time.ZERO;
    long totalDurationOfWork2 = 0L;
    long currentDurationOfWork = 0L;
    long currentWaitingDuration = 0L;
    long totalWaitingTime = 0L;
    private double utilization;

    static String dir;
    File file1, file2, file3;
    FileWriter writer;

    private final List<TrainRequest> requestsToProcess = new LinkedList<>();
    private final Queue<TrainRequest> waitingRequests = new LinkedList<>();
    public List<TrainRequest> allRequests = new ArrayList<>();

    static {
        dir = "./Results/StationsStatistics/";
        File directory = new File(dir);
        directory.mkdirs();
        try{
            FileUtils.cleanDirectory(directory);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ServiceStationStatistics(ServiceStationState state){
        super(null);
        this.serviceStationState = state;
        getEventQueue().enqueue(Event.WRITE, TimeConstants.UPDATE_TIME);
        getEventQueue().enqueue(Event.FINALLY, new Time(TimeConstants.END_SIMULATION.getTicks() - 1));
        prepareFiles();
    }

    @Override
    protected Time doEvent(Time time) {
        Event event = getEventQueue().dequeue();
        updateStatistics(time);
        getEventQueue().enqueue(Event.WRITE, time.add(TimeConstants.UPDATE_TIME));

        if (event == Event.FINALLY){
            long interval, waiting;
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();

            for(int k = 1; k < allRequests.size(); ++k){
                interval = allRequests.get(k).requestTime.getTicks() - allRequests.get(k-1).requestTime.getTicks();
                sb1.append(Time.TicksToMinutes(interval)).append("\n");
                waiting = allRequests.get(k).startTime.getTicks() - allRequests.get(k-1).requestTime.getTicks();
                sb2.append(Time.TicksToMinutes(waiting)).append("\n");
            }

            DistributionsToFile(file2, sb1.toString());
            DistributionsToFile(file3, sb2.toString());
        }
        return getTimeOfNextEvent();
    }

    public void updateStatistics(Time time){
        totalTime = time;
        long duration = 0L;
        LinkedList<TrainRequest> requestsToRemove = new LinkedList<>();

        for(TrainRequest request : requestsToProcess){
            if (request.endTime != null && request.endTime.getTicks() <= time.getTicks()){
                totalDurationOfWork2 += request.endTime.getTicks() - request.startTime.getTicks();
                requestsToRemove.add(request);
            }else {
                duration += time.getTicks() - request.startTime.getTicks();
            }
        }

        requestsToProcess.removeAll(requestsToRemove);
        currentDurationOfWork = totalDurationOfWork2 + duration;
        utilization = currentDurationOfWork*1.0/(serviceStationState.getCapacity() * totalTime.getTicks())*100;
        statisticsToFile();
    }

    public double getAvgWaitingTime(Time time){
        long duration = 0L;
        int totalSize = serviceStationState.getQueueLength() + serviceStationState.getNumberInProcess() + serviceStationState.getTotalServedNumber();

        LinkedList<TrainRequest> requestsToRemove = new LinkedList<>();

        if (totalSize == 0) return 0;

        for (TrainRequest request : waitingRequests){
            if(request.startTime != null){
                totalWaitingTime += request.startTime.getTicks() - request.requestTime.getTicks();
                requestsToRemove.add(request);
            }else{
                duration += time.getTicks() - request.requestTime.getTicks();
            }
        }
        currentWaitingDuration = totalWaitingTime + duration;
        waitingRequests.removeAll(requestsToRemove);
        long res = Math.round(currentWaitingDuration*1.0/totalSize);
        return Time.TicksToMinutes(res);
    }

    public double getAvgQueueLength(){
       return currentWaitingDuration*1.0/ totalTime.getTicks();
    }

    public void addToServed(TrainRequest request){
        requestsToProcess.add(request);
    }

    public void addToWaitingRequests(TrainRequest request){
        waitingRequests.add(request);
    }

    public void statisticsToFile(){
        try
        {
            writer.append(String.valueOf(Time.TicksToMinutes(totalTime.getTicks()))).append("\t");
            writer.append(String.valueOf(Time.TicksToMinutes(currentDurationOfWork))).append("\t");
            writer.append(String.valueOf(getAvgWaitingTime(totalTime))).append("\t");
            writer.append(String.valueOf(serviceStationState.getTotalServedNumber())).append("\t");
            writer.append(String.valueOf(Math.round(utilization * 100) / 100.0)).append("\t");
            writer.append(String.valueOf(serviceStationState.getCurrentNumber())).append("\t");
            writer.append(String.valueOf(serviceStationState.getQueueLength())).append("\t");
            writer.append(String.valueOf(getAvgQueueLength())).append("\t\n");
            writer.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void DistributionsToFile(File file, String str){
        try (FileWriter writer = new FileWriter(file, true)){
            writer.write(str);
            writer.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void prepareFiles(){
        file1 = new File(dir + "StationStatistics_" + ServiceStation.next_id + ".csv");
        file2 = new File(dir + "InputStreamDistribution_" + ServiceStation.next_id + ".csv");
        file3 = new File(dir + "WaitingTimeDistribution_" + ServiceStation.next_id + ".csv");

        try {
            writer = new FileWriter(file1, true);
            writer.write("Time \t Time in work \t Avg waiting time \t Total served \t Utilization \t Current number \t Queue length \t Avg queue length \n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
