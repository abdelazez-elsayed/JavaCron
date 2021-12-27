
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CronScheduler {
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    private final ScheduledExecutorService scheduler;
    static Logger log = LogManager.getLogger(CronScheduler.class.getName());

    /**
     *
     * @param corePoolSize thread pool size of scheduler
     */
    public CronScheduler (int corePoolSize){
        log.info("Instantiating Scheduler");
        scheduler = Executors.newScheduledThreadPool(corePoolSize);
    }

    /**
     *
     * @param job A job to run by the scheduler
     */
    public void submitJob(Job job){

        log.info("Submitting job");
        scheduler.scheduleAtFixedRate(job,
                0,
                job.getFrequency(),
                job.getTimeUnit());
        log.info("New job submitted with id "+job.getId());
    }


}
