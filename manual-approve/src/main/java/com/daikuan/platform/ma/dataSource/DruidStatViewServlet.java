/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.dataSource;

import com.alibaba.druid.support.http.StatViewServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;

/**
 * 
 * @author taosj
 * @version DruidStatViewServlet.java, v0.1 2017年3月7日 上午9:28:44
 */
@WebServlet(urlPatterns = "/druid/*", initParams = { @WebInitParam(name = "allow", value = "127.0.0.1"), // IP白名单(没有配置或者为空，则允许所有访问)
        @WebInitParam(name = "deny", value = ""), // IP黑名单(存在共同时，deny优先于allow)
        @WebInitParam(name = "loginUsername", value = "admin"), // 用户名
        @WebInitParam(name = "loginPassword", value = "admin123"), // 密码
        @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的 Reset
                                                            // All功能
})
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 1L;

}