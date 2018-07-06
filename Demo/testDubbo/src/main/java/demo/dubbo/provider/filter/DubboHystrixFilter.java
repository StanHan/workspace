package demo.dubbo.provider.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

import demo.dubbo.provider.hystrix.DubboHystrixCommand;

public class DubboHystrixFilter implements Filter {

    boolean hystrixIsOpen = true;

    @Override

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 是否启用hystrix
        if (!hystrixIsOpen) {
            return invoker.invoke(invocation);
        }

        String group = invoker.getUrl().getParameter("group");

        URL url = invoker.getUrl();

        // 未配置groupKey的接口不进行限流

        if (StringUtils.isBlank(group)) {
            group = invoker.getUrl().getParameter(Constants.ID_KEY);
        }

        // int serviceLevel = invoker.getUrl().getParameter(HystrixConstants.SERVICE_LEVEL_KEY,
        // ServiceLevelEnum.NORMAL.getLevel());

        DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation, group);

        return command.execute();

    }

}
