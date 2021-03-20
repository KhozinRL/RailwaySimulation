package ru.simulation.railway.train;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.simplesim.core.scheduling.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class trainsGenerator {

    private AbstractRealDistribution distribution;
    private int number;
    Time SIMULATION_START = new Time(0,0,0,0,0,0); //time of simulation start

    /*  a - min in hours
        b - max in hours
        c - mean
    */
    public trainsGenerator(double a, double c, double b, int numberOfTrains){
        distribution = new TriangularDistribution(a, c, b);
        number = numberOfTrains;
    }

    public List<Train> generate(){
        Time lastTime = SIMULATION_START;
        List<Train> list = new ArrayList<>();

        for(int i = 0; i < number; i++){
            Double interval = distribution.sample() * Time.TICKS_PER_HOUR;
            lastTime = lastTime.add(interval.intValue());
            list.add(new Train(lastTime));
        }

        return new ArrayList<Train>();
    }

    public static void main(String[] args) throws IOException {
        trainsGenerator tg = new trainsGenerator(0, 0.5, 1, 100);

        Double d = tg.distribution.sample() * Time.TICKS_PER_HOUR;

        /*FileWriter fileWriter = new FileWriter("1.txt", true);
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.flush();

        for (double d: ar) {
            pw.println(d);
        }
        pw.close();*/
    }
}
