package ru.simulation.railway;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.simplesim.core.messaging.ForwardingStrategy;
import org.simplesim.core.messaging.RoutedMessageForwarding;
import org.simplesim.core.scheduling.Time;
import org.simplesim.simulator.DynamicDecorator;
import org.simplesim.simulator.SequentialDESimulator;
import org.simplesim.simulator.Simulator;
import ru.simulation.railway.station.ServiceStation;
import ru.simulation.railway.train.TrainsGenerator;

public class TrainStationMain {

    public static void main(String[] args) {

        TimeConstants.setEndSimulation(new Time(0,0,30,0,0,0));

        int numberOfFirstOrderServiceStations = 5;
        int capacityOfSecondOrderServiceStation = 5;

        //First order service stations
        ServiceStation[] serviceStations_A = new ServiceStation[numberOfFirstOrderServiceStations];
        serviceStations_A[0] = new ServiceStation(new TriangularDistribution(25,30,35), 1);
        serviceStations_A[1] = new ServiceStation(new TriangularDistribution(15,25,35), 1);
        serviceStations_A[2] = new ServiceStation(new TriangularDistribution(15,25,35), 1);
        serviceStations_A[3] = new ServiceStation(new TriangularDistribution(15,25,35), 1);
        serviceStations_A[4] = new ServiceStation(new TriangularDistribution(15,25,35), 1);

        //Second order service station
        ServiceStation serviceStation_B = new ServiceStation(new TriangularDistribution(15,25,35), capacityOfSecondOrderServiceStation);

        //Generators
        TrainsGenerator[] trainsGenerators = new TrainsGenerator[numberOfFirstOrderServiceStations];
        trainsGenerators[0] = new TrainsGenerator(new ExponentialDistribution(30), 1, 2000);
        trainsGenerators[1] = new TrainsGenerator(new ExponentialDistribution(30), 2, 1500);
        trainsGenerators[2] = new TrainsGenerator(new ExponentialDistribution(30), 3, 1000);
        trainsGenerators[3] = new TrainsGenerator(new ExponentialDistribution(30), 4, 500);
        trainsGenerators[4] = new TrainsGenerator(new ExponentialDistribution(30), 5, 3000);

        try{
            TrainStationModel model = new TrainStationModel(serviceStations_A, serviceStation_B, trainsGenerators);
            final ForwardingStrategy fs=new RoutedMessageForwarding(model);
            final Simulator simulator=new DynamicDecorator(new SequentialDESimulator(model,fs));
            simulator.registerEventsProcessedListener(CurrentTimeListener.getCurrentTimeListener());
            simulator.runSimulation(TimeConstants.END_SIMULATION);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
