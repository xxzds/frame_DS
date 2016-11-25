package com.anjz.wechat;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.anjz.BaseTest;
import com.anjz.core.model.wechat.MenuEntity;
import com.anjz.core.model.wechat.MenuEntity.Button;
import com.anjz.core.model.wechat.QueryMenuEntity;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity.ActionInfo;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity.ActionName;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity.Scene;
import com.anjz.core.service.intf.wechat.WechatService;
import com.anjz.util.PropertiesUtil;
import com.google.common.collect.Lists;

/**
 * @author ding.shuai
 * @date 2016年9月25日下午3:07:57
 */
public class WechatTest extends BaseTest{
	
	@Resource
	private WechatService wechatService;
	
	/**
	 * 获取ticket,ticket是公众号用于调用微信JS接口的临时票据
	 */
	@Test
	public void getTicketTest(){
		wechatService.getTicket();
	}
	
	/**
	 * 获取微信服务器IP地址
	 */
	@Test
	public void getWechatServerIpTest(){
		wechatService.getWechatServerIp();
	}
	
	/**
	 * 定义菜单创建
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void createMenuTest() throws UnsupportedEncodingException{
		MenuEntity menu=new MenuEntity();
		
		List<Button> buttons=Lists.newArrayList();
		Button button=new Button();
		button.setType("click");
		button.setName("今日歌曲");
		button.setKey("V1001_TODAY_MUSIC");
		buttons.add(button);		

		List<Button> buttonxList=Lists.newArrayList();
		Button button2=new Button();
		button2.setType("view");
		button2.setName("搜索");
		button2.setUrl("http://www.soso.com/");
		buttonxList.add(button2);
		
		Button button3=new Button();
		button3.setType("view");
		button3.setName("视频");
		button3.setUrl("http://v.qq.com/");
		buttonxList.add(button3);
		
		Button button4=new Button();
		button4.setType("click");
		button4.setName("赞一下我们");
		button4.setKey("V1001_GOOD");
		buttonxList.add(button4);
		
		Button buttonxx=new Button();
		buttonxx.setType("view");
		buttonxx.setName("发布货源");
		buttonxx.setUrl("http://wi15820987.51mypc.cn/hly_wx/toPostGoods");
//		buttonxx.setUrl("http://wx.ahggwl.com/hly_wx/toPostGoods");
		buttonxList.add(buttonxx);
		
		Button button5=new Button();
		button5.setType("view");
		button5.setName("授权");
		
		PropertiesUtil propertiesUtil= PropertiesUtil.getInstance("wechat.properties");
		String authorize_url=propertiesUtil.getValue("wechat.authorize_url");
		String appid = propertiesUtil.getValue("wechat.appid");
		String redirect_uri = propertiesUtil.getValue("wechat.redirect_uri");
		String scope = propertiesUtil.getValue("wechat.scope");
		
		authorize_url=MessageFormat.format(authorize_url, appid,URLEncoder.encode(redirect_uri,"utf-8"),scope);
		
		logger.info(authorize_url);
		button5.setUrl(authorize_url);
		buttonxList.add(button5);
		
		Button buttonx=new Button();
		buttonx.setName("菜单");
		buttonx.setSub_button(buttonxList);
		
		Button buttony=new Button();
		buttony.setType("view");
		buttony.setName("寻找货源");
		buttony.setUrl("http://wi15820987.51mypc.cn/hly_wx/findgoods");
//		buttony.setUrl("http://wx.ahggwl.com/hly_wx/findgoods");
		buttons.add(buttony);
		
		buttons.add(buttonx);
		
		
		
		menu.setButton(buttons);
		System.out.println(JSON.toJSONString(menu));
		
		wechatService.createMenu(menu);			
	}
	
	/**
	 * 菜单查询
	 */
	@Test
	public void menuTest(){
		QueryMenuEntity menu= wechatService.menu();
		System.out.println(menu.getMenu().getButton().get(0).getName());
	}
	
	/**
	 *删除菜单 
	 */
	@Test
	public void deleteMenuTest(){
		wechatService.deleteMenu();
	}
	
	/**
	 * 创建二维码ticket
	 */
	@Test
	public void crateQrcode(){
		QrcodeCreateEntity entity=new QrcodeCreateEntity();
		
		entity.setExpire_seconds((long) 7200);
		entity.setAction_name(ActionName.QR_SCENE);
//		entity.setAction_name(ActionName.QR_LIMIT_SCENE);
//		entity.setAction_name(ActionName.QR_LIMIT_STR_SCENE);
		
		Scene scene=new Scene();
		scene.setScene_id((long)123);
		scene.setScene_str("123");
		
		ActionInfo actionInfo=new ActionInfo();
		actionInfo.setScene(scene);
		entity.setAction_info(actionInfo);
				
		logger.info(wechatService.crateQrcode(entity));
	}
}
