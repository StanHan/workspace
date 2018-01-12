package demo.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;

/**
 * 创建要被定执行的任务类。创建一个实现了org.quartz.Job接口的类，并实现这个接口的唯一一个方法execute(JobExecutionContext arg0) throws JobExecutionException即可。
 * JobExecutionContext保存了job的上下文信息，比如绑定的是哪个trigger
 */
public class JobDemo implements Job {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 执行定时器任务
        Trigger trigger = context.getTrigger();
        Scheduler scheduler = context.getScheduler();
        System.err.println(String.format("hello world at %s, trigger[%s - %s]", sdf.format(new Date()),
                trigger.getCalendarName(), context.getJobDetail().getDescription()));

    }
}