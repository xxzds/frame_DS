package com.anjz.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ding.shuai
 * @date 2016年8月22日上午9:05:22
 */
public class ReflectUtils {

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        Type parameterizedType = clazz.getGenericSuperclass();
        //CGLUB subclass target object(泛型在父类上)
        if (!(parameterizedType instanceof ParameterizedType)) {
            parameterizedType = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(parameterizedType instanceof  ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return null;
        }
        return (Class<T>) actualTypeArguments[index];
    } 
    
    
    /**
     * 获取类型，比如：com.test.Test  返回Test
     * @param clazz
     * @return
     */
    public static String getClassName(Class<?> clazz){
    	String pathClass=clazz.getName();
    	return pathClass.substring(pathClass.lastIndexOf(".")+1);
    }
}