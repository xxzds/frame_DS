package com.anjz.core.controller.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anjz.base.Constants;
import com.anjz.base.util.WebUtils;
import com.anjz.core.controller.wechat.encrypt.WXBizMsgCrypt;
import com.anjz.core.controller.wechat.util.JssdkSignUtil;
import com.anjz.core.controller.wechat.util.SignUtil;
import com.anjz.core.model.wechat.auth.AccessTokenSucessEntity;
import com.anjz.core.model.wechat.auth.UserInfo;
import com.anjz.core.service.intf.wechat.WechatMessageService;
import com.anjz.core.service.intf.wechat.WechatService;
import com.anjz.result.PlainResult;
import com.anjz.util.PropertiesUtil;

/**
 * 
 * @author ding.shuai
 * @date 2016年9月24日上午9:28:41
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {
	private static final Logger logger=LoggerFactory.getLogger(WechatController.class);
	
	//令牌
	@Value("${wechat.token}")
	private String token;
	
	//应用id
	@Value("${wechat.appid}")
	private String appId;
	
	//加解密秘钥
	@Value("${wechat.encodingAESKey}")
	private String encodingAesKey;
	
	@Resource
	private WechatService wechatService;
	
	@Resource
	private WechatMessageService wechatMessageService;
		
	@RequestMapping("")
	public void accessAuth(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String result ="";
		try{
			request.setCharacterEncoding("UTF-8");  
	        response.setCharacterEncoding("UTF-8");  
	        
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			// 随机字符串
			String echostr = request.getParameter("echostr"); 
			//密文的安全签名串
			String msg_signature=request.getParameter("msg_signature");
			
			logger.info("微信加密签名:{},时间戳：{},随机数:{},随机字符串:{},令牌:{},密文的安全签名串:{}"
					,signature,timestamp,nonce,echostr,token,msg_signature);
	  		
			
			// 通过检验signature对请求进行校验
			if (SignUtil.checkSignature(signature, timestamp, nonce,token)) {			
				// 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回
				if(StringUtils.isNotEmpty(echostr)){
					result=echostr;
					logger.info("微信接入成功！");
				}else{				
					//读取接收到的xml消息
			        StringBuffer sb = new StringBuffer();  
			        InputStream is = request.getInputStream();  
			        InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
			        BufferedReader br = new BufferedReader(isr);  
			        String s = "";  
			        while ((s = br.readLine()) != null) {  
			            sb.append(s);  
			        }  
			        String xml = sb.toString();
			        logger.info("微信端发送的数据:"+xml);
			        
			        //如果msg_signature不为空，则微信发送的数据是加密的，此处进行解密
			        if(StringUtils.isNotEmpty(msg_signature)){
			        	WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
			        	xml = pc.decryptMsg(msg_signature, timestamp, nonce, xml);
			        	logger.info("解密后的xml:"+xml);
			        }
					//正常的微信处理流程  
		            result = wechatMessageService.processWechatMag(xml);
		            
		            //加密
		            if(StringUtils.isNotEmpty(msg_signature)){
		            	WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
		            	result = pc.encryptMsg(result, timestamp, nonce);
		            }
				}				
			}
			
		}catch(Exception e){
			result="";
			logger.info("微信请求响应异常{}",e.getMessage());
		}
		logger.info("服务端响应的数据:"+result);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		out = null;
	}	
	
	
	/**
	 * 授权的回调
	 * @return
	 */
	@RequestMapping("authorize/callback")
	public String  authorizeCallback(
			@RequestParam(value="code",required=false) String code,
			@RequestParam(value="state",required=false) String state,
			Model model){
		if(StringUtils.isNotEmpty(code)){
			PlainResult<AccessTokenSucessEntity> result = wechatService.accessTokenWithAuth(code);
			if(!result.isSuccess()){
				model.addAttribute(Constants.ERROR, result.getMessage());
				return "/wechat/authorize/callback";
			}
			
			AccessTokenSucessEntity accessTokenSucessEntity=result.getData();
			String access_token = accessTokenSucessEntity.getAccess_token();
			String openid=accessTokenSucessEntity.getOpenid();
			
			
			PlainResult<UserInfo> userInfoResult= wechatService.getUserInfo(access_token, openid);
			if(!userInfoResult.isSuccess()){
				model.addAttribute(Constants.ERROR, userInfoResult.getMessage());
				return "/wechat/authorize/callback";
			}
			model.addAttribute("userInfo", userInfoResult.getData());
			
			//检验授权凭证（access_token）是否有效
			wechatService.auth(access_token, openid);
		}
		
		return "/wechat/authorize/callback";
	}
	
	/**
	 * 网页-通过js调用微信功能
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("web")
	public String web(Model model,HttpServletRequest request){	
		
		String jsapi_ticket = wechatService.getTicket();		
		String url = WebUtils.getCurrentUrl(request);		
		Map<String, Object> signPackage= JssdkSignUtil.getSignPackage(jsapi_ticket, url);
		signPackage.put("appid", PropertiesUtil.getInstance("wechat.properties").getValue("wechat.appid"));
		model.addAttribute("signPackage", signPackage);
				
		return "/wechat/web/index";
	}
	
	@RequestMapping("accesstoken")
	@ResponseBody
	public String getAccessToken(){
		return  wechatService.getAccessToken();		
	}
	
	@RequestMapping("ip")
	@ResponseBody
	public List<String> getIp(){
		return wechatService.getWechatServerIp();		
	}
}
