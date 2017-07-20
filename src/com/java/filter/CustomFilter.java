package com.java.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 过滤器使用装饰类来处理请求参数
 * @author lailai
 *
 */
public class CustomFilter implements Filter{

	FilterConfig config=null;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.config=null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		/**
    	 *  // 在这里给FilterChain 传递的参数是传装饰后的request,之后调用的就是EscapeWrapper重写的getParameter(String name )这个方法  
     		// 获取装饰后的Request对象  HttpServletRequest resRequest= new CustomHttpServletRequestWrapper((HttpServletRequest) request);
    	 */
    	 // 使用过滤器就是在请求到大action之前，调用action(HttpServletRequest request,HttpServletResponse response)之前将request对象替换为装饰后的request对象  
        // 只要使用了这个过滤器的请求的reques对象都会被替换为装饰后的request 对象，这样之后调用的getParameter(String str)等方法自然就是装饰后的类的方法 
		CustomHttpServletRequestWrapper wrapper=new CustomHttpServletRequestWrapper((HttpServletRequest)request);
		wrapper.removeParam("_");
		chain.doFilter(wrapper, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		config=filterConfig;
	}

}
