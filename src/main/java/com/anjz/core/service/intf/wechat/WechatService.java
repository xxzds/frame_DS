package com.anjz.core.service.intf.wechat;

import java.util.List;

import com.anjz.core.model.wechat.MenuEntity;
import com.anjz.core.model.wechat.QueryMenuEntity;
import com.anjz.core.model.wechat.auth.AccessTokenSucessEntity;
import com.anjz.core.model.wechat.auth.UserInfo;
import com.anjz.core.model.wechat.qrcode.QrcodeCreateEntity;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;

/**
 * 微信服务
 * @author ding.shuai
 * @date 2016年9月25日上午10:56:28
 */
public interface WechatService {

	/**
	 * 获取access_token,存入缓存中
	 * @return
	 */
	public String getAccessToken() throws BusinessException;
	
	
	/**
	 * 获取ticket,ticket是公众号用于调用微信JS接口的临时票据
	 * @return
	 */
	public String  getTicket();
	
	
	/**
	 * 获取微信服务器IP地址
	 * @return
	 */
	public List<String> getWechatServerIp() throws BusinessException;
	
	
	/**
	 * 自定义菜单创建
	 * @param menu
	 * @return
	 */
	public BaseResult createMenu(MenuEntity menu);
	
	/**
	 * 菜单查询
	 * @return
	 */
	public QueryMenuEntity menu();
	
	/**
	 * 删除菜单
	 * @return
	 */
	public BaseResult deleteMenu();
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 */
	public PlainResult<AccessTokenSucessEntity> accessTokenWithAuth(String code);
	
	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * @param access_token  网页授权接口调用凭证
	 * @param openid        用户的唯一标识
	 * @return
	 */
	public PlainResult<UserInfo> getUserInfo(String access_token,String openid);
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param access_token      网页授权接口调用凭证
	 * @param openid            用户的唯一标识
	 * @return
	 */
	public BaseResult auth(String access_token,String openid);
	
	
	/**
	 * 创建二维码ticket
	 * @param entity
	 * @return
	 */
	public String crateQrcode(QrcodeCreateEntity entity);
	
}
