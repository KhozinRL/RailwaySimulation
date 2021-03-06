package ru.simulation.railway.train;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.simplesim.core.scheduling.Time;
import ru.simulation.railway.TimeConstants;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

public class TrainsGenerator {

    private final AbstractRealDistribution timeDistribution;
    private final int id; //Identifier of generator
    private final int number; //Number of trains to generate

    public TrainsGenerator(AbstractRealDistribution timeDistribution, int threadId, int numberOfTrains){
        this.timeDistribution = timeDistribution;
        this.id = threadId;
        number = numberOfTrains;
    }

    public LinkedList<Train> generate(){
        Time lastTime = TimeConstants.START_SIMULATION;
        LinkedList<Train> list = new LinkedList<>();

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < number; i++) {
            Double interval = timeDistribution.sample()*60;
            lastTime = lastTime.add(interval.intValue());
            list.add(new Train(id, lastTime));
            builder.append(Time.TicksToMinutes(interval.intValue())).append("\n");
        }

        return list;
    }
}

