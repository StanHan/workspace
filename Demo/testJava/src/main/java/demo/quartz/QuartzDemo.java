package demo.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
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

    /**
     * Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，Cron有如下两种语法格式：
     * <li>Seconds Minutes Hours DayofMonth Month DayofWeek Year
     * <li>Seconds Minutes Hours DayofMonth Month DayofWeek
     * <p>
     * 每一个域可出现的字符如下：
     * <ol>
     * <li>Seconds: 可出现 ", - * /"四个字符，有效范围为0-59的整数
     * <li>Minutes: 可出现 ", - * /"四个字符，有效范围为0-59的整数
     * <li>Hours: 可出现 ", - * /"四个字符，有效范围为0-23的整数
     * <li>DayofMonth:可出现 ", - * / ? L W C"八个字符，有效范围为0-31的整数
     * <li>Month: 可出现 ", - * /"四个字符，可以用0-11 或用字符串 “JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC” 表示
     * <li>DayofWeek: 可出现 ", - * / ? L C #"四个字符，有效范围为1-7的整数或SUN-SAT(SUN，MON，TUE，WED，THU，FRI，SAT)两个范围。1表示星期天，2表示星期一
     * <li>Year: 可出现 ", - * /"四个字符，有效范围为1970-2099年
     * </ol>
     * <p>
     * 每一个域都使用数字，但还可以出现如下特殊字符，它们的含义是：
     * <ul>
     * <li>(1) * ： 表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。
     * <li>(2) ? : 只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和DayofWeek会相互影响。
     * 例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。
     * <li>(3) - : 表示范围，例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次
     * <li>(4) / ： 表示起始时间开始触发，然后每隔固定时间触发一次，例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次.
     * <li>(5) , : 表示列出枚举值值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
     * <li>(6) L : 表示最后，只能出现在DayofWeek和DayofMonth域，如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。
     * <li>(7) W : 表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。
     * 例如：在DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。如果5日是星期天，则在6日(周一)触发；如果5日在星期一到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份
     * <li>(8) LW : 这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。
     * <li>(9) # : 用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。6#3表示该月第3个周五
     * </ul>
     * 举几个例子:
     * <li>"0 0 2 1 * ? *" 表示在每月的1日的凌晨2点调度任务
     * <li>"0 15 10 ? * MON-FRI" 表示周一到周五每天上午10：15执行作业
     * <li>"0 15 10 ? 6L 2002-2006" 表示2002-2006年的每个月的最后一个星期五上午10:15执行作。
     * 其中每个元素可以是一个值(如6),一个连续区间(9-12),一个间隔时间(8-18/4)(/表示每隔4小时),一个列表(1,3,5),通配符。由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置?
     * <li>"0 0 10,14,16 * * ?" 每天上午10点，下午2点，4点
     * <li>"0 0/30 9-17 * * ?" 朝九晚五工作时间内每半小时
     * <li>"0 0 12 ? * WED" 表示每个星期三中午12点
     * <li>"0 0 12 * * ?" 每天中午12点触发
     * <li>"0 15 10 ? * *" 每天上午10:15触发
     * <li>"0 15 10 * * ?" 每天上午10:15触发
     * <li>"0 15 10 * * ? *" 每天上午10:15触发
     * <li>"0 15 10 * * ? 2005" 2005年的每天上午10:15触发
     * <li>"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
     * <li>"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
     * <li>"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
     * <li>"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
     * <li>"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
     * <li>"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
     * <li>"0 15 10 15 * ?" 每月15日上午10:15触发
     * <li>"0 15 10 L * ?" 每月最后一日的上午10:15触发
     * <li>"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
     * <li>"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
     * <li>"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
     * 
     */
    static void test() throws ParseException, SchedulerException {
        JobDetail job = new JobDetailImpl();

        Trigger trigger = new CronTriggerImpl();

        CronExpression cronExpression = new CronExpression("1/1 * * * * ?");
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setCronExpression(cronExpression);

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        scheduler.scheduleJob(job, trigger);
        scheduler.shutdown();
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

class HelloWorldJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 执行定时器任务
        Trigger trigger = context.getTrigger();
        Scheduler scheduler = context.getScheduler();
        System.out.println(String.format("hello world at %s, trigger[%s - %s]", date(), trigger.getCalendarName(),
                context.getJobDetail().getDescription()));

    }

    private String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
