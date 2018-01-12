package demo.spring.framework.web.servlet.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import demo.spring.framework.web.context.WebRequestInterceptorDemo;
import demo.spring.framework.web.servlet.HandlerInterceptorDemo;

@Configuration // 标注此文件为一个配置项，spring boot才会扫描到该配置。该注解类似于之前使用xml进行配置
public class DemoWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    /**
     * 拦截器addInterceptors
     * 
     * 要实现拦截器功能需要完成以下2个步骤：
     * <li>创建我们自己的拦截器类并实现 HandlerInterceptor 接口
     * <li>其实重写WebMvcConfigurerAdapter中的addInterceptors方法把自定义的拦截器类添加进来即可
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        
        registry.addWebRequestInterceptor(new WebRequestInterceptorDemo()).addPathPatterns("/demo/**");

        // addPathPatterns("/**")对所有请求都拦截，但是排除了/toLogin和/login请求的拦截。
        registry.addInterceptor(new HandlerInterceptorDemo()).addPathPatterns("/**")// 添加拦截规则
                .excludePathPatterns("/toLogin", "/login"); // 排除的拦截规则
        super.addInterceptors(registry);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // TODO Auto-generated method stub
        super.configureDefaultServletHandling(configurer);
    }

    /**
     * 自定义资源映射addResourceHandlers， 配置静态访问资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations指的是文件放置的目录，addResourceHandler指的是对外暴露的访问路径
        registry.addResourceHandler("/my/**", "/demo/**").addResourceLocations("file:G:/logs/");
        registry.addResourceHandler("/my/**", "/demo/**").addResourceLocations("classpath:/my/");
        super.addResourceHandlers(registry);
    }

    /**
     * 页面跳转addViewControllers。
     * 以前写SpringMVC的时候，如果需要访问一个页面，必须要写Controller类，然后再写一个方法跳转到页面，感觉好麻烦，其实重写WebMvcConfigurerAdapter中的addViewControllers方法即可达到效果了
     * 
     * 值的指出的是，在这里重写addViewControllers方法，并不会覆盖WebMvcAutoConfiguration中的addViewControllers（在此方法中，Spring
     * Boot将“/”映射至index.html），这也就意味着我们自己的配置和Spring Boot的自动配置同时有效
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("/login");
        super.addViewControllers(registry);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        // TODO Auto-generated method stub
        super.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // TODO Auto-generated method stub
        super.configureMessageConverters(converters);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        // TODO Auto-generated method stub
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

}
