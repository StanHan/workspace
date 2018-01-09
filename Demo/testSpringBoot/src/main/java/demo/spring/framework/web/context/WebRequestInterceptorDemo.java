package demo.spring.framework.web.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class WebRequestInterceptorDemo implements WebRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebRequestInterceptorDemo.class);
    
    @Override
    public void preHandle(WebRequest request) throws Exception {
        String sessionId = request.getSessionId();
        logger.info("sessionId:{}" , sessionId);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}
