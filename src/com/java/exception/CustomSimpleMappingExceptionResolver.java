package com.java.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.conn.Wire;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.java.utils.BeanToJson;
import com.java.viewmodel.ViewModel;

/**
 * 自定义实现SimpleMappingExceptionResolver的类：
 * 在sevice层我们需要将建立的异常抛出，在controller层，我们需要捕捉异常，将其转换直接抛出的异常，
 * 希望能通过我们自己统一的配置，支持普通页面和ajax方式的页面处理
 * 重写方法doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)方法，通过修改该方法实现普通异常和ajax异常的处理
 * @author lailai
 *
 */
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver{

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String viewname=determineViewName(ex, request);
		if(viewname!=null){
			//非ajax请求，jsp格式返回
			if(!(request.getHeader("accept").indexOf("application/json")>-1 || 
					(request.getHeader("X-Requested-With")!=null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest")>-1))){
				Integer statuscode=determineStatusCode(request, viewname);
				if(statuscode!=null){
					applyStatusCodeIfPossible(request, response, statuscode);
				}
				String message=ex.getMessage();
				if(ex instanceof BusinessException){
					BusinessException be=(BusinessException) ex;
					if(be.getStatuscode()==302){
						String href=be.getHref();
						if(href!=null){
							href=request.getContextPath()+"/"+href;
						}
						try {
							response.sendRedirect(href);
							return null;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					message+="系统错误,请联系管理员";
				}
				request.setAttribute("message", message);
				return getModelAndView(viewname, ex, request);
			}else{
				//ajax请求，返回json格式
				response.setStatus(400);
				response.setContentType("text/json;charset=utf-8");
				try {
					PrintWriter writer=response.getWriter();
					ViewModel viewModel=new ViewModel<>();
					viewModel.setSuccessful(false);
					String message=ex.getMessage();
					if(ex instanceof BusinessException){
						BusinessException be=(BusinessException) ex;
						viewModel.setErrorCode(be.getStatuscode());
						viewModel.setHref(be.getHref());
					}else{
						viewModel.setErrorCode(response.getStatus());
						message="系统错误,前联系管理员";
					}
					viewModel.setMessage(message);
					String str=BeanToJson.beanToJson(viewModel.getMessage());
					writer.write(str);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}else{
			return null;
		}
	}
//当在系统应用中出现普通异常时，根据是系统异常还是应用异常，跳到相应的界面，当ajax异常时，在ajax的error中可直接获得异常
	
}
