package com.anjz.core.controller.wechat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;

import com.anjz.util.UuidUtil;
import com.google.common.collect.Maps;

/**
 * JS-SDK调用接口的校验
 * @author ding.shuai
 * @date 2016年9月28日下午11:38:11
 */
public class JssdkSignUtil {

	
	/**
	 * 获取校验的所有数据
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, Object> getSignPackage(String jsapi_ticket,String url){
		Map<String, Object> map=Maps.newHashMap();
		
		String noncestr=getNoncestr();
		Long timestamp=getTimestamp();
		String signature = "";
		
		//注意这里参数名必须全部小写，且必须有序
		String string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
		try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
		
//		map.put("url", url);
//		map.put("jsapi_ticket", jsapi_ticket);
		map.put("nonceStr", noncestr);
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		return map;
	}
	
	private static String getNoncestr(){
		return UuidUtil.generateUuid32();
	}
	
	private static Long getTimestamp(){
		return System.currentTimeMillis() /1000;
	}
	
	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
