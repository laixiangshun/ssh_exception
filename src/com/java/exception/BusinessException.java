package com.java.exception;

public class BusinessException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int statuscode;
	private String href;
	
	public BusinessException(){}
	
	public BusinessException(String message){
		super(message);
	}
	public BusinessException(Throwable cause){
		super(cause);
	}
	public BusinessException(String message,int statuscode,String href){
		super(message);
		this.statuscode=statuscode;
		this.href=href;
	}
	 public BusinessException(String message, Throwable cause) {  
        super(message, cause);  
        // TODO Auto-generated constructor stub  
    }

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}  
	 
}
