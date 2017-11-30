package demo.org.quartz;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 * Quartz是一个任务调度框架。Quartz最重要的3个基本要素：
 * 
 * <li>Scheduler：调度器。所有的调度都是由它控制。
 * <li>Trigger： 定义触发的条件。
 * <li>JobDetail & Job： JobDetail 定义的是任务数据，而真正的执行逻辑是在Job中，为什么设计成JobDetail +
 * Job，不直接使用Job？这是因为任务是有可能并发执行，如果Scheduler直接使用Job，就会存在对同一个Job实例并发访问的问题。而JobDetail & Job
 * 方式，sheduler每次执行，都会根据JobDetail创建一个新的Job实例，这样就可以规避并发访问的问题。
 *
 */
public class QuartzDemo {

    public static void main(String[] args) {

    }

    static void demo2() {
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            scheduler.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    static void demo1() {
        // "jobDetail1", "group1", HelloQuartz.class
        JobDetail jobDetail = new JobDetailImpl();
        JobBuilder jobBuilder = JobBuilder.newJob();
        jobDetail.getJobDataMap().put("name", "quartz");

        SimpleTriggerImpl trigger = new SimpleTriggerImpl("trigger1", "group1");
        trigger.setStartTime(new Date());
        trigger.setRepeatInterval(1);
        trigger.setRepeatCount(-1);
    }

}
