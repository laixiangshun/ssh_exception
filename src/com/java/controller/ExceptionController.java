package com.java.controller;

import java.io.PrintWriter;

import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.java.exception.BusinessException;
import com.java.exception.SystemException;
import com.java.utils.BeanToJson;
import com.java.utils.IsAjaxRequest;
import com.java.viewmodel.ViewModel;

/**
 * 异常测试类
 * @author lailai
 *
 */
@Controller
public class ExceptionController {

	@RequestMapping(value="/exception.action",method=RequestMethod.GET)
	public void Exception(HttpServletRequest request,HttpServletResponse response,Integer id) throws Exception{
		switch (id) {
		case 1:
			BusinessException be=new BusinessException("重定向操作");
			be.setStatuscode(302);
			be.setHref("index.html");
			throw be;
		case 2:
			throw new SystemException("controller运行错误");
		case 3:
			throw new SystemException();
		case 4:
			throw new RuntimeException();
		default:
			if(IsAjaxRequest.isAjaxRequest(request)){
				response.setContentType("text/json;charset=utf-8");
	            PrintWriter writer = response.getWriter();
	            ViewModel<Object> viewModel = new ViewModel<>();
	            viewModel.setSuccessful(true);
	            viewModel.setMessage("ok");
	            String json = BeanToJson.beanToJson(viewModel);
	            writer.write(json);  
	            writer.close();
			}else{
				response.sendRedirect(request.getContextPath()+"/index.html");
			}
			break;
		}
	}
	
	/**
	 * 每一权限跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/noAuthority.html",method=RequestMethod.GET)
	public String noAuthority(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return "noAuthority";
	}
}
