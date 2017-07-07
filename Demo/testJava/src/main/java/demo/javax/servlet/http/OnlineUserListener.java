package demo.javax.servlet.http;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class OnlineUserListener
 *
 */
@WebListener
public class OnlineUserListener
		implements HttpSessionListener, ServletContextListener, ServletContextAttributeListener {

	private long onlineUserCount = 0;

	public long getOnlineUserCount() {
		return onlineUserCount;
	}

	/**
	 * Default constructor.
	 */
	public OnlineUserListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletcontextevent) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
	 */
	public void attributeAdded(ServletContextAttributeEvent servletcontextattributeevent) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent httpsessionevent) {
		onlineUserCount++;
		toUpdateCount(httpsessionevent);
	}

	/**
	 * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
	 */
	public void attributeReplaced(ServletContextAttributeEvent servletcontextattributeevent) {
		System.err.println("...attributeReplaced...");
	}

	/**
	 * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
	 */
	public void attributeRemoved(ServletContextAttributeEvent servletcontextattributeevent) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent httpsessionevent) {
		onlineUserCount--;
		toUpdateCount(httpsessionevent);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		// TODO Auto-generated method stub
	}

	private void toUpdateCount(HttpSessionEvent httpSessionEvent){  
        httpSessionEvent.getSession().setAttribute("onlineUserCount", onlineUserCount);  
    } 
}
