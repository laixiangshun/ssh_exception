package com.java.viewmodel;

import java.util.List;

public class ViewModel<T> {
	
	private List<T> result;//返回的具体数据
	
	private int pageIndex;//起始页，0为起始页
	
	private int pageSize;//每页数据的大小
	
	private int total;//数据总条数
	
	private int totalPage;//数据总页数

	private String message;//错误信息
	
	private int errorCode;//302-重定向；other-直接输出错误信息
	
	private boolean successful;//true-成功；false-失败
	
	private String href;//需要重定向的href
	
	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	

}
