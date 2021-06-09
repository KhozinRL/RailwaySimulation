package ru.simulation.railway.train;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.simplesim.core.scheduling.Time;
import ru.simulation.railway.TimeConstants;
import ru.simulation.railway.train.Train;

import java.util.LinkedList;

public class TrainsGenerator {

    public enum DistributionType {
        TRIANGULAR, NORMAL;
    }
    private final AbstractRealDistribution timeDistribution; //Distribution type
    private final AbstractIntegerDistribution threadDistribution;
    private int number; //Number of trains to generate

    /*  a - min in hours
        b - max in hours
        c - mean
    */
    public TrainsGenerator(AbstractRealDistribution timeDistribution, AbstractIntegerDistribution threadDistribution, int numberOfTrains){
        this.timeDistribution = timeDistribution;
        this.threadDistribution = threadDistribution;
        number = numberOfTrains;
    }

    public LinkedList<Train> generate(){
        Time lastTime = TimeConstants.START_SIMULATION;
        LinkedList<Train> list = new LinkedList<>();

        for(int i = 0; i < number; i++){
            Double interval = timeDistribution.sample() * Time.TICKS_PER_HOUR;
            lastTime = lastTime.add(interval.intValue());
            list.add(new Train(threadDistribution.sample(), lastTime));
        }

        return list;
    }

    /*public static void main(String[] args) throws IOException {
        TrainsGenerator tg = new TrainsGenerator(0, 0.5, 1, 100);

        ArrayList<Train> trains = tg.generate();
        System.out.println("hello");
        for (Train t: trains) {
            System.out.println(t + "\n");
        }*/



        /*FileWriter fileWriter = new FileWriter("1.txt", true);
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.flush();

        for (double d: ar) {
            pw.println(d);
        }
        pw.close();
    }*/
}

