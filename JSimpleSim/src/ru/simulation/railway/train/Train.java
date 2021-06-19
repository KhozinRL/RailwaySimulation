package ru.simulation.railway.train;

import org.simplesim.core.messaging.RoutedMessage;
import org.simplesim.core.scheduling.Time;
import org.simplesim.model.AbstractAgent;
import org.simplesim.model.RoutingAgent;
import ru.simulation.railway.TimeConstants;
import ru.simulation.railway.TrainStationModel;
import java.io.File;
import java.io.FileWriter;

public class Train extends RoutingAgent<TrainState, Train.Event> {

    public enum Event{
        REQUEST_SERVICE1, REQUEST_SERVICE2, SINK, WAITING
    }

    public static TrainStationModel model;

    private static int counter = 1;
    int trainId; // Unique train identifier

    static File file = new File("Trains.csv");

    static {
        file.delete();
    }

    public Train(int directionId, Time time){
        super(new TrainState());
        getState().setActivity(TrainState.Activity.waitingForTheFirstService);
        getState().setDirectionId(directionId);
        trainId = counter;
        getState().setTimeOfInjection(time);
        counter += 1;
        getEventQueue().enqueue(Event.REQUEST_SERVICE1, time);
    }

    @Override
    protected Time doEvent(Time time) {
        switch (getEventQueue().dequeue()) {
            case REQUEST_SERVICE1 -> {
                sendRequest(model.getServiceStation_A(getState().getDirectionId()), time);
                System.out.print("\n\nTrainId: " + trainId + " sent request to A station №" + getState().getDirectionId() + " at " + time + "\n");
                getEventQueue().enqueue(Event.WAITING, time);
                break;
            }
            case REQUEST_SERVICE2 -> {
                sendRequest(model.getServiceStation_B(), time);
                System.out.print("\n\nTrainId: " + trainId + " sent request to B at " + time + "\n");
                getEventQueue().enqueue(Event.WAITING, time);
                break;
            }
            case WAITING -> {
                waitingForReply(time);

                if (this.getState().getActivity() == TrainState.Activity.waitingForTheFirstService && this.getState().getEndOfFirstService() != null) {
                    getEventQueue().enqueue(Event.REQUEST_SERVICE2, this.getState().getEndOfFirstService());
                    getState().setActivity(TrainState.Activity.waitingForTheSecondService);
                } else if (this.getState().getActivity() == TrainState.Activity.waitingForTheSecondService && this.getState().getEndOfSecondService() != null) {
                    getEventQueue().enqueue(Event.SINK, Time.INFINITY);
                    getState().setActivity(TrainState.Activity.idle);
                    toFile();
                }

                break;
            }
        }
        return getTimeOfNextEvent();
    }

    public void waitingForReply(Time time){
        if(getInport().hasMessages()){
            TrainRequest reply = getInport().poll().getContent();
            System.out.println("\n\nReply got at: " + time + "\n" + reply);
        }
        else{
            getEventQueue().enqueue(Event.WAITING, time.add(TimeConstants.IDLE_TIME));
        }
    }

    @Override
    public String toString() {
        Time timeOfInjection = getState().getTimeOfInjection();
        return String.format("\n\n TrainID: %d, Time of injection: %d hour, %d minute", trainId, timeOfInjection.hours(), timeOfInjection.minutes());
    }

    public void sendRequest(AbstractAgent<?, ?> destAgent, Time time) {
        TrainRequest request=new TrainRequest(this, time);
        final RoutedMessage msg=new RoutedMessage(this.getAddress(),destAgent.getAddress(),request);
        getOutport().write(msg); // send request to elevator
    }

    private void toFile(){
        try (FileWriter writer = new FileWriter(file, true))
        {
                writer.append("\n");
                writer.append(String.valueOf(getState().getDirectionId()));
                writer.append("\t").append(String.valueOf(trainId));
                writer.append("\t").append(String.valueOf(Time.TicksToMinutes(getState().getTimeOfInjection().getTicks())));
                writer.append("\t").append(String.valueOf(getState().getStartOfFirstService()));
                writer.append("\t").append(String.valueOf(getState().getEndOfFirstService()));
                writer.append("\t").append(String.valueOf(getState().getStartOfSecondService()));
                writer.append("\t").append(String.valueOf(getState().getEndOfSecondService()));
                writer.flush();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
