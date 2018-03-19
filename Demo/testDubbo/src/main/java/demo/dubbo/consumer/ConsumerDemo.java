package demo.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.loanking.galaxy.domain.base.BaseResponse;
import com.loanking.galaxy.facade.MiGuanFacade;

public class ConsumerDemo {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "consumer.xml" });
        context.start();
        // obtain proxy object for remote invocation
        MiGuanFacade demoService = (MiGuanFacade) context.getBean("miGuanFacade");
        // execute remote invocation
        BaseResponse<JSONObject> hello = demoService.request("CDW", "18217006685", "410782198610242812", "韩军营");
        // show the result
        System.out.println(hello);
    }
}
