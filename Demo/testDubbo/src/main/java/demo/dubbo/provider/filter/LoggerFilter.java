package demo.dubbo.provider.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group = Constants.PROVIDER)
public class LoggerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        LOGGER.info("调用dubbo拦截器开始执行......");
        long t1 = System.currentTimeMillis();
        Result res = invoker.invoke(invocation);
        long cost = System.currentTimeMillis() - t1;
        LOGGER.info("耗时：{}ms", cost);
        return res;
    }

}