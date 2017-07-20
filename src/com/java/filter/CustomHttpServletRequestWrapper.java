package com.java.filter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 装饰模式
 * @author lailai
 *
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper{

	private String requestURL="";
	private Map<String,String[]> params=null;
	public CustomHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.requestURL=request.getRequestURI();
		params=new HashMap<String, String[]>();
		this.params.putAll(request.getParameterMap());
		// TODO Auto-generated constructor stub
	}

	public void removeParam(String key){
		this.params.remove(key);
	} 
	/**
	 * 如何在Filter中修改后台Controller中获取到的HttpServletRequest中的参数？
	 * 只需要在Filter中自定义一个类继承于HttpServletRequestWrapper，并复写getParameterNames、getParameter、getParameterValues等方法即可
	 */
	/*public String[] getParameterValues(String parameter){
		String[] value=this.params.get(parameter);
		if(value==null){
			return null;
		}
		int count=value.length;
		String[] v=new String[count];
		for(int i=0;i<count;i++){
			v[i]=cleanXSS(value[i]);
		}
		return v;
	}
	public void addParameter(String key,Object value){
		if(value!=null){
			if(value instanceof String[]){
				this.params.put(key, (String[])value);
			}else if(value instanceof String){
				this.params.put(key, new String[]{(String)value});
			}else {
				this.params.put(key, new String[]{String.valueOf(value)});
			}
		}
	}
	public Map<String,String[]> getParameterMap(){
		return this.params;
	}
	public String getParameter(String parameter){
		String[] value=this.params.get(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value[0]);
	}
	public String getHeader(String name){
		String value=super.getHeader(name);
		if(value ==null)
			return null;
		return cleanXSS(value);
	}*/
	/**
	 * 说明SpringMVC的@RequestParam注解本质上调用的是ServletRequest中的 getParameterValues(String name) 方法而不是 getParameter(String name) 方法
	 * @param value
	 * @return
	 */
	public String cleanXSS(String value){
		if(value!=null){
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
			value = value.replaceAll("'", "&#39;");
			value = value.replaceAll("eval\\((.*)\\)", "");
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value=super.getHeader(name);
		if(value ==null)
			return null;
		return cleanXSS(value);
	}

	@Override
	public String getParameter(String name) {
		String[] value=this.params.get(name);
		if (value == null) {
			return null;
		}
		return cleanXSS(value[0]);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return this.params;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return super.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] value=this.params.get(name);
		if(value==null){
			return null;
		}
		int count=value.length;
		String[] v=new String[count];
		for(int i=0;i<count;i++){
			v[i]=cleanXSS(value[i]);
		}
		return v;
	}
	public void addParameter(String key,Object value){
		if(value!=null){
			if(value instanceof String[]){
				this.params.put(key, (String[])value);
			}else if(value instanceof String){
				this.params.put(key, new String[]{(String)value});
			}else {
				this.params.put(key, new String[]{String.valueOf(value)});
			}
		}
	}
}
