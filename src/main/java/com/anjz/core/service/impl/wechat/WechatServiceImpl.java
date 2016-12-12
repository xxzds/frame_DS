package com.anjz.core.service.impl.wechat;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anjz.core.model.wechat.FailEntity;
import com.anjz.core.model.wechat.MenuEntity;
import com.anjz.core.model.wechat.QueryMenuEntity;
import com.anjz.core.model.wechat.auth.AccessTokenSucessEntity;
import com.anjz.core.model.wechat.auth.UserInfo;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity;
import com.anjz.core.service.intf.wechat.WechatService;
import com.anjz.exception.BusinessException;
import com.anjz.http.HttpCallService;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;
import com.anjz.util.PropertiesUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * 微信服务
 * @author ding.shuai
 * @date 2016年9月25日上午10:57:05
 */
@Service
public class WechatServiceImpl  implements WechatService{

	@Resource
	private HttpCallService httpCallService;
	
	/**
	 * 获取access_token,存入缓存中
	 * 接口返回的数据
	 * {"access_token":"ACCESS_TOKEN","expires_in":7200} 成功
	 * {"errcode":40013,"errmsg":"invalid appid"}  错误的一种
	 * @return
	 */
	@Override
	@Cacheable(value="wechatCache",key="'access_token'")
	public String getAccessToken() throws BusinessException{
		PropertiesUtil propertiesUtil= PropertiesUtil.getInstance("wechat.properties");
		String access_token_url= propertiesUtil.getValue("wechat.access_token_url");
		String appid = propertiesUtil.getValue("wechat.appid");
		String secret = propertiesUtil.getValue("wechat.secret");
		//格式化		
		access_token_url=MessageFormat.format(access_token_url,appid,secret);
		PlainResult<String> result= httpCallService.httpGet(access_token_url);
		if(!result.isSuccess()){
			throw new BusinessException(result.getMessage());
		}
		JSONObject object= JSON.parseObject(result.getData());
		String access_token=(String)object.get("access_token");
		
		if(StringUtils.isEmpty(access_token)){
			throw new BusinessException("错误码："+object.get("errcode")+",错误信息："+object.get("errmsg"));
		}			
		return access_token;
	}
	
	
	/**
	 * 获取ticket,ticket是公众号用于调用微信JS接口的临时票据
	 * @return
	 */
	@Override
	@Cacheable(value="wechatCache",key="'jsapi_ticket'")
	public String  getTicket(){
		String result=null;
		String ticket_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.ticket_url");		
		//格式化
		ticket_url=MessageFormat.format(ticket_url,((WechatService)AopContext.currentProxy()).getAccessToken()); 
		PlainResult<String> responseResult= httpCallService.httpGet(ticket_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		JSONObject object= JSON.parseObject(responseResult.getData());
		if(object.getInteger("errcode")==0){
			result=object.getString("ticket");
		}else{
			throw new BusinessException("错误码："+object.get("errcode")+",错误信息："+object.get("errmsg"));
		}
		return result;
	}
	
	/**
	 * 获取微信服务器IP地址
	 * @return
	 */
	@Override
	public List<String> getWechatServerIp() throws BusinessException{
		List<String> reuslt=Lists.newArrayList();
		
		String callbackip_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.callbackip_url");		
		//格式化
		callbackip_url=MessageFormat.format(callbackip_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		
		PlainResult<String> result= httpCallService.httpGet(callbackip_url);
		if(!result.isSuccess()){
			throw new BusinessException(result.getMessage());
		}
		JSONObject object= JSON.parseObject(result.getData());
		Object ipObject= object.get("ip_list");
		if(ipObject==null){
			throw new BusinessException("错误码："+object.get("errcode")+",错误信息："+object.get("errmsg"));
		}
		JSONArray array=null;
		if(ipObject instanceof JSONArray){
			array=(JSONArray)ipObject;
			reuslt= Lists.transform(array, new Function<Object, String>() {
				@Override
				public String apply(Object input) {
					return (String)input;
				}
			});
		}
		
		return reuslt;
	}
	
	/**
	 * 自定义菜单创建
	 * @param menu
	 * @return
	 */
	public BaseResult createMenu(MenuEntity menu){
		BaseResult result=new BaseResult();
		
		String create_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.create_url");		
		//格式化
		create_url=MessageFormat.format(create_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		
		PlainResult<String> responseResult= httpCallService.urlConnectionPost(create_url, JSON.toJSONString(menu));
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		JSONObject object= JSON.parseObject(responseResult.getData());
		Integer errcode= object.getInteger("errcode");
		if(errcode != 0){
			result.setErrorMessage(errcode, object.getString("errmsg"));
		}
		return result;
	}
	
	/**
	 * 自定义菜单创建
	 * 
	 * @param jsonStr  菜单json字符串
	 * @return
	 */
	public BaseResult createMenu(String jsonStr) {
		BaseResult result=new BaseResult();
		
		String create_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.create_url");		
		//格式化
		create_url=MessageFormat.format(create_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		
		PlainResult<String> responseResult= httpCallService.urlConnectionPost(create_url, jsonStr);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		JSONObject object= JSON.parseObject(responseResult.getData());
		Integer errcode= object.getInteger("errcode");
		if(errcode != 0){
			result.setErrorMessage(errcode, object.getString("errmsg"));
		}
		return result;
	}
	
	/**
	 * 菜单查询
	 * @return
	 */
	public QueryMenuEntity menu(){
		String menu_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.menu_url");		
		//格式化
		menu_url=MessageFormat.format(menu_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		
		PlainResult<String> responseResult=httpCallService.httpGet(menu_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		QueryMenuEntity queryMenuEntity = JSON.parseObject(responseResult.getData(), QueryMenuEntity.class);		
		return queryMenuEntity;
	}
	
	/**
	 * 删除菜单
	 * @return
	 */
	public BaseResult deleteMenu(){
		BaseResult result = new BaseResult();
		
		String delete_menu_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.menu_delete_url");		
		//格式化
		delete_menu_url=MessageFormat.format(delete_menu_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		PlainResult<String> responseResult=httpCallService.httpGet(delete_menu_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		JSONObject object= JSON.parseObject(responseResult.getData());
		Integer errcode= object.getInteger("errcode");
		if(errcode != 0){
			result.setErrorMessage(errcode, object.getString("errmsg"));
		}
		return result;
	}
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 */
	public PlainResult<AccessTokenSucessEntity> accessTokenWithAuth(String code){
		if(StringUtils.isEmpty(code)){
			throw new BusinessException("参数code不能为空");
		}
		
		PlainResult<AccessTokenSucessEntity> result=new PlainResult<AccessTokenSucessEntity>();
		PropertiesUtil propertiesUtil= PropertiesUtil.getInstance("wechat.properties");
		String appid = propertiesUtil.getValue("wechat.appid");
		String secret = propertiesUtil.getValue("wechat.secret");
		String authorize_access_token_url= propertiesUtil.getValue("wechat.authorize_access_token_url");		
		//格式化
		authorize_access_token_url=MessageFormat.format(authorize_access_token_url,appid,secret,code);
		PlainResult<String> responseResult=httpCallService.httpGet(authorize_access_token_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
				
		AccessTokenSucessEntity successEntity = JSON.parseObject(responseResult.getData(),AccessTokenSucessEntity.class);
		if(StringUtils.isNotEmpty(successEntity.getAccess_token())){
			result.setData(successEntity);
		}else{
			FailEntity failEntity=JSON.parseObject(responseResult.getData(), FailEntity.class);
			result.setErrorMessage(failEntity.getErrcode(), failEntity.getErrmsg());
		}
		return result;
	}
	
	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * @param access_token  网页授权接口调用凭证
	 * @param openid        用户的唯一标识
	 * @return
	 */
	public PlainResult<UserInfo> getUserInfo(String access_token,String openid){
		if(StringUtils.isEmpty(access_token)){
			throw new BusinessException("参数access_token不能为空");
		}
		if(StringUtils.isEmpty(openid)){
			throw new BusinessException("参数openid不能为空");
		}
		PlainResult<UserInfo> result=new PlainResult<UserInfo>();
		
		String userinfo_url = PropertiesUtil.getInstance("wechat.properties").getValue("wechat.userinfo_url");
		userinfo_url=MessageFormat.format(userinfo_url, access_token,openid);
		PlainResult<String> responseResult=httpCallService.httpGet(userinfo_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		
		UserInfo userInfo=JSON.parseObject(responseResult.getData(), UserInfo.class);
		if(StringUtils.isEmpty(userInfo.getOpenid())){
			FailEntity failEntity=JSON.parseObject(responseResult.getData(), FailEntity.class);
			result.setErrorMessage(failEntity.getErrcode(), failEntity.getErrmsg());
		}else{
			result.setData(userInfo);
		}		
		return result;
	}
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param access_token      网页授权接口调用凭证
	 * @param openid            用户的唯一标识
	 * @return
	 */
	public BaseResult auth(String access_token,String openid){
		if(StringUtils.isEmpty(access_token)){
			throw new BusinessException("参数access_token不能为空");
		}
		if(StringUtils.isEmpty(openid)){
			throw new BusinessException("参数openid不能为空");
		}
		BaseResult result =new BaseResult();
		String auth_url = PropertiesUtil.getInstance("wechat.properties").getValue("wechat.auth_url");
		auth_url=MessageFormat.format(auth_url, access_token,openid);
		
		PlainResult<String> responseResult=httpCallService.httpGet(auth_url);
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		FailEntity failEntity=JSON.parseObject(responseResult.getData(), FailEntity.class);
		if(failEntity.getErrcode()!=0){
			result.setErrorMessage(failEntity.getErrcode(), failEntity.getErrmsg());
		}		
		return result;
	}
	
	/**
	 * 创建二维码ticket
	 * @param entity
	 * @return
	 */
	public String crateQrcode(QrcodeCreateEntity entity){
		String qrcode_create_url= PropertiesUtil.getInstance("wechat.properties").getValue("wechat.qrcode_create_url");		
		//格式化
		qrcode_create_url=MessageFormat.format(qrcode_create_url,((WechatService)AopContext.currentProxy()).getAccessToken());
		
		PlainResult<String> responseResult = httpCallService.urlConnectionPost(qrcode_create_url, JSON.toJSONString(entity));
		if(!responseResult.isSuccess()){
			throw new BusinessException(responseResult.getMessage());
		}
		JSONObject object= JSON.parseObject(responseResult.getData());
		String ticket=(String)object.get("ticket");
		
		if(StringUtils.isEmpty(ticket)){
			throw new BusinessException("错误码："+object.get("errcode")+",错误信息："+object.get("errmsg"));
		}			
		
		return ticket;		
	}
}
