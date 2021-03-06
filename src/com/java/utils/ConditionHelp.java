package com.java.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.Query;

/**
 * 根据查询条件封装类和实体类型返回sql
 * (针对单个实体进行查询)
 * @author fox
 *
 */
public class ConditionHelp {

	public static <T> String getConditionString(QueryCondition condition) {
		String result = "";
		StringBuilder sb = new StringBuilder();
		try {
			if (condition.getqPropertys().size()>0) {
				String aName = "";//model属性名
				String type ="";//model属性的类型（简单类型）
				String value = "";//model属性的值
				String order=" order by ";//排序条件
				int ordernum=0;//参与排序的条件个数
				Field field = null;
				String prefix="";
				for (QueryProperty<?> item : condition.getqPropertys()) {
					aName = item.getName();
					if(item.getOrderby()!=null&&!item.getOrderby().isEmpty()){
						if(ordernum>0){
							//非第一个参与排序的条件
							order+=","+aName+" "+item.getOrderby();
						}else{
							//第一个参与排序的条件
							order+=aName+" "+item.getOrderby();
						}
						ordernum++;
					}
					if(item.getValue()==null||item.getValue().isEmpty()){
						continue;//如果查询条件为空
					}
					prefix="";
					field = item.getCurrclass().getClass().getDeclaredField(aName);
					field.setAccessible(true);
//					PropertyDescriptor pd = new PropertyDescriptor(
//							field.getName(), item.getCurrclass().getClass());
					type =field.getType().getSimpleName();
					if (item.getComparesymbol()!=null&&item.getComparesymbol().equals(QueryProperty.LIKE))
					{
						prefix="%";
					}
					switch (type.toLowerCase()) {
					case "string":
						if(item.getValue()!=null&&!item.getValue().isEmpty()){
							value = "'" +prefix+ item.getValue()+prefix + "'";
						}
						break;
					case "date":
						if(item.getValue()!=null&&!item.getValue().isEmpty()){
							value = "'" +prefix+ item.getValue()+prefix + "'";
						}
						break;
					default:
						value = item.getValue();
						break;
					}
					if (item.getAliaName() != null
							&& item.getAliaName().length() > 0) {
						aName = item.getAliaName() + "." + item.getName();
					}
					if(!value.trim().isEmpty()){
						//如果查询条件的值为空，就不加入该查询条件
						sb.append(" " + item.getLogicSymbol() +" "+ aName+" "+item.getComparesymbol()+" "+ value + " ");
					}
					
				}
				if(ordernum>0){
					sb.append(order);//加入排序条件
				}
				result = sb.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result="";
		}
		return result;

	}
	
	public static String setParamter(Query query,QueryCondition condition,List<String> parameters)
	{
		String result = "";
		//String commandText=query.getQueryString();

		StringBuilder sb = new StringBuilder();
		try {
			if (condition.getqPropertys().size()>0) {
				String aName = "";//model属性名
				String type ="";//model属性的类型（简单类型）
				Object value = "";//model属性的值
				String order=" order by ";//排序条件
				int ordernum=0;//参与排序的条件个数
				Field field = null;
				String prefix="";
				for (QueryProperty<?> item : condition.getqPropertys()) {
					aName = item.getName();
					if(item.getOrderby()!=null&&!item.getOrderby().isEmpty()){
						if(ordernum>0){
							//非第一个参与排序的条件
							order+=","+aName+" "+item.getOrderby();
						}else{
							//第一个参与排序的条件
							order+=aName+" "+item.getOrderby();
						}
						ordernum++;
					}
					if(item.getValue()==null){
						continue;//如果查询条件为空
					}
					prefix="";
					field = item.getCurrclass().getClass().getDeclaredField(aName);
					field.setAccessible(true);
					
					//PropertyDescriptor pd = new PropertyDescriptor(
							//field.getName(), item.getCurrclass().getClass());
					//type = pd.getPropertyType().getSimpleName();
					type=field.getType().getSimpleName();
					if (item.getComparesymbol()!=null&&item.getComparesymbol().equals(QueryProperty.LIKE))
					{
						prefix="%";
					}
					switch (type.toLowerCase()) {
					case "string":
						if(item.getValue()!=null&&!item.getValue().isEmpty()){
							value = "'" +prefix+ item.getValue()+prefix + "'";
						}
						break;
					case "integer":
						if(item.getValue()!=null&&!item.getValue().isEmpty()){
							value = Integer.valueOf(item.getValue());
						}
						break;
					default:
						value = item.getValue();
						break;
					}
					if (item.getAliaName() != null
							&& item.getAliaName().length() > 0) {
						aName = item.getAliaName() + "." + item.getName();
					}
					boolean isParam=false;
					for(String paramItem :parameters)
					{
						if (paramItem.toLowerCase().equals(item.getName())==true)
						{
							if (query!=null)
							{
								
					           query.setParameter(item.getName(), value);
							}
					        isParam=true;
					        break;
						}
					}
					
					
					if(!value.toString().trim().isEmpty()){
						//如果查询条件的值为空，就不加入该查询条件
						
						if (isParam==false)
						{
						    sb.append(" " + item.getLogicSymbol() +" "+ aName+" "+item.getComparesymbol()+" "+ value + " ");
						}
					}
					
				}
				if(ordernum>0){
					sb.append(order);//加入排序条件
				}
				result = sb.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result="";
		}
		
		return result;
	}

}
