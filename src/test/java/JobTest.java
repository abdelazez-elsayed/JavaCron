import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class JobTest {


    CronScheduler scheduler;
    @Before
    public void setUp(){
        scheduler = new CronScheduler(1);
        Job.registeredJobs.clear();
    }
    @Test
    public void testOneJob(){
        AtomicReference<Integer> i = new AtomicReference<>(0);
        Job job = new Job(()->{
            i.getAndSet(i.get() + 1);
        },1,1,231, TimeUnit.SECONDS,scheduler);
        scheduler.submitJob(job);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(i.get().intValue(),10);
    }
    @Test
    public void TestTwoJobs(){
        AtomicReference<Integer> i = new AtomicReference<>(0);
        AtomicReference<Integer> j = new AtomicReference<>(0);

        Job job1 = new Job(()->{
            i.getAndSet(i.get() + 1);
        },1,1,231, TimeUnit.SECONDS,scheduler);
        Job job2 = new Job(()->{
            j.getAndSet(j.get() + 1);
        },1,1,2331, TimeUnit.SECONDS,scheduler);
        scheduler.submitJob(job2);
        scheduler.submitJob(job1);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(i.get().intValue()==j.get().intValue() && j.get().intValue()==10);
    }
    @Test
    public void testDuplicatedIDJob(){
        Job job = new Job(()->{},1,1,1,TimeUnit.SECONDS,scheduler);
        try{
            Job job2 = new Job(()->{},1,1,1,TimeUnit.SECONDS,scheduler);
            Assert.fail();
        }catch (IllegalArgumentException ignored){

        }

    }
    @Test
    public void testTooLongJob() throws InterruptedException {
        AtomicReference<Integer> i = new AtomicReference<>(0);
        Job job = new Job(()->{
            try {
                Thread.sleep(10000);
                i.getAndSet(i.get()+1);
            } catch (InterruptedException ignored) {

            }
        },2,1,1,TimeUnit.SECONDS,scheduler);
        scheduler.submitJob(job);
        Thread.sleep(10000);
        Assert.assertEquals(i.get().intValue(),0);
    }
    @Test
    public void testShortAndLongJobsTogether(){
        AtomicReference<Integer> i = new AtomicReference<>(0);
        AtomicReference<Integer> j = new AtomicReference<>(0);

        Job job1 = new Job(()->{
            try {
                Thread.sleep(10000);
                i.getAndSet(i.get() + 1);
            } catch (InterruptedException e) {
            }
        },1,1,231, TimeUnit.SECONDS,scheduler);
        Job job2 = new Job(()->{
            j.getAndSet(j.get() + 1);
        },1,1,2331, TimeUnit.SECONDS,scheduler);
        scheduler.submitJob(job2);
        scheduler.submitJob(job1);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
        int[] expected = {0,10};
        int[] actual = {i.get(), j.get()};
        Assert.assertArrayEquals(expected,actual);
    }
}