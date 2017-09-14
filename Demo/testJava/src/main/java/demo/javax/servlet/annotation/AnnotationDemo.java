package demo.javax.servlet.annotation;

/**
 * 在最新的servlet3.0中引入了很多新的注解，尤其是和servlet安全相关的注解。
 * 
 * <li>HandlesTypes –该注解用来表示一组传递给ServletContainerInitializer的应用类。
 * <li>HttpConstraint – 该注解代表所有HTTP方法的应用请求的安全约束，和ServletSecurity注释中定义的HttpMethodConstraint安全约束不同。
 * <li>HttpMethodConstraint – 指明不同类型请求的安全约束，和ServletSecurity 注解中描述HTTP协议方法类型的注释不同。
 * <li>MultipartConfig –该注解标注在Servlet上面，表示该Servlet希望处理的请求的 MIME 类型是 multipart/form-data。
 * <li>ServletSecurity 该注解标注在Servlet继承类上面，强制该HTTP协议请求遵循安全约束。
 * <li>WebFilter – 该注解用来声明一个Server过滤器；
 * <li>WebInitParam – 该注解用来声明Servlet或是过滤器的中的初始化参数，通常配合 @WebServlet 或者 @WebFilter 使用。
 * <li>WebListener –该注解为Web应用程序上下文中不同类型的事件声明监听器。
 * <li>WebServlet –该注解用来声明一个Servlet的配置。
 *
 */
public class AnnotationDemo {

}
