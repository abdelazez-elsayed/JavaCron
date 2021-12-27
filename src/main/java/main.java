import java.util.concurrent.TimeUnit;
public class main {
    public static void main(String[] args) {

        CronScheduler scheduler  = new CronScheduler(1);
        Job simpleJob = new Job(new Runnable() {
            int i=1;
            @Override
            public void run() {
                System.out.println("Jop 1 run for "+i+" time(s)");
                i++;
            }
        }
        ,10,4,991233, TimeUnit.SECONDS,scheduler);
        Job simpleJob2 = new Job(new Runnable() {
            int i=1;
            @Override
            public void run() {
                System.out.println("Jop 2 run for "+i+" time(s)");
                i++;
            }
        },
                2,2,3131, TimeUnit.SECONDS,scheduler);
        Job simpleJob3 = new Job(new Runnable() {
            int i=1;
            @Override
            public void run() {
                System.out.println("Jop 3 run for "+i+" time(s)");
                i++;
            }
        }, 10,4,713, TimeUnit.SECONDS,scheduler);

        scheduler.submitJob(simpleJob);
        scheduler.submitJob(simpleJob3);
        scheduler.submitJob(simpleJob2);
    }
}
