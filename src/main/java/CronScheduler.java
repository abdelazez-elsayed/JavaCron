
import java.util.Timer;
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CronScheduler {
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    private ScheduledExecutorService scheduler;
    Timer timer;
    static Logger log = LogManager.getLogger(CronScheduler.class.getName());

    public CronScheduler (int corePoolSize){
        log.info("Instantiating Scheduler");
        scheduler = Executors.newScheduledThreadPool(corePoolSize);
     //   timer = new Timer();
    }
    public void submitJob(Job job){

        log.info("Submitting job");
        long milis = job.getTimeUnit().toMillis(job.getFrequency());
        ScheduledFuture<?> jobHandler= scheduler.scheduleAtFixedRate(job,
                0,
                job.getFrequency(),
                job.getTimeUnit());
//        timer.scheduleAtFixedRate(job, 0, milis);
        log.info("New job submitted with id "+job.getId());
    }


}
