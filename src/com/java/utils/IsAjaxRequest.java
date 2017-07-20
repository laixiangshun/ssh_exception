package com.java.utils;

import javax.servlet.http.HttpServletRequest;

public class IsAjaxRequest {

	/**
	 * 判斷是否ajax請求
	 * @param request
	 * @return true： ajax請求，false：url請求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		if (!(request.getHeader("accept").indexOf("application/json") > -1 
				|| ( request.getHeader("X-Requested-With") != null 
					&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))){
			return false;
		}else{
			return true;
		}
	}
}
