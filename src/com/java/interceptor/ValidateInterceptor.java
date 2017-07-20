package com.java.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.User;
import com.java.utils.BeanToJson;
import com.java.utils.IsAjaxRequest;
import com.java.utils.Tools;
import com.java.viewmodel.ViewModel;

/**
 * 自定义的拦截器
 * @author lailai
 *
 */
public class ValidateInterceptor implements HandlerInterceptor{

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，
	 * 也就是DispatcherServlet渲染了视图执行，
	 * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
	 * 它的执行时间是在处理器进行处理之
	 * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
	 * ，也就是说在这个方法中你可以对ModelAndView进行操
	 * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用
	 * ，这跟Struts2里面的拦截器的执行过程有点像，
	 * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法
	 * ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
	 * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前
	 * ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
	 * ，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的
	 * ，这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		System.out.println("验证拦截器");
		HttpSession session = request.getSession();
		String requestUrl = request.getRequestURI();
		User user=(User)session.getAttribute("user");
		boolean noAuthority=true;
		if(requestUrl.endsWith("login.html") || requestUrl.endsWith("login.action") || requestUrl.endsWith("logout.action")){
			return true;
		}else{
			if(user==null){
				Redirect(request, response);
				return false;
			}else{
				String token=(String)session.getAttribute("token");
				if(!token.equals(Tools.getMd5(session.getId()))){
					Redirect(request, response);
					return false;
				}else{
					//判断权限
					if(noAuthority){
						return true;
					}else{
						if(IsAjaxRequest.isAjaxRequest(request)){
							response.setStatus(400);
							ViewModel<Object> viewModel=new ViewModel<>();
							viewModel.setMessage("每有权限");
							viewModel.setErrorCode(401);
							viewModel.setSuccessful(false);
							String json = BeanToJson.beanToJson(viewModel);
							response.setContentType("text/json;charset=utf-8");
							response.getWriter().write(json);
						}else{
							response.sendRedirect(request.getContextPath() + "/noAuthority.html");
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 重定向
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void Redirect(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(IsAjaxRequest.isAjaxRequest(request)){
			response.setStatus(400);
			ViewModel<Object> viewModel=new ViewModel<>();
			viewModel.setErrorCode(302);
			viewModel.setSuccessful(false);
			viewModel.setHref(request.getContextPath()+"/login.html");
			viewModel.setMessage("请重新登录");
			String json = BeanToJson.beanToJson(viewModel);
			response.setContentType("text/json;charset=utf-8");
			response.getWriter().write(json);
		}else{
			response.sendRedirect(request.getContextPath()+"/login.html");
		}
	}
}
