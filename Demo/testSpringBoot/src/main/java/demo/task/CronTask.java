package demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CronTask {
    
    private Logger logger = LoggerFactory.getLogger(CronTask.class);


    @Scheduled(cron =  "1/1 * * * * ?")
    public void execute() {
        logger.info("开始执行咯...");
    }

}
