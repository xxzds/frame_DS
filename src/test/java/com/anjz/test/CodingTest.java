package com.anjz.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码测试
 * @author ding.shuai
 * @date 2017年4月8日上午11:10:04
 */
public class CodingTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodingTest.class);
	
	@Test
	public void test() throws UnsupportedEncodingException{
		String utf8encoder = URLEncoder.encode("你好","utf-8");
		String gbkencoder = URLEncoder.encode("你好","gbk");
		
		
		
		LOGGER.info(utf8encoder);
		LOGGER.info(gbkencoder);
		//乱码
		LOGGER.info( URLDecoder.decode(utf8encoder,"gbk"));
		LOGGER.info( URLDecoder.decode(utf8encoder,"utf-8"));
		
		String utf8encoder2 = URLEncoder.encode(utf8encoder, "utf-8");
		
		LOGGER.info(utf8encoder2);
		LOGGER.info( URLDecoder.decode(utf8encoder2,"gbk"));
		
		
		LOGGER.info( URLEncoder.encode("http://www.baidu.com?name=你好","utf-8"));
		LOGGER.info( URLEncoder.encode("http://www.baidu.com?name=你好","iso-8859-1"));
		
		LOGGER.info( URLDecoder.decode("http://www.baidu.com?name=你好","utf-8"));
		LOGGER.info( URLDecoder.decode("http%3A%2F%2Fwww.baidu.com%3Fname%3D%E4%BD%A0%E5%A5%BD","utf-8"));
		LOGGER.info( URLDecoder.decode("http%3A%2F%2Fwww.baidu.com%3Fname%3D%E4%BD%A0%E5%A5%BD","gbk"));
		LOGGER.info( URLDecoder.decode("http%3A%2F%2Fwww.baidu.com%3Fname%3D%E4%BD%A0%E5%A5%BD","iso-8859-1"));
		LOGGER.info( URLDecoder.decode("http%3A%2F%2Fwww.baidu.com%3Fname%3D%3F%3F","utf-8"));
		LOGGER.info(URLEncoder.encode("http%3A%2F%2Fwww.baidu.com%3Fname%3D%3F%3F","iso-8859-1"));
		
		
		String name ="你好";
		System.out.println(Arrays.toString(name.getBytes("utf-8")));
		//客户端(utf-8)
		String utf8bm =URLEncoder.encode(name,"utf-8");		
		System.out.println("utf-8编码后:"+utf8bm);
		System.out.println(Arrays.toString(utf8bm.getBytes("utf-8")));
		
		//服务端(iso-8859-1)	
		//第一步，二进制转字符串
		String fwdStr = new String(utf8bm.getBytes("utf-8"),"iso-8859-1");
		System.out.println(fwdStr);
		String iso8859jm=URLDecoder.decode(fwdStr,"iso-8859-1");
		System.out.println("iso-8859-1解码后:"+iso8859jm);
		System.out.println(Arrays.toString(iso8859jm.getBytes("iso-8859-1")));
		System.out.println(new String(iso8859jm.getBytes("iso-8859-1"),"utf-8"));
	}
}
