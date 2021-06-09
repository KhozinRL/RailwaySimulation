package ru.simulation.railway.train;

import org.simplesim.core.scheduling.Time;

public class TrainsStatistics {
    static private Time totalWorkingFirstTime = Time.ZERO;
    static private Time totalWaitingFirstTime = Time.ZERO;
    static private int numOfFirstServiceTrains = 0;
    static public Time firstServiceAVGTime;
    static public Time firstWaitingAVGTime;
    static private Time totalTime;

    static public void processTrain(TrainState state) throws Exception{
        //TrainState state = train.getState();
        if(state.getEndOfFirstService() == null)
            throw new Exception("Listener Exception");

        numOfFirstServiceTrains++;

        Time injection = state.getTimeOfInjection();
        Time startOfFirstService = state.getStartOfFirstService();
        Time endOfFirstService = state.getEndOfFirstService();


        //Service avg time
        totalWorkingFirstTime = totalWorkingFirstTime.add(endOfFirstService.getTicks()-startOfFirstService.getTicks());
        firstServiceAVGTime = new Time(totalWorkingFirstTime.getTicks()/numOfFirstServiceTrains);
        //System.out.println("First service station avg time: " + firstServiceAVGTime);

        //Waiting avg time
        totalWaitingFirstTime = totalWaitingFirstTime.add(startOfFirstService.getTicks() - injection.getTicks());
        firstWaitingAVGTime = new Time(totalWaitingFirstTime.getTicks()/numOfFirstServiceTrains);
        //System.out.println("First service station waiting avg time: " + firstWaitingAVGTime);

        totalTime = endOfFirstService;
    }

    static public int getNumOfFirstServiceTrains() {
        return numOfFirstServiceTrains;
    }

    static public int percentageOfWork(){
        System.out.println("Total time: " + totalTime + "\nWorking time: " + totalWorkingFirstTime);
        return (int) (totalWorkingFirstTime.getTicks()/totalTime.getTicks() * 100);
    }
}
