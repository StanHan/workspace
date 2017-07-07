package demo.javax.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LogFilter implements Filter {

	private FilterConfig filterConfig;

	public FilterConfig getFilterConfig() {
		System.err.println("...getFilterConfig...");
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		System.err.println("...setFilterConfig...");
		this.filterConfig = filterConfig;
	}

	@Override
	public void destroy() {
		System.err.println("...filter destroy...");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.err.println("...doFilter...");
		chain.doFilter(request, response);// 看到这没，这只要是传递下一个Filter
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.err.println("...init Filter...");
		this.filterConfig = filterConfig;
	}

}
