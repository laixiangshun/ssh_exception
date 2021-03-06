package com.java.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体查询条件封装类
 * @author fox
 * 
 */
public class QueryCondition {

	private List<QueryProperty<?>> qPropertys;// 查询条件列表
	private int pageIndex;// 第几页
	private int pageSize;// 每页几条
	
	public QueryCondition(){
		qPropertys = new ArrayList<QueryProperty<?>>();
	}
	public QueryCondition(int pageIndex,int pageSize){
		this.pageIndex=pageIndex;
		this.pageSize=pageSize;
		qPropertys = new ArrayList<QueryProperty<?>>();
	}
	/**
	 * 查询条件列表
	 */
	public List<QueryProperty<?>> getqPropertys() {
		return qPropertys;
	}

	public void setqPropertys(List<QueryProperty<?>> qPropertys) {
		this.qPropertys = qPropertys;
	}
	/**
	 * 第几页
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	/**
	 * 每页几条
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	

}
