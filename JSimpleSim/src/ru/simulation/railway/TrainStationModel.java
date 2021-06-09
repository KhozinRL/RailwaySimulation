package ru.simulation.railway;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.simplesim.model.RoutingDomain;
import ru.simulation.railway.station.ServiceStation;
import ru.simulation.railway.train.Train;
import ru.simulation.railway.train.TrainsGenerator;

import java.util.LinkedList;

public class TrainStationModel extends RoutingDomain {

    private final ServiceStation[] serviceStations_A;
    //private final ServiceStation serviceStation1 = new ServiceStation(5);
    private final ServiceStation serviceStation2;
    TrainsGenerator tg;

    public TrainStationModel(int numberOfTrains, int numberOfStations, int capacityOfSecondServiceStation){
        super();
        tg = new TrainsGenerator(new TriangularDistribution(0,0.5,1), new UniformIntegerDistribution(1,numberOfStations), numberOfTrains);
        serviceStations_A = new ServiceStation[numberOfStations];
        initializeStationsA(numberOfStations, 1);
        serviceStation2 = new ServiceStation(capacityOfSecondServiceStation);
        setAsRootDomain();
    }

    public ServiceStation getStation(int id) {
        return serviceStations_A[id-1];
    }
    public ServiceStation getSecondStation(){
        return serviceStation2;
    }

    public int getServiceStationsALen(){
        return serviceStations_A.length;
    }

    public void initializeModel(){
        for (ServiceStation station: serviceStations_A) {
            this.addEntity(station);
        }
        //this.addEntity(serviceStation1);
        this.addEntity(serviceStation2);

        LinkedList<Train> trains = tg.generate();
        for (Train train : trains){
            this.addEntity(train);
        }

        Train.model = this;
    }

    public void initializeStationsA(int number, int capacity){
        for(int i = 0; i < number; i++){
            serviceStations_A[i] = new ServiceStation(capacity);
        }
    }

    @Override
    public String getName() {
        return "Train station";
    }

    public static void main(String[] args) {
        TrainStationModel m = new TrainStationModel(1,2,5);
        System.out.println(m.serviceStations_A[0].getState().getCapacity());
    }
}
