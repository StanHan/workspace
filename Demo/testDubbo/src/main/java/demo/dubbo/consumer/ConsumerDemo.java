package demo.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.loanking.galaxy.domain.base.BaseResponse;
import com.loanking.galaxy.facade.MiGuanFacade;
import com.loanking.galaxy.facade.UdspFacade;
import com.loanking.galaxy.facade.domain.UdspRequestVo;

public class ConsumerDemo {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
        context.start();
        // obtain proxy object for remote invocation
        MiGuanFacade demoService = (MiGuanFacade) context.getBean("miGuanFacade");

        UdspFacade udsp = context.getBean(UdspFacade.class);
        // execute remote invocation
        // BaseResponse<JSONObject> hello = demoService.request(null);
        UdspRequestVo vo = new UdspRequestVo();
        vo.setClient("789");
        vo.setCardId("410782198610242812");
        vo.setRealName("韩军营");
        vo.setPhone("18217006685");
        vo.setCardNo("6225882133907151");
        vo.setSerialNo("serialNo12345679");
        BaseResponse<JSONObject> hello = udsp.request(vo);
        // show the result
        System.out.println(hello);
    }
}
