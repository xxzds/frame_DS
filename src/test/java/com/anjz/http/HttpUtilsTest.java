package com.anjz.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.http.core.HttpUrlConnectionUtil;

/**
 * @author ding.shuai
 * @date 2017年4月9日下午4:53:08
 */
public class HttpUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtilsTest.class);
	
	
	@Test
	public void httpUrlConnectionUtilTest(){
		try{
			HttpUrlConnectionUtil.doPost("http://localhost:9999/kj/test/ajax3", "{\"name\":\"你好\"}", 60000);	
//			HttpUrlConnectionUtil.doGet("http://www.xh99d.com", null, 5000);
		}catch(Exception e){
			LOGGER.error("exception:",e);
		}
	}
	
	/**
	 * 模拟微信授权，更改请求头中的User-Agent，没有成功，看来还有其他封锁
	 */
	@Test
	public void simulationAuth() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httpget.
		HttpGet httpGet = new HttpGet("http://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb488456d88e40620&redirect_uri=http://wi15820987.51mypc.cn/wlgz/pages/mobile/myAccount/myAccount.jsp&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");	
		//模拟浏览器头部
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1 wechatdevtools/0.7.0 MicroMessenger/6.3.9 Language/zh_CN webview/0");
		httpGet.setHeader("Upgrade-Insecure-Requests","1");
		httpGet.setHeader("Connection","keep-alive");
		httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8");
		httpGet.setHeader("Accept-Encoding","gzip, deflate, sdch");
		httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		
		//请求头部
				Header[] requestHeaders = httpGet.getAllHeaders();
				for(Header h:requestHeaders){
					System.out.println(h.getName() + ":" + h.getValue());
				}
				System.out.println("--------------------------------------------------");
		try {
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			LOGGER.info(statusLine.toString());

			// 如果返回的状态码是302，从http头部获取客户端跳转的地址
			if (statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
				String location = response.getFirstHeader("location").getValue();
				System.out.println("客户端跳转地址:" + location);
			}

			// 获取响应实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				LOGGER.info(EntityUtils.toString(entity, "UTF-8"));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
