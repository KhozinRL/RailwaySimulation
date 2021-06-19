package ru.simulation.railway;

import org.simplesim.model.RoutingDomain;
import ru.simulation.railway.station.ServiceStation;
import ru.simulation.railway.train.Train;
import ru.simulation.railway.train.TrainsGenerator;
import java.util.LinkedList;

public class TrainStationModel extends RoutingDomain {

    private final ServiceStation[] serviceStations_A;
    private final ServiceStation serviceStation_B;
    TrainsGenerator[] tgs;

    public TrainStationModel(ServiceStation[] serviceStations_A, ServiceStation serviceStation_B, TrainsGenerator[] generators) throws Exception{
        super();
        if (serviceStations_A.length != generators.length)
            throw new Exception("Number of Service Stations not equal to number of generators");

        this.serviceStations_A = serviceStations_A;
        this.serviceStation_B = serviceStation_B;
        this.tgs = generators;
        setAsRootDomain();
        initializeModel();
    }

    public ServiceStation getServiceStation_A(int id) {
        return serviceStations_A[id-1];
    }

    public ServiceStation getServiceStation_B(){
        return serviceStation_B;
    }

    public void initializeModel(){

        for (TrainsGenerator t: tgs) {
            LinkedList<Train> trains = t.generate();
            for (Train train : trains){
                this.addEntity(train);
            }
        }

        for (ServiceStation station: serviceStations_A) {
            this.addEntity(station);
            this.addEntity(station.getState().getStatistics());
        }
        this.addEntity(serviceStation_B);
        this.addEntity(serviceStation_B.getState().getStatistics());

        Train.model = this;
    }


    @Override
    public String getName() {
        return "Train station";
    }
}
