package hu.noroc.test.utils.common;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Oryk on 4/28/2016.
 */
public class TimeMeasure {
    private Logger logger = Logger.getLogger(TimeMeasure.class.getSimpleName());

    private long startTime;
    private boolean measuring;
    private String measureTarget;

    public void start(String measureTarget){
        if(measuring) return;
        this.measureTarget = measureTarget;
        measuring = true;
        startTime = System.nanoTime();
    }

    public void stopAndLog(){
        long time = System.nanoTime() - startTime;
        logger.info("Measured time for " + measureTarget + ".\nTime result: "
                + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS) + " ms, "
                + (time % 1000000) + " ns.");
        measuring = false;
    }

    public boolean isMeasuring() {
        return measuring;
    }
}
