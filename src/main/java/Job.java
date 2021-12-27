import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Job implements Runnable  {
    static Logger log = LogManager.getLogger(Job.class.getName());
    private final int frequency;
    private final int expectedInterval;
    private final int id;
    private final CronScheduler cronScheduler;
    private final TimeUnit timeUnit;
    private final Runnable taskJob;
    static  Set<Integer> registeredJobs = new HashSet<>();

    /**
     *
     * @param taskJob Runnable Object to run
     * @param frequency period to run
     * @param expected_interval expected time to run within
     * @param id Unique id to identify job with
     * @param unit TimeUnit Object to specify entered time (frequency,expected_interval) units
     * @param cronScheduler The Scheduler which will run the job
     * @throws IllegalArgumentException if id is not unique (Exists before)
     */
    public Job(Runnable taskJob,int frequency, int expected_interval, int id, TimeUnit unit,CronScheduler cronScheduler) throws IllegalArgumentException{
        if(registeredJobs.contains(id)) {
            log.error("Trying to submit a job with existing id "+id);
            throw new IllegalArgumentException("Replicated job ID "+id);
        }
        this.taskJob = taskJob;
        registeredJobs.add(id);
        this.cronScheduler = cronScheduler;
        this.frequency = frequency;
        this.expectedInterval = expected_interval;
        this.id = id;
        this.timeUnit = unit;
        log.info("New job created with id "+id);
    }

    @Override
    public final void run(){
        log.info("Running a job with id "+this.getId());
        Future<?> task = Executors.newSingleThreadExecutor().submit(this::jobTaskWrapper);
        cronScheduler.getScheduler().schedule(() -> {
            if(! task.isDone()) {
                log.error("Job with id "+getId()+" timed out");
                task.cancel(true);
            }
        }, getExpectedInterval(), getTimeUnit());

    }
    private void jobTaskWrapper(){
        long time = System.nanoTime();
        taskJob.run();
        time = System.nanoTime() - time;
        log.info("Job with id "+this.getId()+" finished , Executed time = "+ time/1e6+" millie seconds");
    }

    @Override
    public int hashCode(){
        return this.id;
    }
    public int getFrequency() {
        return frequency;
    }

    public int getExpectedInterval() {
        return expectedInterval;
    }


    public int getId() {
        return id;
    }
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

}
