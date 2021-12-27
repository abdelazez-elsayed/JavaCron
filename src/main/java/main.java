import java.util.concurrent.TimeUnit;
public class main {
    public static void main(String[] args) {

        CronScheduler scheduler  = new CronScheduler(1);
        Job simpleJob = new Job(() -> {
            System.out.println("Jop 1 run");
        },10,4,991233, TimeUnit.SECONDS,scheduler);
        Job simpleJob2 = new Job(()->{
            System.out.println("Job 2 run");
        },
                2,2,3131, TimeUnit.SECONDS,scheduler);
        Job simpleJob3 = new Job(()->{
            System.out.println("Job 3 run");
        }, 10,4,713, TimeUnit.SECONDS,scheduler);

        scheduler.submitJob(simpleJob);
        scheduler.submitJob(simpleJob3);
        scheduler.submitJob(simpleJob2);
    }
}
