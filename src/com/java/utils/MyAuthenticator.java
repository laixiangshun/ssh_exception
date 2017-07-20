package com.java.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮件用户密码验证类
 * @author lailai
 *
 */
public class MyAuthenticator extends Authenticator {
	private String username;
	private String password;
		
	public MyAuthenticator(String username,String password)
	{
		super();
		this.username=username;
		this.password=password;
	}
	protected PasswordAuthentication  getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
