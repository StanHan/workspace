package demo.dubbo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.loanking.galaxy.domain.base.BaseResponse;
import com.loanking.galaxy.facade.UdspFacade;
import com.loanking.galaxy.facade.domain.UdspRequestVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HystrixDemo {

    private static Logger logger = LoggerFactory.getLogger(HystrixDemo.class);

    @Autowired
    private UdspFacade udsp;

    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public String demoHystrix() {
        UdspRequestVo vo = new UdspRequestVo();
        vo.setClient("789");
        vo.setCardId("410782198610242812");
        vo.setRealName("韩军营");
        vo.setPhone("18217006685");
        vo.setCardNo("6225882133907151");
        vo.setSerialNo("serialNo12345679");
        BaseResponse<JSONObject> hello = udsp.request(vo);
        return hello.toString();
    }

    String fallbackMethod(Throwable throwable) {
        logger.warn("fallback,excecption:{}", throwable.getMessage());
        return "fallback:" + throwable.getMessage();
    }
}
