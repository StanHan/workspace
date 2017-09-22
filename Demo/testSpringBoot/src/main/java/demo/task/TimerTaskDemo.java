package demo.task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务，用于将审批过的案件提交到WS
 * 
 * @author hanjy
 *
 */
@Component
public class TimerTaskDemo {

    private Logger logger = LoggerFactory.getLogger(TimerTaskDemo.class);

    private static final long delay = 1;
    private static final long intevalPeriod = 100;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Timer timer = new Timer();

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            logger.info("start a new task.");
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    printLog();
                }
            });
        }
    };

    @PostConstruct
    public void startTask() {
        logger.info("start Task.");
        timer.scheduleAtFixedRate(timerTask, delay, intevalPeriod);
    }
    
    void printLog(){
        logger.trace("----------------------trace----------------------");
        logger.debug("----------------------debug----------------------");
        logger.info("----------------------info----------------------");
        logger.warn("----------------------warn----------------------");
        logger.error("----------------------error----------------------");
    }

}
