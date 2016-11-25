package com.anjz.core.service.impl.wechat;

import org.springframework.stereotype.Service;

import com.anjz.core.model.wechat.Music;
import com.anjz.core.model.wechat.ReceiveXmlEntity;
import com.anjz.core.service.impl.wechat.util.FormatXml;
import com.anjz.core.service.impl.wechat.util.ReciveXmlParse;
import com.anjz.core.service.impl.wechat.util.TulingApiProcess;
import com.anjz.core.service.intf.wechat.WechatMessageService;

/**
 * 微信xml消息推送的服务
 * @author ding.shuai
 * @date 2016年9月27日下午10:05:58
 */
@Service
public class WechatMessageServiceImpl implements WechatMessageService{

	/**
	 * 处理xml数据
	 * @param xml  微信传入的数据
	 * @return  返回xml格式的数据
	 */
	@Override
	public String  processWechatMag(String xml){
		String result="";
		//解析xml数据
		ReceiveXmlEntity xmlEntity=ReciveXmlParse.getMsgEntity(xml);
		
		switch (xmlEntity.getMsgType()) {
		case text:
			result=TulingApiProcess.getTulingResult(xmlEntity.getContent());
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
			break;
		case image:			
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), "你发的是图片");
			break;
		case voice:
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), "你发的是语音");
			break;
		case video:
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), "你发的是视频");
			break;
		case shortvideo:
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), "你发的是小视频");
			break;
		case location:
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), xmlEntity.getLabel());
			break;
		case link:
			result=FormatXml.formatXmlAboutText(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), "你发的是链接");
			break;
		case event:
			String eventKey = xmlEntity.getEventKey();
			if("V1001_TODAY_MUSIC".equals(eventKey)){
				Music music = new Music();
				music.setTitle("小意");
				music.setMusicURL("http://wi15820987.51mypc.cn/kj/upload/2016/09/28/faca78524c8a13f89065396154c57bee_%E5%B0%8F%E6%84%8F%E5%A4%A7%E7%8E%8B%20-%20%E5%AE%9A%E6%83%85.mp3");
//				music.setThumbMediaId("I0I0lwAkoKuJ-0DLljR5J5FnN2f3NqQFRcsoE9ihPrCLmYQdzEbbXa5OxQ1vylcu");
				
				music.setThumbMediaId("GRBnMRPtC_utkc5YTuM9hmlmyOPRJ_kVqV2nkosOlo3CljR1WyD4Lfa1U3U7mSVV");
				result=FormatXml.formatXmlAboutMusic(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), music);
			}
			break;
		default:
			break;
		}
		
		
		
		return result;
	}
}
