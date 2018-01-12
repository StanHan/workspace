package demo.spring.framework.web.servlet;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HandlerInterceptorDemo implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorDemo.class);

    private final AtomicLong count = new AtomicLong(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /**
         * 对来自后台的请求统一进行处理
         */
        long tmp = count.incrementAndGet();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Map<String, String[]> parameterMap = request.getParameterMap();
        logger.info("parameterMap:{}", parameterMap.toString());
        logger.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s", url, method, uri, queryString));
        
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if(user == null) {
//            response.sendError(-1, "need login .");
//            response.sendRedirect("/toLogin");
//            return false;
            return true;
        } else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler,
            ModelAndView modelAndView) throws Exception {
        logger.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object handler, Exception e) throws Exception {
        logger.info("afterCompletion");
    }
}