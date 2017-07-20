package com.java.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class MongoDBHelp{
	public static <T> Query searchLike(int index, int pageSize, T model) {
		Query query = new Query();
		Field[] fields = model.getClass().getDeclaredFields();
		for(Field field : fields){
			String name = field.getName();//获取属性名
			String key = name;
			String type = field.getType().toString();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);//属性首字母大写
			try {
				Method m = model.getClass().getMethod("get" + name);//构造get方法
				Object value = m.invoke(model);//调用get方法获取值
				if(value != null){
					if(type.endsWith("String")){
						Pattern pattern = Pattern.compile("^.*" + value.toString() +".*$", Pattern.CASE_INSENSITIVE);//字段类型为String的进行模糊查询
						query.addCriteria(Criteria.where(key).regex(pattern));
					}else{
						query.addCriteria(Criteria.where(key).is(value));
					}
				}
			}catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return query;
	}
}
