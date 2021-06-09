package ru.simulation.railway;

import org.simplesim.core.messaging.ForwardingStrategy;
import org.simplesim.core.messaging.RoutedMessageForwarding;
import org.simplesim.simulator.DynamicDecorator;
import org.simplesim.simulator.SequentialDESimulator;
import org.simplesim.simulator.Simulator;
import ru.simulation.railway.train.TrainsStatistics;

public class TrainStationMain {

    public static void main(String[] args) {

        TrainStationModel model = new TrainStationModel(1,1,5);
        model.initializeModel();

        /*ArrayList<Train> trains = tg.generate();
        for (Train t: trains) {
            model.addEntity(t);
        }*/

        final ForwardingStrategy fs=new RoutedMessageForwarding(model);
        //final Simulator simulator=new DynamicDecorator(new ConcurrentDESimulator(model,fs));
        final Simulator simulator=new DynamicDecorator(new SequentialDESimulator(model,fs));

        // add observer
        simulator.registerEventsProcessedListener(CurrentTimeListener.getCurrentTimeListener());

        simulator.runSimulation(TimeConstants.END_SIMULATION);

        System.out.println("\nFirst service station waiting avg time: " + TrainsStatistics.firstWaitingAVGTime);
        System.out.println("Trains processed: " + TrainsStatistics.getNumOfFirstServiceTrains());
        System.out.println("Simulator total time: " + simulator.getSimulationTime());
    }
}
